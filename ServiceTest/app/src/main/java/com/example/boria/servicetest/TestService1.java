package com.example.boria.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Boria on 2015/11/19.
 */
public class TestService1 extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("onBind方法被调用", "onBind方法被调用!");
        return null;
    }


    @Override
    public void onCreate() {
        Log.i("onCreate方法被调用!", "onCreate方法被调用!");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("onStartCommand方法被调用!", "onStartCommand方法被调用!");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("onDestory方法被调用!", "onDestory方法被调用!");
        super.onDestroy();
    }
}
