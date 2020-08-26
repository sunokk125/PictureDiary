package com.cookandroid.teamproject2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    MediaPlayer mp;

    @Nullable

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }


    @Override
    public void onCreate(){
        android.util.Log.i("서비스 테스트", "onCreate()");
        super.onCreate();
        mp = MediaPlayer.create(this, R.raw.song1);
        mp.setLooping(true);
    }

    @Override
    public void onDestroy() {
        android.util.Log.i("서비스 테스트", "onDestroy()");
        mp.stop();
        super.onDestroy();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        android.util.Log.i("서비스 테스트", "onStartCommand()");
        mp.start();
        return super.onStartCommand(intent, flags, startId);
    }

}
