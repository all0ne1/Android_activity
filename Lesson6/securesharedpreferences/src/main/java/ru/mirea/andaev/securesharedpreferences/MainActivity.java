package ru.mirea.andaev.securesharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

import ru.mirea.andaev.securesharedpreferences.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            final KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
            final String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

            final SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            binding.nameInput.setText(secureSharedPreferences.getString("secure", "Samuel L.J."));
        }
        catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.saveButton.setOnClickListener(v -> {
            try {
                KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
                String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

                SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                        "secret_shared_prefs",
                        mainKeyAlias,
                        getBaseContext(),
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
                SharedPreferences.Editor editor = secureSharedPreferences.edit();
                editor.putString("secure", binding.nameInput.getText().toString());
                editor.apply();
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}