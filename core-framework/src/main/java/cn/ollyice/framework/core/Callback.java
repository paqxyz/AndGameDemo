package cn.ollyice.framework.core;

/**
 * Created by ollyice on 2018/6/19.
 */

public interface Callback<T> {
    void onNext(T result);
}
