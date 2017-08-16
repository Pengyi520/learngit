package com.py.library.network.exception;

import android.text.TextUtils;

/**
 * Created by pengyi on 2017/7/27.
 */

public class InvalidUrlException extends RuntimeException {

    public InvalidUrlException(String url) {
        super("You've configured an invalid url : " + (TextUtils.isEmpty(url) ? "EMPTY_OR_NULL_URL" : url));
    }
}
