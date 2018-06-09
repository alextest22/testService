package com.test.testservices.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.test.testservices.R;
import com.test.testservices.utill.base.PhoneCallReceiver;
import com.test.testservices.utill.database.DbHelper;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class CallService extends Service {
    private String TAG = this.getClass().getName();
    public static final String BROADCAST_PHONE_STATE = "android.intent.action.PHONE_STATE";
    public static final String BROADCAST_CALL = "android.intent.action.NEW_OUTGOING_CALL";
    private MediaRecorder mRecorder = new MediaRecorder();
    private boolean mRecordStarted;
    private long mTimeCallStart = 0;

    public CallService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Служба создана",
                Toast.LENGTH_SHORT).show();
        initCallReciver();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initCallReciver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_PHONE_STATE);
        intentFilter.addAction(BROADCAST_CALL);
        registerReceiver(new CallReceiver(), intentFilter);
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "Служба остановлена",
                Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    private class CallReceiver extends PhoneCallReceiver {

        @Override
        protected void onIncomingCallReceived(Context ctx, String number, Date start)
        {
            Log.e(TAG,"onIncomingCallReceived");
        }

        @Override
        protected void onIncomingCallAnswered(Context ctx, String number, Date start)
        {   saveFile();
            Toast.makeText(ctx, "Входящий звонок записываеться ",
                    Toast.LENGTH_SHORT).show();
            mTimeCallStart = Calendar.getInstance().getTimeInMillis();
        }

        @Override
        protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
        {
            stopWriteCall();
            Toast.makeText(ctx, "Входящий звонок записался ",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onOutgoingCallStarted(Context ctx, String number, Date start)
        {   saveFile();
            Toast.makeText(ctx, "Исходящий звонок записываеться",
                    Toast.LENGTH_SHORT).show();
            mTimeCallStart = Calendar.getInstance().getTimeInMillis();

        }

        @Override
        protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end)
        {
            stopWriteCall();
            Toast.makeText(ctx, "Исходящий звонок записался",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onMissedCall(Context ctx, String number, Date start)
        {
            Log.e(TAG,"onMissedCall");

        }

    }


    private void saveFile(){
        try {
            File sampleDir = Environment.getExternalStorageDirectory();
            File audiofile;
            try {
                audiofile = File.createTempFile(String.format(getString(R.string.name_prefix),System.currentTimeMillis()),
                        ".mp3", sampleDir);

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setOutputFile(audiofile.getAbsolutePath());
            mRecorder.prepare();
            mRecorder.start();
            mRecordStarted = true;

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }



    }

    private void stopWriteCall(){
        if (mRecordStarted) {
            mRecorder.stop();
            mRecordStarted = false;
            DbHelper.addCall(Calendar.getInstance().getTimeInMillis() - mTimeCallStart);

        }
    }
}
