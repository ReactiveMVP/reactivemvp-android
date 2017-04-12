package io.github.reactivemvp.model.reducer;

import io.github.reactivemvp.model.Action;
import io.github.reactivemvp.model.Reducer;
import io.github.reactivemvp.model.State;
import io.reactivex.annotations.NonNull;

/**
 * Since Java is a strongly typed programming language, we can take advantage of
 * its generic feature to simplify the reducer implication.
 */
public abstract class GenericReducer<TState extends State, TAction extends Action>
        implements Reducer<TState> {

    /**
     * determine if this reducer should handle an action
     * @param action
     * @return
     */
    protected boolean shouldHandle(final Action action) {
        return action != null && getAcceptableActionClass().equals(action.getClass());
    }

    /**
     * Return the action type this reducer can handle
     *
     * @return
     */
    @NonNull
    protected abstract Class<TAction> getAcceptableActionClass();

    @Override
    @NonNull
    public final TState reduce(TState currentState, Action action) {
        if (!shouldHandle(action)) {
            return currentState;
        }

        //noinspection unchecked
        return doReduce(currentState, (TAction) action);
    }

    @NonNull
    protected abstract TState doReduce(TState currentState, TAction action);

}
