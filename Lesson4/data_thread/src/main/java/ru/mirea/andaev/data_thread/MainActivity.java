package ru.mirea.andaev.data_thread;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import ru.mirea.andaev.data_thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final Runnable runn1 = new Runnable() {
            public void run() {
                binding.tvInfo.setText("runn1");
            }
        };
        final Runnable runn2 = new Runnable() {
            public void run() {
                binding.tvInfo.setText("runn2");
            }
        };
        final Runnable runn3 = new Runnable() {
            public void run() {
                binding.tvInfo.setText("runn3");
            }
        };
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    runOnUiThread(runn1);
                    TimeUnit.SECONDS.sleep(1);
                    binding.tvInfo.postDelayed(runn3, 2000);
                    binding.tvInfo.post(runn2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        TextView tv = binding.textView;
        tv.setText("Различия между методами и последовательность запуска:\n");
        tv.append("runOnUiThread:\n");
        tv.append("Выполняет код в главном потоке\n");
        tv.append("Можно взаимодействовать с интерфейсом из фонового потока\n");
        tv.append("postDelayed:\n");
        tv.append("Добавляет код в очередь выполнения с задержкой в n миллисекунд\n");
        tv.append("Можно выполнять асинхронные операции, не блокируя главный поток\n");
        tv.append("post:\n");
        tv.append("Добавляет код в очередь выполнения\n");
        tv.append("Код будет выполнен после выполнения существующей очереди операций\n");
        tv.append("Позволяет выполнить асинхронные операции в главном потоке без задержки");



    }
}