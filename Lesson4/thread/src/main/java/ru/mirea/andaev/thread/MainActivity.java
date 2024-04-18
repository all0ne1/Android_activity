package ru.mirea.andaev.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import ru.mirea.andaev.thread.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView tv = binding.textView;
        Thread mainThread = Thread.currentThread();
        tv.setText("Имя текущего потока " + mainThread.getName());
        mainThread.setName("МОЙ НОМЕР ГРУППЫ: 04, НОМЕР ПО СПИСКУ: 2, МОЙ ЛЮБИИМЫЙ ФИЛЬМ: Дюна");
        tv.append("\nНовое имя потока " + mainThread.getName());
        Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread background_worker = new Thread() {
                    @Override
                    public void run() {
                        try{
                            final int days = Integer.parseInt(binding.inputDays.getText().toString());
                            final int lessons = Integer.parseInt(binding.inputLessons.getText().toString());
                            long endTime = System.currentTimeMillis() + 20 * 1000;
                            while (System.currentTimeMillis() < endTime) {
                                synchronized (this) {
                                    float res = 0;
                                    try {
                                        Log.i(MainActivity.class.getSimpleName(), "Начинаю фоновое вычисление");
                                        wait(endTime - System.currentTimeMillis());
                                        res = (float) lessons / days;
                                        Log.i(MainActivity.class.getSimpleName(), "Закончил фоновое вычисление");
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    } finally {
                                        binding.calculationRes.setText("Серднее кол-во пар " + res);
                                    }
                                }
                            }
                        }
                        catch (NumberFormatException e){
                            binding.calculationRes.setText("Неверный ввод");
                        }

                    }
                };
                background_worker.start();

            }
        });
        binding.AnotherThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        int numberThread = counter++;
                        Log.d("ThreadProject", String.format("Запущен поток No %d студентом группы No %s номер по " +
                                "списку No %d ", numberThread, "БСБО-04-22", 2));
                        long endTime = System.currentTimeMillis() + 20 * 1000;
                        while (System.currentTimeMillis() < endTime) {
                            synchronized (this) {
                                try {
                                    wait(endTime - System.currentTimeMillis());
                                    Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            Log.d("ThreadProject", "Выполнен поток No " + numberThread);
                        }
                    }
                }).start();
            }
        });
    }
}
