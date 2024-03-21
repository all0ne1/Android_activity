package ru.mirea.andaev.multiactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this,"Activity started", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        Toast.makeText(this,"Activity on pause", Toast.LENGTH_SHORT).show();
        super.onPause();
    }
    @Override
    protected void onStop() {
        Toast.makeText(this,"Activity stopped", Toast.LENGTH_SHORT).show();
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        Toast.makeText(this,"Activity destroyed(", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        String text = (String) getIntent().getSerializableExtra("key");
        TextView textView = findViewById(R.id.mainview);
        textView.setText(text);
    }
}