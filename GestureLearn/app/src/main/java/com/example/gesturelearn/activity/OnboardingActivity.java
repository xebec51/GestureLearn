package com.example.gesturelearn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gesturelearn.R;
import com.example.gesturelearn.activity.auth.LoginActivity;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}