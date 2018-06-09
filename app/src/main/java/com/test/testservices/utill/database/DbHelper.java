package com.test.testservices.utill.database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.test.testservices.services.CallService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class DbHelper {
    private static final String TAG = "DataBase";
    private static final Locale LOCALE_RU = new Locale("ru");
    private static final String FORMAT_DD_MMMM_YYYY = "dd-MMMM-yyyy";
    private static final String MASK_TIME_CALL =   "%1$d:%2$02d";


    public static void addCall(long range){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> call = new HashMap<>();
        call.put("date", getCurrentDate());
        call.put("timeRange", getTimeRange(range));
        db.collection("testService")
                .add(call)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    private static String getTimeRange(Long range){
        long sec = (range/1000)  % 60;
        long min = (range/1000) / 60;
        return String.format(LOCALE_RU, MASK_TIME_CALL, min, sec);
    }

    private static String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DD_MMMM_YYYY, LOCALE_RU);
        return dateFormat.format(Calendar.getInstance().getTime());
    }
}
