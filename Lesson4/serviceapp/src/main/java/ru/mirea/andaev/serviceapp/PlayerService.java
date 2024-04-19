package ru.mirea.andaev.serviceapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PlayerService extends Service {
    MediaPlayer mPLayer;
    static String CHANNEL_ID = "ForegroundServiceChannel";
    static public String TITLE = "MusicTitle";
    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText("Playing....")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(TITLE))
                .setContentTitle("Music Player");
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Student Andaev A.S. Notification", importance);

        channel.setDescription("MIREA Channel");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(channel);
        startForeground(1, builder.build());
        mPLayer = MediaPlayer.create(this,R.raw.music);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPLayer.stop();
        Log.d(PlayerService.class.getSimpleName(),"Музыка остановилась");
        mPLayer = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPLayer.start();
        Log.d(PlayerService.class.getSimpleName(),"Музыка играет");
        mPLayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopForeground(true);
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }
}