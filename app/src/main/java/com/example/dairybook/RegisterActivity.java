package com.example.dairybook;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etRegEmail, etPhone, etRegPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private TextView tvLogin;
    private View layoutLoadingOverlay;
    private LottieAnimationView lottieLoader;

    // Firebase Auth & Database instances
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth and Database with explicit regional URL
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase
                .getInstance("https://dairybook-64844-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("users");

        // Bind layout views
        etFullName = findViewById(R.id.etFullName);
        etRegEmail = findViewById(R.id.etRegEmail);
        etPhone = findViewById(R.id.etPhone);
        etRegPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Bind Loading Overlay views
        layoutLoadingOverlay = findViewById(R.id.layoutLoadingOverlay);
        lottieLoader = findViewById(R.id.lottieLoader);

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

        // Show Lottie loading overlay and explicitly play animation
        btnRegister.setEnabled(false);
        layoutLoadingOverlay.setVisibility(View.VISIBLE);
        lottieLoader.playAnimation();

        // Create user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("uid", userId);
                            userMap.put("fullName", fullName);
                            userMap.put("email", email);
                            userMap.put("phone", phone);

                            databaseReference.child(userId).setValue(userMap)
                                    .addOnCompleteListener(dbTask -> {
                                        // Stop and hide loading overlay immediately
                                        lottieLoader.cancelAnimation();
                                        layoutLoadingOverlay.setVisibility(View.GONE);
                                        btnRegister.setEnabled(true);

                                        if (dbTask.isSuccessful()) {
                                            // Clear text fields
                                            etFullName.setText("");
                                            etRegEmail.setText("");
                                            etPhone.setText("");
                                            etRegPassword.setText("");
                                            etConfirmPassword.setText("");

                                            Toast.makeText(RegisterActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            String errorMsg = dbTask.getException() != null ? dbTask.getException().getMessage() : "Unknown database error";
                                            Toast.makeText(RegisterActivity.this, "Failed to save user data: " + errorMsg, Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            lottieLoader.cancelAnimation();
                            layoutLoadingOverlay.setVisibility(View.GONE);
                            btnRegister.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "User session is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        lottieLoader.cancelAnimation();
                        layoutLoadingOverlay.setVisibility(View.GONE);
                        btnRegister.setEnabled(true);

                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Authentication failed";
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }
}