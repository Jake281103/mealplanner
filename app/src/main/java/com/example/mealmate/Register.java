package com.example.mealmate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mealmate.databinding.ActivityRegisterBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Register extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnRegister.setOnClickListener(view->registerForm());
        binding.loginRedirect.setOnClickListener(view->{
            startActivity(new Intent(this, Login.class));
        });

    }

    public void registerForm() {
        String name = Objects.requireNonNull(binding.regUsername.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.regEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.regPassword.getText()).toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your name, email and password", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Your email format is invalid", Toast.LENGTH_SHORT).show();
        } else if (password.length() <= 5) {
            Toast.makeText(this, "Password must be at least 5 characters", Toast.LENGTH_SHORT).show();
        }else{
            addToFirebase(email,password);
        }
    }

    public void addToFirebase(String email, String password){
        FirebaseApp.initializeApp(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task->{
            if(task.isSuccessful()){
                Toast.makeText(this,"Account successfully created",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            else{
                Toast.makeText(this,"Account creation failed ",Toast.LENGTH_SHORT).show();
            }
        });
    }
}