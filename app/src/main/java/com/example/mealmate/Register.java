package com.example.mealmate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealmate.databinding.ActivityRegisterBinding;
import com.example.mealmate.model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Register extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(view -> registerForm());
        binding.loginRedirect.setOnClickListener(view -> {
            startActivity(new Intent(this, Login.class));
        });
    }

    public void registerForm() {
        String name = Objects.requireNonNull(binding.regUsername.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.regEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.regPassword.getText()).toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your name, email, and password", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Your email format is invalid", Toast.LENGTH_SHORT).show();
        } else if (password.length() <= 5) {
            Toast.makeText(this, "Password must be at least 5 characters", Toast.LENGTH_SHORT).show();
        } else {
            addToFirebase(name, email, password);
        }
    }

    public void addToFirebase(String name, String email, String password) {
        FirebaseApp.initializeApp(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Get the user ID from Firebase Auth
                String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

                // Create a User object
                User user = new User();
                user.setUserId(userId);
                user.setName(name);
                user.setEmail(email);

                // Store the User object in Firestore
                firestore.collection("users").document(userId).set(user)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Account successfully created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Account creation failed in Firestore", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        });
            } else {
                Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
