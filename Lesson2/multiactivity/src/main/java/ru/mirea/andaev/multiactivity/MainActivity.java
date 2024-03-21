package ru.mirea.andaev.multiactivity;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
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
    public void onClickMainActivity(View view){
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("key","MIREA");
        startActivity(intent);
    }
    public void onSend(View view){
        EditText toSend = findViewById(R.id.SendThis);
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("key", toSend.getText().toString());
        startActivity(intent);
    }
}