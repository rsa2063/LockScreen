package chouakira.cc.lockscreen;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

public class MainActivity extends Activity {

    DevicePolicyManager mDPM;
    ComponentName comName;
    FingerprintManager mFP;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lockScreen();

//        mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
//        comName = new ComponentName(this, MyAdminReceiver.class);
//        mFP = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

//        if(checkSelfPermission("android.permission.USE_FINGERPRINT") == PackageManager.PERMISSION_GRANTED
//                && mFP.isHardwareDetected()
//                && mFP.hasEnrolledFingerprints()
//                ) {
//            // https://yanlu.me/android-m6-0-permission-chasm/
//            if(!Settings.System.canWrite(this)) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
//                        Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, 1);
//            }
//            sp = getSharedPreferences("system_setting", 0);
//            SharedPreferences.Editor ed = sp.edit();
//            try {
//                int timeout = Settings.System.getInt(this.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
//                if(timeout != 10) {
//                    ed.putInt(Settings.System.SCREEN_OFF_TIMEOUT, timeout);
//                    ed.commit();
//                }
//            } catch(Settings.SettingNotFoundException ex) {
//                ex.printStackTrace();
//                ed.putInt(Settings.System.SCREEN_OFF_TIMEOUT, 1000 * 15);
//            }
//
//            Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 10);
//        } else {
//            // https://developer.android.com/guide/topics/admin/device-admin.html
//            if(mDPM.isAdminActive(comName)) {
//                mDPM.lockNow();
//            } else {
//                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, comName);
//                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
//                        this.getString(R.string.message));
//                startActivity(intent);
//            }
//        }

        finish();
    }

    public void lockScreen() {
        mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        comName = new ComponentName(this, MyAdminReceiver.class);
        mFP = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        if(checkSelfPermission("android.permission.USE_FINGERPRINT") == PackageManager.PERMISSION_GRANTED
                && mFP.isHardwareDetected()
                && mFP.hasEnrolledFingerprints()
                ) {
            // https://yanlu.me/android-m6-0-permission-chasm/
            if(!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
                return;
            }
            sp = getSharedPreferences("system_setting", 0);
            SharedPreferences.Editor ed = sp.edit();
            try {
                int timeout = Settings.System.getInt(this.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
                if(timeout != 10) {
                    ed.putInt(Settings.System.SCREEN_OFF_TIMEOUT, timeout);
                    ed.commit();
                }
            } catch(Settings.SettingNotFoundException ex) {
                ex.printStackTrace();
                ed.putInt(Settings.System.SCREEN_OFF_TIMEOUT, 1000 * 15);
            }

            Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 10);
        } else {
            // https://developer.android.com/guide/topics/admin/device-admin.html
            if(mDPM.isAdminActive(comName)) {
                mDPM.lockNow();
            } else {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, comName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        this.getString(R.string.sample_device_admin_description));
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK) {
            lockScreen();
        }
    }
}
