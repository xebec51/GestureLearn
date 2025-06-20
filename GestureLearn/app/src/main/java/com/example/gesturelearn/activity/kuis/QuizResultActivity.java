package com.example.gesturelearn.activity.kuis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gesturelearn.R;

public class QuizResultActivity extends AppCompatActivity {

    private TextView tvFinalScore, tvCorrectAnswers, tvWrongAnswers;
    private Button btnRetry, btnBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        // Inisialisasi Views
        tvFinalScore = findViewById(R.id.tv_final_score);
        tvCorrectAnswers = findViewById(R.id.tv_correct_answers);
        tvWrongAnswers = findViewById(R.id.tv_wrong_answers);
        btnRetry = findViewById(R.id.btn_retry);
        btnBackToHome = findViewById(R.id.btn_back_to_home);

        // Ambil data skor dari Intent
        int finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);
        int totalQuestions = 10; // Sesuai spesifikasi, ada 10 soal
        int correctAnswers = finalScore / 10;
        int wrongAnswers = totalQuestions - correctAnswers;

        // Tampilkan data ke UI
        tvFinalScore.setText(String.valueOf(finalScore));
        tvCorrectAnswers.setText(correctAnswers + " Jawaban benar");
        tvWrongAnswers.setText(wrongAnswers + " Jawaban salah");

        // Set listener untuk tombol
        btnRetry.setOnClickListener(v -> {
            // Mulai lagi kuis dari awal
            Intent intent = new Intent(QuizResultActivity.this, VocabularyQuizActivity.class);
            startActivity(intent);
            finish();
        });

        btnBackToHome.setOnClickListener(v -> {
            // Kembali ke halaman utama (Kuis Fragment)
            finish();
        });
    }
}