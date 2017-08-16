package com.py.library.network.intercepter;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pengyi on 2017/7/21.
 */

public class HeaderInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder .header("AppType", "TPOS")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        return chain.proceed(builder.build());
    }
}
