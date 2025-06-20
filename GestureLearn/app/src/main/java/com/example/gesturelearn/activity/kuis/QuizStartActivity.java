// File: D:\GitHub\GestureLearn\GestureLearn\app\src\main\java\com\example\gesturelearn\activity\kuis\QuizStartActivity.java
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

        quizCategory = getIntent().getStringExtra("QUIZ_CATEGORY");
        quizTitle = getIntent().getStringExtra("QUIZ_TITLE");

        if (quizTitle != null) {
            tvQuizTitle.setText(quizTitle);
        }

        btnBack.setOnClickListener(v -> finish());

        btnStartQuiz.setOnClickListener(v -> {
            Intent intent;

            if ("KOSAKATA".equals(quizCategory)) {
                intent = new Intent(QuizStartActivity.this, VocabularyQuizActivity.class);
            } else if ("ABJAD_SIBI".equals(quizCategory) || "ABJAD_BISINDO".equals(quizCategory)) {
                intent = new Intent(QuizStartActivity.this, AlphabetQuizActivity.class);
            } else {
                Toast.makeText(this, "Kategori kuis tidak dikenal.", Toast.LENGTH_SHORT).show();
                return;
            }

            intent.putExtra("QUIZ_CATEGORY", quizCategory);
            intent.putExtra("QUIZ_TITLE", quizTitle);
            startActivity(intent);
            finish();
        });
    }
}