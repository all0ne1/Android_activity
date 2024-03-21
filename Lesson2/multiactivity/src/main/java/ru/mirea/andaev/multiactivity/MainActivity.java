package ru.mirea.andaev.multiactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onClickMainActivity(View view){
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("key","MIREA");
        startActivity(intent);
    }
    public void onSend(View view){
        EditText toSend = findViewById(R.id.SendThis);
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("key", toSend.getText());
        startActivity(intent);
    }
}