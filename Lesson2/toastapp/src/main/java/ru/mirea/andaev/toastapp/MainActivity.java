package ru.mirea.andaev.toastapp;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onCountClick(View view){
        EditText editText = findViewById(R.id.editText);
        Toast toast = Toast.makeText(getApplicationContext(),"СТУДЕНТ № 2 ГРУППА БСБО-04-22 Количество символов" +
                editText.length(),Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,0);
        toast.show();

    }
}