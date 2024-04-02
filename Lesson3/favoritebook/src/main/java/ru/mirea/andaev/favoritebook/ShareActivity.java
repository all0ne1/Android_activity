package ru.mirea.andaev.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class ShareActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extra = getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        if (extra != null){
            TextView textView = findViewById(R.id.textView2);
            String bookName = extra.getString(MainActivity.KEY);
            textView.setText(String.format("Моя любимая книга %s",bookName));
        }
    }

    public void onReturnClick(View view){
        EditText editText = findViewById(R.id.editTextText);
        Intent data = new Intent();
        data.putExtra(MainActivity.USER_MESSAGE, editText.getText().toString());
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}