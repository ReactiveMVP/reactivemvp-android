package io.github.reactivemvp.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import io.github.reactivemvp.model.action.IncreaseCounterAction;
import io.github.reactivemvp.model.middleware.IncreaseCounterMiddleware;
import io.github.reactivemvp.model.reducer.UpdateCounterReducer;
import io.github.reactivemvp.model.store.CounterStore;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ruoshili on 4/12/2017.
 */

public class MiddlewareTest {
    private CounterStore mStore;
    private Throwable mError;
    private int mLocalCount;
    private Disposable mDisposable;

    @Before
    public void before() {
        mStore = new CounterStore();
        mStore.init(new CounterState.Builder().build(),
                Collections.<Middleware>singletonList(new IncreaseCounterMiddleware()),
                Collections.<Reducer<CounterState>>singletonList(new UpdateCounterReducer()));

        mDisposable = mStore.getObservable()
                .subscribe(new Consumer<StateChangedEventArgs<CounterState>>() {
                    @Override
                    public void accept(@NonNull StateChangedEventArgs<CounterState> args) throws Exception {
                        mLocalCount = args.getState().getCount();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mError = throwable;
                    }
                });

        mLocalCount = mStore.getState().getCount();
    }

    @Test
    public void testInc() {
        final int originalCount = mStore.getState().getCount();
        mStore.dispatch(new IncreaseCounterAction());
        Assert.assertEquals(originalCount + 1, mStore.getState().getCount());
    }


    @After
    public void after() {
        Assert.assertEquals(mLocalCount, mStore.getState().getCount());
        Assert.assertNull("mError is not null", mError);
        Assert.assertNotNull("mDisposable is null", mDisposable);
        Assert.assertFalse(mDisposable.isDisposed());
        mDisposable.dispose();
    }

}
