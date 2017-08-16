package com.py.library.network.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.py.library.network.manager.BaseUrlMappingManager;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pengyi on 2017/7/21.
 */

public class RetrofitClient {
  private static final int DEFAULT_TIMEOUT = 20;
  private static String baseUrl = "https://www.baidu.com/";
  private static Context mContext;
  private File httpCacheDirectory;
  private Cache cache;
  private OkHttpClient okHttpClient;
  private Retrofit retrofit;
  private RetrofitConfiguration retrofitConfiguration;

  public static class SingleHolder {
    private static RetrofitClient INSTANCE = new RetrofitClient(mContext);
  }

  private RetrofitClient(Context context) {
    if (httpCacheDirectory == null) {
      httpCacheDirectory = new File(mContext.getCacheDir(), "tamic_cache");
    }

    try {
      if (cache == null) {
        cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
      }
    } catch (Exception e) {
      Log.e("OKHttp", "Could not create http cache", e);
    }
  }

  public static RetrofitClient getInstance(Context context) {
    if (context != null) {
      mContext = context;
    }
    if (SingleHolder.INSTANCE.retrofitConfiguration == null) {
      throw new IllegalArgumentException("retrofitConfiguration should be init!");
    }
    return SingleHolder.INSTANCE;
  }

  public <T> T create(final Class<T> service) {
    if (service == null) {
      throw new RuntimeException("Api service is null!");
    }
    return retrofit.create(service);
  }

  public static RetrofitClient initRetrofit(Context context,
      RetrofitConfiguration retrofitConfiguration) {
    mContext = context;
    RetrofitClient retrofitClient = SingleHolder.INSTANCE;
    retrofitClient.retrofitConfiguration = retrofitConfiguration;
    BaseUrlMappingManager.getInstance().with(retrofitConfiguration.okClientBuilder);
    retrofitClient.okHttpClient = retrofitConfiguration.okClientBuilder
        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .retryOnConnectionFailure(true)
        .cache(retrofitClient.cache)
        .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
        .build();
    retrofitClient.retrofit = retrofitConfiguration.retrofitBuilder
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(retrofitClient.okHttpClient)
        .build();
    return retrofitClient;
  }

  public static class RetrofitConfiguration {
    private OkHttpClient.Builder okClientBuilder;
    private Retrofit.Builder retrofitBuilder;

    public RetrofitConfiguration() {
      okClientBuilder = new OkHttpClient.Builder();
      retrofitBuilder = new Retrofit.Builder();
    }

    public RetrofitConfiguration addInterceptor(Interceptor interceptor) {
      okClientBuilder.addInterceptor(interceptor);
      return this;
    }

    public RetrofitConfiguration baseUrl(String baseUrl) {
      retrofitBuilder.baseUrl(baseUrl);
      return this;
    }
  }

}
