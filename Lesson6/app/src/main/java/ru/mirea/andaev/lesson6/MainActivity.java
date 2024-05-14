package ru.mirea.andaev.lesson6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.andaev.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    static private final String group_number = "04";
    static private final String student_number = "2";
    static private final String film_title = "Dune(2021)";
    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    @Override
    protected void onStart() {
        super.onStart();
        final SharedPreferences settings = getSharedPreferences("app.settings", Context.MODE_PRIVATE);
        binding.groupNumberInput.setText(String.valueOf(settings.getInt(group_number, 0)));
        binding.studentNumberInput.setText(String.valueOf(settings.getInt(student_number, 0)));
        binding.filmTitleInput.setText(settings.getString(film_title, ""));
        binding.button.setOnClickListener(v ->{
            SharedPreferences.Editor editor = settings.edit();
            try {
                int group_number = Integer.parseInt(binding.groupNumberInput.getText().toString());
                int student_number = Integer.parseInt(binding.studentNumberInput.getText().toString());
                String film_title = binding.filmTitleInput.getText().toString();
                editor.putInt(MainActivity.group_number, group_number);
                editor.putInt(MainActivity.student_number, student_number);
                editor.putString(MainActivity.film_title, film_title);
                editor.apply();
                Toast.makeText(this, String.format("Succes saving %d, %d, %s",
                        group_number, student_number, film_title), Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, String.format("Error: %s", e.toString()), Toast.LENGTH_LONG).show();
            }
        });
    }
}