package ru.mirea.andaev.workmanager;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import ru.mirea.andaev.workmanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(Worker.class)
                        .build();
        WorkManager
                .getInstance(this)
                .enqueue(uploadWorkRequest);
        binding.workButton.setOnClickListener(v ->{
            Constraints constraints = new Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build();
            WorkRequest workRequest = new OneTimeWorkRequest.Builder(Worker.class)
                    .setConstraints(constraints)
                    .build();
            WorkManager.getInstance(this).enqueue(workRequest);
            Log.d("MainActivity", "Button clicked, enqueuing work");
        });
    }
}