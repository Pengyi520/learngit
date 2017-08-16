/*
 * Copyright (C) 2005-2017 Qihoo 360 Inc.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.qihoo360.replugin.sample.host;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.py.library.network.base.BaseObserver;
import com.py.library.network.base.BaseResponse;
import com.py.library.network.http.CommonObserver;
import com.py.library.network.http.CommonResponse;
import com.py.library.network.http.RetrofitClient;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;
import com.qihoo360.replugin.sample.service.MyService;
import com.qihoo360.replugin.sample.service.params.BillParams;
import com.qihoo360.replugin.sample.service.response.BillResponse;
import com.qihoo360.replugin.sample.util.ShortcutUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * @author RePlugin Team
 */
public class MainActivity extends Activity {
  private FingerprintManagerCompat fingerprintManagerCompat;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    fingerprintManagerCompat = FingerprintManagerCompat.from(this);
    /*
     * PluginInfo pi = RePlugin.install("/sdcard/mylibrary-debug.apk");
     * if(pi != null) {
     * RePlugin.preload(pi);
     * }
     */
    /*
     * if(RePlugin.isPluginInstalled("library1")){
     * RePlugin.uninstall("library1");
     * }
     */

    /*
     * if(RePlugin.isPluginInstalled("secondmodule")){
     * RePlugin.uninstall("secondmodule");
     * }
     */


    /*
     * if(RePlugin.isPluginInstalled("thirdmodule")){
     * RePlugin.uninstall("thirdmodule");
     * }
     */
    Toast.makeText(this, RePlugin.isPluginInstalled("library1") + "", Toast.LENGTH_SHORT).show();

    findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RePlugin.startActivity(MainActivity.this,
            RePlugin.createIntent("demo1", "com.qihoo360.replugin.sample.demo1.MainActivity"));
      }
    });
    findViewById(R.id.other_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RePlugin.startActivity(MainActivity.this,
            RePlugin.createIntent("library1", "com.example.mylibrary.MyActivity"));
        // RePlugin.startActivity(MainActivity.this, RePlugin.createIntent("secondmodule",
        // "com.example.secondmodule.SecondActivity2"));
        // RePlugin.startActivity(MainActivity.this, RePlugin.createIntent("thirdmodule",
        // "com.example.thirdmodule.ThirdActivity"));
      }
    });
    findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, PluginFragmentActivity.class));
      }
    });

    /**
     * action" -> "paidBills "pageSize" -> "20" "pageIndex" -> "1" "wdId" ->
     * "523fe6d993785bec2d4cd0814a7718cf" "FFClientType" -> "2" "imei" -> "000000000000000"
     */
    findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MyService myService = RetrofitClient.getInstance(MainActivity.this).create(MyService.class);
        BillParams params = new BillParams();
        params.action = "paidBills";
        params.billType = "3";
        params.pageIndex = "1";
        params.pageSize = "20";
        params.puid ="E8F84D1AE88849DF90BBF03F2B3FE024";
        myService.postByField("paidBills","3","1","20").observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new CommonObserver<BillResponse>(getApplicationContext()) {
              @Override
              protected void getDisposable(Disposable d) {

          }

              @Override
              protected void onError(String errorMsg) {

          }

              @Override
              protected void onSuccess(BillResponse billResponse) {
                List<BillResponse.DataModel> data = billResponse.data;
                Toast.makeText(MainActivity.this, data.size() + "", Toast.LENGTH_SHORT).show();
              }
            });
      }
    });
    findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          if(fingerprintManagerCompat.isHardwareDetected() && fingerprintManagerCompat.hasEnrolledFingerprints()) {
            Toast.makeText(MainActivity.this,"本设备支持指纹识别,开始识别,手指放Home键...",Toast.LENGTH_SHORT).show();
            initFingerPrint();
          }
        //  jumpToGesturePassCheck();
        }
      }
    });
    findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Intent intent =  new Intent(MainActivity.this,SonicActivity.class);
          startActivity(intent);
      }
    });
    findViewById(R.id.btn5).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent =  new Intent();
        intent.setAction("com.pengyi.addshoutcut");
        BitmapDrawable bitmapDrawable=(BitmapDrawable)getResources().getDrawable(R.drawable.ic_launcher);
        ShortcutUtils.addShortcut(MainActivity.this,intent,"pengyi",false,bitmapDrawable.getBitmap());
      }
    });
  }
  private final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 0;
  private FingerPrintUiHelper fingerPrintUiHelper;
  private void initFingerPrint() {
    fingerPrintUiHelper = new FingerPrintUiHelper(this);
    fingerPrintUiHelper.startFingerPrintListen(new MyCallBack());
  }

            /**
             * 跳转到手势密码校验界面
             */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void jumpToGesturePassCheck() {
    KeyguardManager keyguardManager =
            (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
    Intent intent =
            keyguardManager.createConfirmDeviceCredentialIntent("finger", "测试指纹识别");
    fingerPrintUiHelper.stopsFingerPrintListen();
    startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
  }


  public class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback {
    private static final String TAG = "MyCallBack";

    // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
      Log.d(TAG, "onAuthenticationError: " + errString);
      Toast.makeText(MainActivity.this,errString,Toast.LENGTH_SHORT).show();

    }

    // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
    @Override
    public void onAuthenticationFailed() {
      Toast.makeText(MainActivity.this,"验证失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
      Toast.makeText(MainActivity.this,helpString,Toast.LENGTH_SHORT).show();

    }

    // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult
                                                  result) {
      Toast.makeText(MainActivity.this,"验证成功",Toast.LENGTH_SHORT).show();
    }
  }
}
