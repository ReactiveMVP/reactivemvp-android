package io.github.reactivemvp.statecontainer.reducer;

import io.github.reactivemvp.statecontainer.CounterState;
import io.github.reactivemvp.statecontainer.action.UpdateCounterAction;

/**
 * Created by ruoshili on 4/12/2017.
 */

public final class UpdateCounterReducer extends GenericReducer<CounterState, UpdateCounterAction> {
    @Override
    protected Class<UpdateCounterAction> getAcceptableActionClass() {
        return UpdateCounterAction.class;
    }

    @Override
    protected CounterState doReduce(CounterState currentState, UpdateCounterAction action) {
        CounterState.Builder builder = new CounterState.Builder(currentState);
        switch (action.getOperation()) {
            case Increase:
                builder.setCount(currentState.getCount() + 1);
                break;
            case Decrease:
                builder.setCount(currentState.getCount() - 1);
                break;
            default:
                return currentState;
        }
        return builder.build();
    }

}
