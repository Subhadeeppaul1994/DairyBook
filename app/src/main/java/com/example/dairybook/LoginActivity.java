package com.example.dairybook;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    // Define hardcoded credentials for app testing
    private static final String TEST_EMAIL = "sp@gmail.com";
    private static final String TEST_PASSWORD = "123456";

    private TextInputEditText etEmail, etPassword;
    private CheckBox cbRememberMe;
    private TextView tvForgotPassword, tvSignUp;
    private MaterialButton btnLogin, btnGoogleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components matching your XML IDs
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);

        // Pre-fill fields with test credentials for fast testing
        etEmail.setText(TEST_EMAIL);
        etPassword.setText(TEST_PASSWORD);

        // Login Button Click Listener
        btnLogin.setOnClickListener(v -> handleLogin());

        // Google Sign In Button Click Listener
        btnGoogleLogin.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Google Sign-In clicked", Toast.LENGTH_SHORT).show();
            // TODO: Integrate Google Sign-In logic here
        });

        // Forgot Password Click Listener
        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Redirect to Forgot Password Screen", Toast.LENGTH_SHORT).show();
        });

        // Redirect to Registration/Sign Up Activity
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // Static credential validation
        if (email.equalsIgnoreCase(TEST_EMAIL) && password.equals(TEST_PASSWORD)) {
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

            // Navigate to Home Main Screen
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Prevent user from returning to login screen via back button
        } else {
            Toast.makeText(this, "Invalid credentials. Use: admin@dairybook.com / 123456", Toast.LENGTH_LONG).show();
        }
    }
}