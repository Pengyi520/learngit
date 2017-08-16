package com.qihoo360.replugin.sample.host;

import android.app.Activity;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

/**
 * Created by pengyi on 2017/8/9.
 */

public class FingerPrintUiHelper {
    private CancellationSignal signal;
    private FingerprintManagerCompat fingerprintManager;


    public FingerPrintUiHelper(Activity activity) {
        signal = new CancellationSignal();
        fingerprintManager = FingerprintManagerCompat.from(activity);
    }

    public void startFingerPrintListen(FingerprintManagerCompat.AuthenticationCallback callback) {
        fingerprintManager.authenticate(null, 0, signal, callback, null);
    }

    public void stopsFingerPrintListen() {
        signal.cancel();
        signal = null;
    }
}
