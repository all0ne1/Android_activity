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
        Log.d(MainActivity.class.getSimpleName(),"Stack: " + Arrays.toString(mainThread.getStackTrace()));
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int days = Integer.parseInt(binding.inputDays.getText().toString());
                final int lessons = Integer.parseInt(binding.inputLessons.getText().toString());
                long endTime = System.currentTimeMillis() + 20*1000;
                Thread background_worker = new Thread(){
                    @Override
                    public void run(){
                        while (System.currentTimeMillis() < endTime){
                            synchronized (this){
                                try{
                                    wait(endTime - System.currentTimeMillis());


                                }
                                catch (Exception e){
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                };



            }
        });

    }
}