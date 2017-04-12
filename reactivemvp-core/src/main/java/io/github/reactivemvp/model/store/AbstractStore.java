package io.github.reactivemvp.model.store;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.github.reactivemvp.model.Action;
import io.github.reactivemvp.model.Middleware;
import io.github.reactivemvp.model.Reducer;
import io.github.reactivemvp.model.State;
import io.github.reactivemvp.model.StateChangedEventArgs;
import io.github.reactivemvp.model.StateChangedListener;
import io.github.reactivemvp.model.Store;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by ruoshi on 4/11/17.
 */

public abstract class AbstractStore<TState extends State> implements Store<TState> {
    protected final Object mReduceSyncRoot = new Object();
    protected final Object mMiddlewareSyncRoot = new Object();

    private final Relay<StateChangedEventArgs<TState>> mActionRelay = PublishRelay.create();

    private final Consumer<Throwable> mOnError = new Consumer<Throwable>() {
        @Override
        public void accept(@NonNull Throwable throwable) throws Exception {
        }
    };
    private TState mState;
    private List<Reducer<TState>> mReducers = Collections.emptyList();
    private List<Middleware<? extends Action>> mMiddlewareList = Collections.emptyList();

    @Override
    public TState getState() {
        return mState;
    }

    @Override
    public <TAction extends Action> TAction dispatch(@NonNull final TAction action) {
        if (executeMiddleware(action)) {
            return action;
        }

        // 目前由于reducer做的事情比较简单，只是创建对象和修改对象状态，就在调用者线程执行了
        final boolean shouldFireStateChangedEvent;
        final TState newState;
        synchronized (mReduceSyncRoot) {
            final TState originalState = mState;

            for (Reducer<TState> r : mReducers) {
                // 对比类型，如果匹配才调用

                mState = r.reduce(mState, action);
                //noinspection ConstantConditions
                if (mState == null) {
                    mState = originalState;
                }
            }
            // fire state changed event only if the state was really changed
            shouldFireStateChangedEvent = originalState != mState;
            // 在同步区域内，保证mState不会被其他线程意外的改变
            newState = mState;
        }
        if (shouldFireStateChangedEvent) {
            mActionRelay.accept(new StateChangedEventArgs<>(action, newState));
        }
        return action;
    }

    private <TAction extends Action> boolean executeMiddleware(@NonNull final TAction action) {
        if (mMiddlewareList.size() > 0) {
            // 使用 middleware 处理副作用，Middleware需要自己处理异步
            synchronized (mMiddlewareSyncRoot) {
                for (final Middleware<? extends Action> middleware : mMiddlewareList) {
                    if (action.getClass().equals(middleware.getAcceptableActionClass())) {
                        //noinspection unchecked
                        ((Middleware<TAction>) middleware)
                                .process(action)
                                .subscribe(new Consumer<Action>() {
                                               @Override
                                               public void accept(@NonNull Action resultAction) throws Exception {
                                                   dispatch(resultAction);
                                               }
                                           },
                                        new Consumer<Throwable>() {
                                            @Override
                                            public void accept(@NonNull Throwable throwable) throws Exception {
                                                onMiddlewareError(throwable);
                                            }
                                        });
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected void onMiddlewareError(@NonNull Throwable throwable) {

    }

    protected void onStateChangedListenerError(@NonNull Throwable throwable) {

    }

    @Override
    public Observable<StateChangedEventArgs<TState>> getObservable() {
        return mActionRelay.toSerialized();
    }

    @Override
    public Disposable subscribe(@NonNull final StateChangedListener<TState> stateChangedListener) {
        return mActionRelay.filter(new Predicate<StateChangedEventArgs<TState>>() {
            @Override
            public boolean test(@NonNull StateChangedEventArgs<TState> eventArgs) throws Exception {
                return stateChangedListener.hasInterestFor(eventArgs.getAction());
            }
        }).subscribe(new Consumer<StateChangedEventArgs<TState>>() {
            @Override
            public void accept(@NonNull StateChangedEventArgs<TState> eventArgs) throws Exception {
                try {
                    stateChangedListener.onStateChanged(eventArgs);
                } catch (Throwable ignore) {
                    onStateChangedListenerError(ignore);
                }
            }
        }, mOnError);
    }

    @SafeVarargs
    public final void init(@NonNull final TState initState,
                           final Reducer<TState>... reducers) {
        init(initState,
                Collections.<Middleware<? extends Action>>emptyList(),
                Arrays.asList(reducers));
    }

    public final void init(@NonNull final TState initState,
                           final List<Middleware<? extends Action>> middlewareList,
                           final List<Reducer<TState>> reducers) {
        if (initState == null) {
            throw new NullPointerException("initState is null");
        }
        mState = initState;
        mMiddlewareList = Collections.unmodifiableList(middlewareList);
        mReducers = Collections.unmodifiableList(reducers);
    }

}
