/*
 * Copyright (C) 2005-2017 Qihoo 360 Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.qihoo360.replugin.sample.host;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.py.library.network.http.RetrofitClient;
import com.py.library.network.intercepter.HeaderInterceptor;
import com.py.library.network.manager.BaseUrlMappingManager;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginCallbacks;
import com.qihoo360.replugin.RePluginConfig;
import com.qihoo360.replugin.RePluginEventCallbacks;
import com.qihoo360.replugin.model.PluginInfo;
import com.qihoo360.replugin.sample.service.ApiConstants;
import com.qihoo360.replugin.sample.service.intercepter.CommonParamsIntercepter;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author RePlugin Team
 */
public class SampleApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        RePluginConfig config = new RePluginConfig();
        config.setVerifySign(!BuildConfig.DEBUG);
        config.setPrintDetailLog(BuildConfig.DEBUG);
        config.setUseHostClassIfNotFound(true);
        config.setCallbacks(new RePluginCallbacks(base) {
            @Override
            public boolean onPluginNotExistsForActivity(final Context context, final String plugin, final Intent intent, int process) {
                Observable.create(new ObservableOnSubscribe<Boolean>() {

                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                        if (plugin.equals("library1")) {
                            PluginInfo pi = RePlugin.install("/sdcard/mylibrary-debug.apk");
                            if (pi != null) {
                                e.onNext(RePlugin.preload(pi));
                            } else {
                                e.onNext(false);
                            }
                            return;
                        } else if (plugin.equals("secondmodule")) {
                            PluginInfo pi = RePlugin.install("/sdcard/secondmodule-debug.apk");
                            if (pi != null) {
                                e.onNext(RePlugin.preload(pi));
                            } else {
                                e.onNext(false);
                            }
                            return;
                        } else if (plugin.equals("thirdmodule")) {
                            PluginInfo pi = RePlugin.install("/sdcard/thirdmodule-debug.apk");
                            if (pi != null) {
                                e.onNext(RePlugin.preload(pi));
                            } else {
                                e.onNext(false);
                            }
                            return;
                        }
                        e.onNext(false);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                          if(aBoolean) {
                              RePlugin.startActivity(context,intent);
                          }
                    }
                });
                return super.onPluginNotExistsForActivity(context, plugin, intent, process);
            }

            @Override
            public boolean onLoadLargePluginForActivity(Context context, String plugin, Intent intent, int process) {
                return super.onLoadLargePluginForActivity(context, plugin, intent, process);
            }
        });
        config.setEventCallbacks(new RePluginEventCallbacks(base) {
            @Override
            public void onInstallPluginFailed(String path, InstallResult code) {
                super.onInstallPluginFailed(path, code);
            }
        });
        // ======= REPLUGIN =======
        RePlugin.App.attachBaseContext(this, config);
        // ========================

    }

    @Override
    public void onCreate() {
        super.onCreate();

        // ======= REPLUGIN =======
        RePlugin.App.onCreate();
        initRetrofit(getApplicationContext());
        // ========================
    }

    public void initRetrofit(Context context) {
        BaseUrlMappingManager.getInstance().putDomain("service1", "https://www.pengyi.com");
        RetrofitClient.RetrofitConfiguration configuration = new RetrofitClient.RetrofitConfiguration()
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new CommonParamsIntercepter())
                .baseUrl(ApiConstants.BASE_URL);
        RetrofitClient.initRetrofit(context,configuration);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        // ======= REPLUGIN =======
        RePlugin.App.onLowMemory();
        // ========================
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        // ======= REPLUGIN =======
        RePlugin.App.onTrimMemory(level);
        // ========================
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // ======= REPLUGIN =======
        RePlugin.App.onConfigurationChanged(newConfig);
        // ========================
    }
}
