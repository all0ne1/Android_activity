package ru.mirea.andaev.serviceapp;

import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.andaev.serviceapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    static private final int PermissionCode = 200;
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        boolean notification_permission = ContextCompat.checkSelfPermission(this,FOREGROUND_SERVICE)
                == PackageManager.PERMISSION_GRANTED;
        boolean foreground_service_permission = ContextCompat.checkSelfPermission(this,POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;
        String message = (notification_permission && foreground_service_permission)
                ? "Привилегии получены" : "В привилегиях отказано";
        Log.d(MainActivity.class.getSimpleName(), message);
        if (!(foreground_service_permission && notification_permission)){
            ActivityCompat.requestPermissions(this, new String[]{FOREGROUND_SERVICE,POST_NOTIFICATIONS},PermissionCode);
        }
        binding.playButton.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(MainActivity.this, PlayerService.class);
            serviceIntent.putExtra(PlayerService.TITLE,"RickRoll");
            ContextCompat.startForegroundService(MainActivity.this,serviceIntent);
        });
        binding.stopButton.setOnClickListener(v ->{
            stopService(new Intent(MainActivity.this,PlayerService.class));
        });
    }
}