package com.awen.photo.photopick.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.awen.photo.R;


/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class PermissionUtil {

    public static final String TAG = "PermissionUtil";
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 100;

    public static boolean checkPermission(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int storagePermission = ActivityCompat.checkSelfPermission(activity, permission);
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public static void showPermissionDialog(final Activity activity, String permission) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            ActivityCompat.requestPermissions(activity, new String[]{permission},
                    PermissionUtil.REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        ActivityCompat.requestPermissions(activity, new String[]{permission},
                PermissionUtil.REQUEST_CODE_ASK_PERMISSIONS);
    }

    public static void showSystemSettingDialog(@NonNull final Activity activity,@NonNull String tips){
        new AlertDialog.Builder(activity)
                .setMessage(tips)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startSystemSettingActivity(activity);
                    }
                })
                .show();
    }

    public static void startSystemSettingActivity(@NonNull Activity activity){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }

}
