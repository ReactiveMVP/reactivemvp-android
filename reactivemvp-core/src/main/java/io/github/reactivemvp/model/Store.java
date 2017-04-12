package io.github.reactivemvp.model;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by ruoshi on 3/31/17.
 */

public interface Store<TState extends State> {

    TState getState();

    /**
     * Dispatches an action. This is the only way to trigger a state change.
     *
     * @param action  A plain object describing the change that makes sense for your application.
     * @return The dispatched action
     */
    <TAction extends Action> TAction dispatch(@NonNull TAction action);

    /**
     * 订阅状态变化
     *
     * @param stateChangedListener 监听器
     * @return
     */
    Disposable subscribe(@NonNull StateChangedListener<TState> stateChangedListener);

    /**
     * 获取状态变化的被观察者，用于更加灵活的订阅状态变化
     *
     * @return
     */
    Observable<StateChangedEventArgs<TState>> getObservable();

}
