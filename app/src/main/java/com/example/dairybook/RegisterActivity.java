package com.example.dairybook;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etRegEmail, etPhone, etRegPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Bind layout views
        etFullName = findViewById(R.id.etFullName);
        etRegEmail = findViewById(R.id.etRegEmail);
        etPhone = findViewById(R.id.etPhone);
        etRegPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Handle Registration action
        btnRegister.setOnClickListener(v -> handleRegistration());

        // Redirect back to Login
        tvLogin.setOnClickListener(v -> finish());
    }

    private void handleRegistration() {
        String fullName = etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
        String email = etRegEmail.getText() != null ? etRegEmail.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
        String password = etRegPassword.getText() != null ? etRegPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etRegEmail.setError("Email is required");
            etRegEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone number is required");
            etPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etRegPassword.setError("Password is required");
            etRegPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etRegPassword.setError("Password must be at least 6 characters");
            etRegPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Please retype your password");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

        // Navigate to Home Main Screen
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}