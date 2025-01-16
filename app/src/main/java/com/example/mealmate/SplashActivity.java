package com.example.mealmate;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Find the logo ImageView
        ImageView logo = findViewById(R.id.logo);

        // Load the animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        logo.startAnimation(animation);

        // Set a delay for the splash screen
        int splashTimeout = 3000; // 3 seconds
        new Handler().postDelayed(() -> {
//            Redirect to RegisterActivity

            FirebaseApp.initializeApp(this);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            Log.d(TAG,"DataUser"+user);

            startActivity(user!=null? new Intent(SplashActivity.this, MainActivity.class):  new Intent(SplashActivity.this, ValidationActivity.class));
            finish();

//            Intent intent = new Intent(SplashActivity.this, ValidationActivity.class);
//            startActivity(intent);
//            finish(); // Close SplashActivity
        }, splashTimeout);


    }
}