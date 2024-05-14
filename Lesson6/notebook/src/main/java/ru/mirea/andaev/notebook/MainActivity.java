package ru.mirea.andaev.notebook;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.andaev.notebook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding = null;
    File filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.loadButton.setOnClickListener(v -> {
            try {
                File file = new File(filepath, binding.editFileName.getText().toString());
                FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                List<String> lines = new ArrayList<>();
                String line = reader.readLine();
                while (line != null) {
                    lines.add(line);
                    line = reader.readLine();
                }
                reader.close();
                binding.editQuote.setText(String.join("\n", lines));
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, String.format("Error: %s", e.toString()), Toast.LENGTH_SHORT).show();
            }
        });
        binding.saveButton.setOnClickListener(v -> {
            try {
                File file = new File(filepath, binding.editFileName.getText().toString());
                FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsoluteFile());
                OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
                output.write(binding.editQuote.getText().toString());
                output.close();

                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, String.format("Error: %s", e.toString()), Toast.LENGTH_SHORT).show();
            }
        });
    }

}