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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.gesturelearn.R;
import com.example.gesturelearn.data.AppDatabase;
import com.example.gesturelearn.data.SignDao;
import com.example.gesturelearn.model.IQuizQuestion;
import com.example.gesturelearn.model.QuizQuestion;
import com.example.gesturelearn.model.ReverseQuizQuestion;
import com.example.gesturelearn.model.Sign;
import com.example.gesturelearn.utils.QuizGenerator;

import java.util.List;

public class AlphabetQuizActivity extends AppCompatActivity implements View.OnClickListener {

    // UI Umum
    private TextView tvScore, tvKuisCategory;
    private Button btnNext;
    private ImageButton btnCloseQuiz;

    // UI untuk Soal Standar (Tebak Huruf)
    private CardView cardStandardQuestion;
    private TextView tvQuestionNumberStandard;
    private ImageView ivQuizGif;
    private TextView[] standardOptions;

    // UI untuk Soal Terbalik (Tebak GIF)
    private CardView cardReverseQuestion;
    private TextView tvQuestionNumberReverse;
    private TextView tvQuestionTextReverse;
    private ImageView[] reverseOptions;

    // Data Kuis
    private List<IQuizQuestion> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean answerSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabet_quiz);

        initViews();
        setListeners();

        String category = getIntent().getStringExtra("QUIZ_CATEGORY");
        String title = getIntent().getStringExtra("QUIZ_TITLE");

        if (title != null) tvKuisCategory.setText(title);
        if (category == null) category = "ABJAD_BISINDO"; // Default

        SignDao signDao = AppDatabase.getDatabase(this).signDao();
        // Menggunakan generator baru untuk kuis campuran
        questionList = QuizGenerator.generateMixedAlphabetQuiz(signDao, category, 10);

        if (questionList == null || questionList.isEmpty()) {
            Toast.makeText(this, "Gagal memuat data kuis.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayQuestion();
    }

    private void initViews() {
        // Init UI Umum
        tvScore = findViewById(R.id.tv_score);
        tvKuisCategory = findViewById(R.id.tv_kuis_category);
        btnNext = findViewById(R.id.btn_next);
        btnCloseQuiz = findViewById(R.id.btn_close_quiz);

        // Init UI Standar
        cardStandardQuestion = findViewById(R.id.card_standard_question);
        tvQuestionNumberStandard = findViewById(R.id.tv_question_number_standard);
        ivQuizGif = findViewById(R.id.iv_quiz_gif);
        standardOptions = new TextView[]{
                findViewById(R.id.option1_standard),
                findViewById(R.id.option2_standard),
                findViewById(R.id.option3_standard),
                findViewById(R.id.option4_standard)
        };

        // Init UI Terbalik
        cardReverseQuestion = findViewById(R.id.card_reverse_question);
        tvQuestionNumberReverse = findViewById(R.id.tv_question_number_reverse);
        tvQuestionTextReverse = findViewById(R.id.tv_question_text_reverse);
        reverseOptions = new ImageView[]{
                findViewById(R.id.option1_reverse),
                findViewById(R.id.option2_reverse),
                findViewById(R.id.option3_reverse),
                findViewById(R.id.option4_reverse)
        };
    }

    private void setListeners() {
        btnNext.setOnClickListener(this);
        btnCloseQuiz.setOnClickListener(this);
        for (TextView option : standardOptions) {
            option.setOnClickListener(this);
        }
        for (ImageView option : reverseOptions) {
            option.setOnClickListener(this);
        }
    }

    private void displayQuestion() {
        resetAllUI();
        answerSelected = false;
        btnNext.setVisibility(View.GONE);

        IQuizQuestion currentQuestion = questionList.get(currentQuestionIndex);
        String questionNumberText = "Soal " + (currentQuestionIndex + 1);
        tvScore.setText(score + " Skor");

        if (currentQuestion.getQuestionType().equals("STANDARD")) {
            displayStandardQuestion((QuizQuestion) currentQuestion, questionNumberText);
        } else if (currentQuestion.getQuestionType().equals("REVERSE")) {
            displayReverseQuestion((ReverseQuizQuestion) currentQuestion, questionNumberText);
        }
    }

    private void displayStandardQuestion(QuizQuestion question, String questionNumberText) {
        cardStandardQuestion.setVisibility(View.VISIBLE);
        cardReverseQuestion.setVisibility(View.GONE);
        tvQuestionNumberStandard.setText(questionNumberText);

        Glide.with(this).asGif().load(question.getGifUrl()).placeholder(R.drawable.logo_splash).into(ivQuizGif);
        List<String> options = question.getOptions();
        for (int i = 0; i < standardOptions.length; i++) {
            standardOptions[i].setText(options.get(i));
        }
    }

    private void displayReverseQuestion(ReverseQuizQuestion question, String questionNumberText) {
        cardReverseQuestion.setVisibility(View.VISIBLE);
        cardStandardQuestion.setVisibility(View.GONE);
        tvQuestionNumberReverse.setText(questionNumberText);
        tvQuestionTextReverse.setText("Yang manakah isyarat huruf " + question.getCorrectAnswer().word + "?");

        List<Sign> options = question.getOptions();
        for (int i = 0; i < reverseOptions.length; i++) {
            reverseOptions[i].setTag(options.get(i)); // Simpan data di tag
            Glide.with(this)
                    .asGif()
                    .load(options.get(i).gifUrl)
                    .placeholder(R.drawable.logo_splash)
                    .into(reverseOptions[i]);
        }
    }

    @Override
    public void onClick(View v) {
        if (answerSelected) {
            if (v.getId() == R.id.btn_next) handleNextButton();
            return;
        }

        IQuizQuestion currentQuestion = questionList.get(currentQuestionIndex);
        if (currentQuestion.getQuestionType().equals("STANDARD")) {
            checkStandardAnswer((TextView) v);
        } else if (currentQuestion.getQuestionType().equals("REVERSE")) {
            checkReverseAnswer((ImageView) v);
        }

        if (v.getId() == R.id.btn_close_quiz) finish();
    }

    private void checkStandardAnswer(TextView selectedOption) {
        answerSelected = true;
        setStandardOptionsEnabled(false);
        QuizQuestion currentQuestion = (QuizQuestion) questionList.get(currentQuestionIndex);
        String correctAnswer = currentQuestion.getCorrectAnswer();

        if (selectedOption.getText().toString().equals(correctAnswer)) {
            score += 10;
            selectedOption.setBackgroundResource(R.drawable.background_alphabet_option_correct);
            selectedOption.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            selectedOption.setBackgroundResource(R.drawable.background_alphabet_option_wrong);
            selectedOption.setTextColor(ContextCompat.getColor(this, R.color.white));
            showCorrectStandardAnswer(correctAnswer);
        }
        btnNext.setVisibility(View.VISIBLE);
    }

    private void checkReverseAnswer(ImageView selectedOption) {
        answerSelected = true;
        setReverseOptionsEnabled(false);
        ReverseQuizQuestion currentQuestion = (ReverseQuizQuestion) questionList.get(currentQuestionIndex);
        Sign correctAnswer = currentQuestion.getCorrectAnswer();
        Sign selectedAnswer = (Sign) selectedOption.getTag();

        if (selectedAnswer.word.equals(correctAnswer.word)) {
            score += 10;
            selectedOption.setBackgroundResource(R.drawable.background_answer_correct);
        } else {
            selectedOption.setBackgroundResource(R.drawable.background_answer_wrong);
            showCorrectReverseAnswer(correctAnswer);
        }
        btnNext.setVisibility(View.VISIBLE);
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

    // --- Helper Methods to Reset and Show Correct Answers ---

    private void resetAllUI() {
        // Reset Standard UI
        for (TextView option : standardOptions) {
            option.setBackgroundResource(R.drawable.background_alphabet_option_default);
            option.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            option.setEnabled(true);
        }
        // Reset Reverse UI
        for (ImageView option : reverseOptions) {
            option.setBackgroundResource(R.drawable.background_answer_default);
            option.setEnabled(true);
        }
    }

    private void setStandardOptionsEnabled(boolean enabled) {
        for (TextView option : standardOptions) option.setEnabled(enabled);
    }

    private void setReverseOptionsEnabled(boolean enabled) {
        for (ImageView option : reverseOptions) option.setEnabled(enabled);
    }

    private void showCorrectStandardAnswer(String correctAnswer) {
        for (TextView option : standardOptions) {
            if (option.getText().toString().equals(correctAnswer)) {
                option.setBackgroundResource(R.drawable.background_alphabet_option_correct);
                option.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
        }
    }

    private void showCorrectReverseAnswer(Sign correctAnswer) {
        for (ImageView option : reverseOptions) {
            Sign sign = (Sign) option.getTag();
            if (sign.word.equals(correctAnswer.word)) {
                option.setBackgroundResource(R.drawable.background_answer_correct);
                break;
            }
        }
    }
}