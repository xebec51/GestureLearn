package com.example.gesturelearn.activity.kuis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.gesturelearn.R;

public class QuizStartActivity extends AppCompatActivity {

    private String quizCategory;
    private String quizTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start);

        ImageButton btnBack = findViewById(R.id.btn_back);
        Button btnStartQuiz = findViewById(R.id.btn_start_quiz);
        TextView tvQuizTitle = findViewById(R.id.tv_quiz_title);

        // Ambil kategori dan judul dari Intent
        quizCategory = getIntent().getStringExtra("QUIZ_CATEGORY");
        quizTitle = getIntent().getStringExtra("QUIZ_TITLE");

        // Set judul halaman sesuai kuis yang dipilih
        if (quizTitle != null) {
            tvQuizTitle.setText(quizTitle);
        }

        btnBack.setOnClickListener(v -> finish());

        btnStartQuiz.setOnClickListener(v -> {
            Intent intent;
            if (quizCategory.equals("KOSAKATA")) {
                intent = new Intent(QuizStartActivity.this, QuizQuestionActivity.class);
            } else if (quizCategory.equals("ABJAD_SIBI")) {
                // DIUBAH: Sekarang kita arahkan ke ReverseQuizActivity
                intent = new Intent(QuizStartActivity.this, ReverseQuizActivity.class);
            } else { // Untuk ABJAD_BISINDO
                intent = new Intent(QuizStartActivity.this, AlphabetQuizActivity.class);
            }

            intent.putExtra("QUIZ_CATEGORY", quizCategory);
            intent.putExtra("QUIZ_TITLE", quizTitle);
            startActivity(intent);
            finish();
        });
    }
}