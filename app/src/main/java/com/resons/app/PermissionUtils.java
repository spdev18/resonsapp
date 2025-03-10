// PermissionUtils.java

package com.resons.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class PermissionUtils {

    public static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    public static final int REQUEST_DND_PERMISSION = 201;

    public static void requestPermissionAlert(final Context context, final String permission, final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Permission Required")
                .setMessage("This app needs permission to function properly.")
                .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((FragmentActivity) context, new String[]{permission}, requestCode);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    public static void requestDNDPermissionAlert(final Context context, final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Permission Required")
                .setMessage("This app needs permission to function properly. Please enable it in the settings.")
                .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        ((FragmentActivity) context).startActivityForResult(intent, requestCode);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    public static boolean checkPermission(final Context context, final String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
