package com.test.testservices.utill;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.test.testservices.R;

import java.util.List;

import io.reactivex.functions.Consumer;


public class PermissionHelper {



    public static void checkDeviceDataPermission(final Activity activity, Consumer<Boolean> permissionsGranted) {
        checkPermission(activity, permissionsGranted, Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,
               Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private static void checkPermission(final Activity activity, Consumer<Boolean> permissionsGranted,
                                        String... permissions) {
        Dexter.withActivity(activity)
                .withPermissions(permissions)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) callConsumer(permissionsGranted, true);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener((DexterError error) -> Log.e("DexterError: " ,  error.name())).onSameThread().check();
    }

    private static void callConsumer(Consumer<Boolean> permissionsGranted, boolean isGranted) {
        if (permissionsGranted != null) {
            try {
                permissionsGranted.accept(isGranted);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
