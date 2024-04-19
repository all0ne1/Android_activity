package ru.mirea.andaev.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class Worker extends androidx.work.Worker {
    static final String TAG = "Worker";
    public Worker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: start");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "doWork: end");
        return Result.success();
    }
}
