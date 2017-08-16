package com.py.library.network.base;

/**
 * Created by pengyi on 2017/7/21.
 */

public class BaseResponse<T> {
    public String code;
    public String msg;
    public T data;
}
