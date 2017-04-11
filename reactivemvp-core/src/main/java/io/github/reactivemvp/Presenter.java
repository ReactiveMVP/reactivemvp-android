package io.github.reactivemvp;

/**
 * Created by ruoshi on 3/30/17.
 */

public interface Presenter<TView extends View> {

    void attach(TView view);
    void detach();

}
