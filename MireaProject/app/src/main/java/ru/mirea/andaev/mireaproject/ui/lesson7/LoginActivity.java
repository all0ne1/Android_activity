package ru.mirea.andaev.mireaproject.ui.lesson7;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.andaev.mireaproject.MainActivity;
import ru.mirea.andaev.mireaproject.R;
import ru.mirea.andaev.mireaproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();
        binding.signInButton.setOnClickListener(v -> signIn(binding.loginEmailInput.getText().toString(),
                binding.loginPasswordInput.getText().toString()));

        binding.createAccountButton.setOnClickListener(v -> createAccount(binding.loginEmailInput.getText().toString(),
                binding.loginPasswordInput.getText().toString()));
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            binding.statusTextView.setText(R.string.signed_out);
            binding.loginActionPanel.setVisibility(View.VISIBLE);
        }
    }
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        Toast.makeText(LoginActivity.this, "Registration succeeded.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Registration failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signIn(String email, String password) {
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if (!task.isSuccessful()) {
                            binding.statusTextView.setText(R.string.auth_failed);
                        }
                    });
        }
        catch (Exception e){
            Toast.makeText(LoginActivity.this,"Пожалуйста, введи данные", Toast.LENGTH_SHORT).show();
        }
    }

}