package com.example.gesturelearn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gesturelearn.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Handler untuk menunda perpindahan ke activity berikutnya
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Buat Intent untuk pindah ke OnboardingActivity
            Intent intent = new Intent(SplashScreenActivity.this, OnboardingActivity.class);
            startActivity(intent);
            // Tutup SplashScreenActivity agar tidak bisa kembali
            finish();
        }, SPLASH_DELAY);
    }
}