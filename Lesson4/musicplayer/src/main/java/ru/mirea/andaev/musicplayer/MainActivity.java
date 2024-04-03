package ru.mirea.andaev.musicplayer;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import ru.mirea.andaev.musicplayer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Fragment musicFragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        musicFragment = new musicFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.musicContainer,musicFragment).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}