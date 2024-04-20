package ru.mirea.andaev.mireaproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PlayerService extends Service {
    static String CHANNEL_ID = "ForegroundServiceChannel";
    MediaPlayer mediaPlayer;
    static public String TITLE = "MusicTitle";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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
        mediaPlayer = MediaPlayer.create(this,R.raw.music);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            mediaPlayer.start();
            Log.d(PlayerService.class.getSimpleName(),"Музыка играет");
        } catch (Exception e) {
            Log.e(PlayerService.class.getSimpleName(), "Ошибка при проигрывании музыки", e);
        }
        mediaPlayer.setOnCompletionListener(mp ->{
            stopForeground(true);
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        Log.d(PlayerService.class.getSimpleName(),"Музыка остановилась");
        mediaPlayer = null;
    }

}
