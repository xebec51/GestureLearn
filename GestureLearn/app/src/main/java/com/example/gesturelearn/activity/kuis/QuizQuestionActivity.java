package com.example.gesturelearn.activity.kuis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.gesturelearn.R;
import com.example.gesturelearn.model.QuizQuestion;
import com.example.gesturelearn.utils.QuizGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    // Variabel untuk UI
    private TextView tvQuestionNumber, tvScore;
    private ImageView ivQuizGif;
    private Button btnOption1, btnOption2, btnOption3, btnOption4, btnNext;
    private ImageButton btnCloseQuiz;

    // Variabel untuk data dan state kuis
    private List<QuizQuestion> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean answerSelected = false;
    private Button selectedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);

        // Inisialisasi semua view
        initViews();

        // Generate 10 pertanyaan kuis
        questionList = QuizGenerator.generateQuestions(this, 10);

        if (questionList.isEmpty()) {
            Toast.makeText(this, "Gagal memuat data kuis.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Tampilkan pertanyaan pertama
        displayQuestion();

        // Set listener untuk semua tombol yang bisa diklik
        btnOption1.setOnClickListener(this);
        btnOption2.setOnClickListener(this);
        btnOption3.setOnClickListener(this);
        btnOption4.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnCloseQuiz.setOnClickListener(this);
    }

    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvScore = findViewById(R.id.tv_score);
        ivQuizGif = findViewById(R.id.iv_quiz_gif);
        btnOption1 = findViewById(R.id.btn_option1);
        btnOption2 = findViewById(R.id.btn_option2);
        btnOption3 = findViewById(R.id.btn_option3);
        btnOption4 = findViewById(R.id.btn_option4);
        btnNext = findViewById(R.id.btn_next);
        btnCloseQuiz = findViewById(R.id.btn_close_quiz);
    }

    private void displayQuestion() {
        // Reset tampilan sebelum menampilkan soal baru
        resetOptionsUI();
        answerSelected = false;
        selectedButton = null;
        btnNext.setVisibility(View.GONE);

        // Ambil data pertanyaan saat ini
        QuizQuestion currentQuestion = questionList.get(currentQuestionIndex);

        // Update UI dengan data pertanyaan baru
        tvQuestionNumber.setText("Soal " + (currentQuestionIndex + 1));
        tvScore.setText(score + " Skor");

        // Muat GIF menggunakan Glide
        Glide.with(this)
                .asGif()
                .load(currentQuestion.getGifUrl())
                .placeholder(R.drawable.logo_splash) // Gambar sementara saat loading
                .into(ivQuizGif);

        // Set teks pada tombol pilihan jawaban
        List<String> options = currentQuestion.getOptions();
        btnOption1.setText(options.get(0));
        btnOption2.setText(options.get(1));
        btnOption3.setText(options.get(2));
        btnOption4.setText(options.get(3));
    }

    private void resetOptionsUI() {
        btnOption1.setBackgroundResource(R.drawable.background_answer_default);
        btnOption2.setBackgroundResource(R.drawable.background_answer_default);
        btnOption3.setBackgroundResource(R.drawable.background_answer_default);
        btnOption4.setBackgroundResource(R.drawable.background_answer_default);

        btnOption1.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        btnOption2.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        btnOption3.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        btnOption4.setTextColor(ContextCompat.getColor(this, R.color.text_primary));

        btnOption1.setEnabled(true);
        btnOption2.setEnabled(true);
        btnOption3.setEnabled(true);
        btnOption4.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_option1 || viewId == R.id.btn_option2 || viewId == R.id.btn_option3 || viewId == R.id.btn_option4) {
            if (!answerSelected) {
                selectedButton = (Button) v;
                checkAnswer(selectedButton);
            }
        } else if (viewId == R.id.btn_next) {
            handleNextButton();
        } else if (viewId == R.id.btn_close_quiz) {
            finish(); // Keluar dari kuis
        }
    }

    private void checkAnswer(Button selectedOption) {
        answerSelected = true;
        btnOption1.setEnabled(false);
        btnOption2.setEnabled(false);
        btnOption3.setEnabled(false);
        btnOption4.setEnabled(false);

        String correctAnswer = questionList.get(currentQuestionIndex).getCorrectAnswer();
        String selectedAnswer = selectedOption.getText().toString();

        if (selectedAnswer.equals(correctAnswer)) {
            // Jawaban benar
            score += 10;
            tvScore.setText(score + " Skor");
            selectedOption.setBackgroundResource(R.drawable.background_answer_correct);
            selectedOption.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            // Jawaban salah
            selectedOption.setBackgroundResource(R.drawable.background_answer_wrong);
            selectedOption.setTextColor(ContextCompat.getColor(this, R.color.white));
            // Tampilkan jawaban yang benar
            showCorrectAnswer();
        }
        btnNext.setVisibility(View.VISIBLE);
    }

    private void showCorrectAnswer() {
        String correctAnswer = questionList.get(currentQuestionIndex).getCorrectAnswer();
        if (btnOption1.getText().toString().equals(correctAnswer)) {
            btnOption1.setBackgroundResource(R.drawable.background_answer_correct);
            btnOption1.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (btnOption2.getText().toString().equals(correctAnswer)) {
            btnOption2.setBackgroundResource(R.drawable.background_answer_correct);
            btnOption2.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (btnOption3.getText().toString().equals(correctAnswer)) {
            btnOption3.setBackgroundResource(R.drawable.background_answer_correct);
            btnOption3.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (btnOption4.getText().toString().equals(correctAnswer)) {
            btnOption4.setBackgroundResource(R.drawable.background_answer_correct);
            btnOption4.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    private void handleNextButton() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            // Masih ada soal berikutnya
            displayQuestion();
        } else {
            // Kuis selesai, pindah ke halaman hasil
            Intent intent = new Intent(this, QuizResultActivity.class);
            intent.putExtra("FINAL_SCORE", score);
            startActivity(intent);
            finish(); // Tutup activity pertanyaan
        }
    }
}