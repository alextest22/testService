package com.test.testservices;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.View;

import com.test.testservices.services.CallService;
import com.test.testservices.utill.PermissionHelper;
import com.test.testservices.utill.database.DbHelper;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
    private Unbinder mUnbinder;
    private MediaRecorder recorder = new MediaRecorder();
    private boolean recordStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @OnClick(R.id.btn_start)
    protected void startClick() {
        PermissionHelper.checkDeviceDataPermission(this, isGranted ->
                runOnUiThread(()-> startService(new Intent(MainActivity.this, CallService.class))));
    }

    @OnClick(R.id.btn_stop)
    protected void stopClick() {
        stopService(new Intent(MainActivity.this, CallService.class));

    }

}
