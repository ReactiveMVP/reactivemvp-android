package io.github.reactivemvp.model;

import io.reactivex.annotations.NonNull;

/**
 * Created by ruoshi on 3/31/17.
 */

public interface Reducer<TState extends State, TAction extends Action> {

    /**
     * Return the action type this reducer can handle
     * @return
     */
    @NonNull
    Class<TAction> getAcceptableActionClass();



    /**
     * A function that returns the next state tree, given
     * the current state tree and the action to handle.
     * @param currentState
     * @param action
     * @return
     */
    TState reduce(TState currentState, TAction action);
}
