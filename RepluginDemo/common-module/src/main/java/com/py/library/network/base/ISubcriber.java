package com.py.library.network.base;

import io.reactivex.disposables.Disposable;

/**
 * Created by pengyi on 2017/7/21.
 */

public interface ISubcriber<T extends BaseResponse> {
    void doOnSubscribe(Disposable d);

    void doOnError(String errorMsg);

    void doOnNext(T t);

    void doOnCompleted();
}
