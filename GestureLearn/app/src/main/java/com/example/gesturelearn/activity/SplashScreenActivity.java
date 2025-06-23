package com.example.gesturelearn.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gesturelearn.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("GestureLearnPrefs", MODE_PRIVATE);

            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            Intent intent;
            if (isLoggedIn) {
                String userEmail = sharedPreferences.getString("userEmail", null);
                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                intent.putExtra("userEmail", userEmail);
            } else {
                intent = new Intent(SplashScreenActivity.this, OnboardingActivity.class);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}