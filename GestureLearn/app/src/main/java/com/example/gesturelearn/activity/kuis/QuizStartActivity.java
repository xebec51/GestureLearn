package com.example.gesturelearn.activity.kuis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gesturelearn.R;

public class QuizStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start);

        ImageButton btnBack = findViewById(R.id.btn_back);
        Button btnStartQuiz = findViewById(R.id.btn_start_quiz);

        // Listener untuk tombol kembali
        btnBack.setOnClickListener(v -> {
            finish(); // Menutup activity ini dan kembali ke layar sebelumnya
        });

        btnStartQuiz.setOnClickListener(v -> {
            // Ganti Toast dengan Intent ke QuizQuestionActivity
            Intent intent = new Intent(QuizStartActivity.this, QuizQuestionActivity.class);
            startActivity(intent);
            finish(); // Tutup activity ini agar tidak bisa kembali
        });
    }
}