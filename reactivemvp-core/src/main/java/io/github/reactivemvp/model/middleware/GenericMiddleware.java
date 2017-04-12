package io.github.reactivemvp.model.middleware;

import io.github.reactivemvp.model.Action;
import io.github.reactivemvp.model.Middleware;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by ruoshili on 4/12/2017.
 */

public abstract class GenericMiddleware<TAction extends Action> implements Middleware {
    /**
     * determine if this middleware should handle an action
     *
     * @param action
     * @return
     */
    protected boolean shouldHandle(final Action action) {
        return action != null && getAcceptableActionClass().equals(action.getClass());
    }

    @Override
    public final Observable<? extends Action> process(Action action) {
        if (shouldHandle(action)) {
            //noinspection unchecked
            return doProcess((TAction) action);
        }
        return Observable.empty();
    }

    /**
     * 返回该Reducer可以处理的Action类型
     *
     * @return 可以处理的Action类型，不然不为null
     */
    @NonNull
    protected abstract Class<TAction> getAcceptableActionClass();


    protected abstract Observable<? extends Action> doProcess(TAction action);
}
