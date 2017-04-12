package io.github.reactivemvp.model.action;

import io.github.reactivemvp.model.Action;

/**
 * Created by ruoshili on 4/12/2017.
 */

public final class UpdateCounterAction implements Action {
    @Override
    public String getActionTypeName() {
        return "io.github.reactivemvp.UpdateCounterAction";
    }

    private final Operation mOperation;

    public UpdateCounterAction(Operation operation) {
        mOperation = operation;
    }

    public enum Operation {
        Increase,
        Decrease
    }

    public Operation getOperation() {
        return mOperation;
    }
}
