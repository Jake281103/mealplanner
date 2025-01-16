package com.example.mealmate;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ValidationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        // Buttons
        Button createAccountButton = findViewById(R.id.create_account_button);
        Button loginButton = findViewById(R.id.login_button);

        // Navigate to Create Account Page
        createAccountButton.setOnClickListener(view -> {
            Intent intent = new Intent(ValidationActivity.this, Register.class);
            startActivity(intent);
        });

        // Navigate to Login Page
        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(ValidationActivity.this, Login.class);
            startActivity(intent);
        });
    }
}