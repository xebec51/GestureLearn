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
import com.example.gesturelearn.data.AppDatabase;
import com.example.gesturelearn.data.SignDao;
import com.example.gesturelearn.model.QuizQuestion;
import com.example.gesturelearn.utils.QuizGenerator;

import java.util.List;

public class AlphabetQuizActivity extends AppCompatActivity implements View.OnClickListener {

    // Ganti Button dengan TextView untuk pilihan jawaban
    private TextView tvOption1, tvOption2, tvOption3, tvOption4;
    private TextView tvQuestionNumber, tvScore, tvKuisCategory;
    private ImageView ivQuizGif;
    private Button btnNext;
    private ImageButton btnCloseQuiz;

    private List<QuizQuestion> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean answerSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabet_quiz);

        initViews();

        String category = getIntent().getStringExtra("QUIZ_CATEGORY");
        String title = getIntent().getStringExtra("QUIZ_TITLE");

        if (title != null) tvKuisCategory.setText(title);
        if (category == null) category = "ABJAD_BISINDO";

        SignDao signDao = AppDatabase.getDatabase(this).signDao();
        questionList = QuizGenerator.generateQuestions(signDao, category, 10);

        if (questionList.isEmpty()) {
            Toast.makeText(this, "Gagal memuat data kuis.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayQuestion();
        setListeners();
    }

    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvScore = findViewById(R.id.tv_score);
        ivQuizGif = findViewById(R.id.iv_quiz_gif);
        btnNext = findViewById(R.id.btn_next);
        btnCloseQuiz = findViewById(R.id.btn_close_quiz);
        tvKuisCategory = findViewById(R.id.tv_kuis_category);
        tvOption1 = findViewById(R.id.option1);
        tvOption2 = findViewById(R.id.option2);
        tvOption3 = findViewById(R.id.option3);
        tvOption4 = findViewById(R.id.option4);
    }

    private void setListeners() {
        tvOption1.setOnClickListener(this);
        tvOption2.setOnClickListener(this);
        tvOption3.setOnClickListener(this);
        tvOption4.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnCloseQuiz.setOnClickListener(this);
    }

    // Metode lain (displayQuestion, checkAnswer, dll.) disalin dari QuizQuestionActivity
    // namun dengan sedikit modifikasi untuk menggunakan TextView.

    private void displayQuestion() {
        resetOptionsUI();
        answerSelected = false;
        btnNext.setVisibility(View.GONE);
        QuizQuestion currentQuestion = questionList.get(currentQuestionIndex);
        tvQuestionNumber.setText("Soal " + (currentQuestionIndex + 1));
        tvScore.setText(score + " Skor");
        Glide.with(this).asGif().load(currentQuestion.getGifUrl()).placeholder(R.drawable.logo_splash).into(ivQuizGif);
        List<String> options = currentQuestion.getOptions();
        tvOption1.setText(options.get(0));
        tvOption2.setText(options.get(1));
        tvOption3.setText(options.get(2));
        tvOption4.setText(options.get(3));
    }

    private void resetOptionsUI() {
        TextView[] options = {tvOption1, tvOption2, tvOption3, tvOption4};
        for (TextView option : options) {
            option.setBackgroundResource(R.drawable.background_alphabet_option_default);
            option.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            option.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.option1 || id == R.id.option2 || id == R.id.option3 || id == R.id.option4) {
            if (!answerSelected) {
                checkAnswer((TextView) v);
            }
        } else if (id == R.id.btn_next) {
            handleNextButton();
        } else if (id == R.id.btn_close_quiz) {
            finish();
        }
    }

    private void checkAnswer(TextView selectedOption) {
        answerSelected = true;
        tvOption1.setEnabled(false);
        tvOption2.setEnabled(false);
        tvOption3.setEnabled(false);
        tvOption4.setEnabled(false);

        String correctAnswer = questionList.get(currentQuestionIndex).getCorrectAnswer();
        if (selectedOption.getText().toString().equals(correctAnswer)) {
            score += 10;
            tvScore.setText(score + " Skor");
            selectedOption.setBackgroundResource(R.drawable.background_alphabet_option_correct);
            selectedOption.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            selectedOption.setBackgroundResource(R.drawable.background_alphabet_option_wrong);
            selectedOption.setTextColor(ContextCompat.getColor(this, R.color.white));
            showCorrectAnswer();
        }
        btnNext.setVisibility(View.VISIBLE);
    }

    private void showCorrectAnswer() {
        String correctAnswer = questionList.get(currentQuestionIndex).getCorrectAnswer();
        TextView[] options = {tvOption1, tvOption2, tvOption3, tvOption4};
        for (TextView option : options) {
            if (option.getText().toString().equals(correctAnswer)) {
                option.setBackgroundResource(R.drawable.background_alphabet_option_correct);
                option.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
        }
    }

    private void handleNextButton() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            displayQuestion();
        } else {
            Intent intent = new Intent(this, QuizResultActivity.class);
            intent.putExtra("FINAL_SCORE", score);
            startActivity(intent);
            finish();
        }
    }
}