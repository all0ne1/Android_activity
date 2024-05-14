package ru.mirea.andaev.internalfilestorage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.mirea.andaev.internalfilestorage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String filname = "sometext.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            binding.textToFile.post(() -> binding.textToFile.setText(getTextFromFile()));
        }).start();

        binding.saveButton.setOnClickListener(v -> {
            String data = binding.textToFile.getText().toString();
            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(filname, Context.MODE_PRIVATE);
                outputStream.write(data.getBytes());
                outputStream.close();
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        setContentView(binding.getRoot());
    }

    public String getTextFromFile() {
        try {
            FileInputStream fin;
            fin = openFileInput(filname);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            Log.d(this.getLocalClassName(), text);
            return text;
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}