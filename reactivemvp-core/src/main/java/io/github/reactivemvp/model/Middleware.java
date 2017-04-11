package io.github.reactivemvp.model;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by ruoshi on 4/11/17.
 */

public interface Middleware<TAction extends Action> {
    /**
     * 返回该Reducer可以处理的Action类型
     *
     * @return 可以处理的Action类型，不然不为null
     */
    @NonNull
    Class<TAction> getAcceptableActionClass();

    /**
     * 如果action是有副作用的，在这里进行处理，
     * 然后生成一个或者多个新的action再次投递到store
     *
     * @param action
     * @return action的执行结果
     */
    @NonNull
    Observable<? extends Action> process(TAction action);
}
