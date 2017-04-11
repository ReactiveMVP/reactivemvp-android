package io.github.reactivemvp.model;

import java.util.List;

/**
 * Created by ruoshi on 4/11/17.
 */

public interface StateChangedListener<TState extends State> {
    void onStateChanged(StateChangedEventArgs<TState> eventArgs);
    List<Class<? extends Action>> getInterestedActionTypes();
}
