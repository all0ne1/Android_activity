package ru.mirea.andaev.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.andaev.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d(MainActivity.class.getSimpleName(), "Task execute. This is result: " + msg.getData().getString("result"));
            }
        };
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int age = Integer.parseInt(binding.editAge.getText().toString());
                    String job = binding.editJob.getText().toString();
                    if (age > -1){
                        long delay = age * 1000;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Thread.sleep(delay);
                                    Message msg = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("result", "Вы работаете на позиции: " + job + " " + age + " лет");
                                    msg.setData(bundle);
                                    mainThreadHandler.sendMessage(msg);
                                }
                                catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                else{
                        Toast.makeText(binding.getRoot().getContext(), "Введите корректный возраст",Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e){
                    Log.d(MainActivity.class.getSimpleName(),"Некорректный ввод");
                }

            }
        });

    }
}