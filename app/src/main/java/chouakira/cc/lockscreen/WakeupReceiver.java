package chouakira.cc.lockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.provider.Settings;

/**
 * Created by rsa on 2017/12/10.
 */

public class WakeupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean fingerprint = false;
        FingerprintManager mFP = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        if(context.checkSelfPermission("android.permission.USE_FINGERPRINT") == PackageManager.PERMISSION_GRANTED
                && mFP.isHardwareDetected()
                && mFP.hasEnrolledFingerprints()
                ) {
            fingerprint = true;
        }
        if(fingerprint && Settings.System.canWrite(context)) {
            SharedPreferences sp = context.getSharedPreferences("system_setting", 0);
            int timeout = sp.getInt(Settings.System.SCREEN_OFF_TIMEOUT, 1000 * 15);

            int timeoutNow = 0;
            try {
                timeoutNow = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
            } catch(Settings.SettingNotFoundException ex) {
                ex.printStackTrace();
            }

            if(timeoutNow == 10) {
                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, timeout);
            }
        }

    }
}
