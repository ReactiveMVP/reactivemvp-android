package io.github.reactivemvp.model.middleware;

import io.github.reactivemvp.model.Action;
import io.github.reactivemvp.model.action.IncreaseCounterAction;
import io.github.reactivemvp.model.action.UpdateCounterAction;
import io.reactivex.Observable;

/**
 * Created by ruoshili on 4/12/2017.
 */

public final class IncreaseCounterMiddleware extends GenericMiddleware<IncreaseCounterAction> {
    @Override
    protected Class<IncreaseCounterAction> getAcceptableActionClass() {
        return IncreaseCounterAction.class;
    }

    @Override
    protected Observable<? extends Action> doProcess(IncreaseCounterAction action) {
        return Observable.just(new UpdateCounterAction(UpdateCounterAction.Operation.Increase));
    }
}
