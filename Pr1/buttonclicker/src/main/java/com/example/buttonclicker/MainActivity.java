package com.example.buttonclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textViewStudent;
    Button btnWhoAmI;
    Button btnItIsNotMe;
    CheckBox itsMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStudent = findViewById(R.id.textViewStudent);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        btnItIsNotMe = findViewById(R.id.btnItIsNotMe);
        itsMe = findViewById(R.id.itsMe);

        // обработка нажатия на кнопку способ 1
        View.OnClickListener oclBtnWhoAmI = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                textViewStudent.setText("Мой номер по списку № 2");
                itsMe.setChecked(true);
            }
        };
        btnWhoAmI.setOnClickListener(oclBtnWhoAmI);

    }
    // обработка нажатия на кнопку способ 2
    public void onMyButtonClick(View view) {
        Toast.makeText(this,"Еще один способ!", Toast.LENGTH_SHORT).show();
        textViewStudent.setText("Это не я(");
        itsMe.setChecked(false);
    }

}