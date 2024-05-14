package ru.mirea.andaev.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import ru.mirea.andaev.httpurlconnection.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.button.setOnClickListener(v -> {
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                Toast.makeText(this, "Request network module...",Toast.LENGTH_LONG).show();
                return;
            }

            NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
            if (networkinfo == null) {
                Toast.makeText(this, "Request network connection...",Toast.LENGTH_LONG).show();
                return;
            }
            new IpInfoRequest("https://ipinfo.io/json",this.binding).execute();
        });
    }

}