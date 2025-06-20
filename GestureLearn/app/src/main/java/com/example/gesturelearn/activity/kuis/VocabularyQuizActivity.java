// File: D:\GitHub\GestureLearn\GestureLearn\app\src\main\java\com\example\gesturelearn\activity\kuis\VocabularyQuizActivity.java
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

public class VocabularyQuizActivity extends AppCompatActivity implements View.OnClickListener {

    // UI Umum
    private TextView tvScore, tvKuisCategory;
    private Button btnNext;
    private ImageButton btnCloseQuiz;

    // UI untuk Soal Standar (Tebak Kata)
    private CardView cardStandardVocabulary;
    private TextView tvQuestionNumberStandard;
    private ImageView ivQuizGif;
    private Button[] standardOptions;

    // UI untuk Soal Terbalik (Tebak GIF)
    private CardView cardReverseVocabulary;
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
        setContentView(R.layout.activity_vocabulary_quiz);

        initViews();
        setListeners();

        androidx.activity.OnBackPressedCallback callback = new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        // Menggunakan generator baru untuk kuis kosakata campuran
        SignDao signDao = AppDatabase.getDatabase(this).signDao();
        questionList = QuizGenerator.generateMixedVocabularyQuiz(signDao, 10);

        if (questionList == null || questionList.isEmpty()) {
            Toast.makeText(this, "Gagal memuat data kuis.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayQuestion();
    }

    private void initViews() {
        // UI Umum
        tvScore = findViewById(R.id.tv_score);
        tvKuisCategory = findViewById(R.id.tv_kuis_category);
        btnNext = findViewById(R.id.btn_next);
        btnCloseQuiz = findViewById(R.id.btn_close_quiz);

        // UI Standar
        cardStandardVocabulary = findViewById(R.id.card_standard_vocabulary);
        tvQuestionNumberStandard = findViewById(R.id.tv_question_number_standard);
        ivQuizGif = findViewById(R.id.iv_quiz_gif);
        standardOptions = new Button[]{
                findViewById(R.id.btn_option1_standard),
                findViewById(R.id.btn_option2_standard),
                findViewById(R.id.btn_option3_standard),
                findViewById(R.id.btn_option4_standard)
        };

        // UI Terbalik
        cardReverseVocabulary = findViewById(R.id.card_reverse_vocabulary);
        tvQuestionNumberReverse = findViewById(R.id.tv_question_number_reverse);
        tvQuestionTextReverse = findViewById(R.id.tv_question_text_reverse);
        reverseOptions = new ImageView[]{
                findViewById(R.id.iv_option1_reverse),
                findViewById(R.id.iv_option2_reverse),
                findViewById(R.id.iv_option3_reverse),
                findViewById(R.id.iv_option4_reverse)
        };
    }

    private void setListeners() {
        btnNext.setOnClickListener(this);
        btnCloseQuiz.setOnClickListener(this);
        for (Button option : standardOptions) {
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

        if (currentQuestionIndex >= questionList.size()) return;

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
        cardStandardVocabulary.setVisibility(View.VISIBLE);
        cardReverseVocabulary.setVisibility(View.GONE);
        tvQuestionNumberStandard.setText(questionNumberText);

        Glide.with(this).asGif().load(question.getGifUrl()).placeholder(R.drawable.logo_splash).into(ivQuizGif);
        List<String> options = question.getOptions();
        for (int i = 0; i < standardOptions.length; i++) {
            standardOptions[i].setText(options.get(i));
        }
    }

    private void displayReverseQuestion(ReverseQuizQuestion question, String questionNumberText) {
        cardReverseVocabulary.setVisibility(View.VISIBLE);
        cardStandardVocabulary.setVisibility(View.GONE);
        tvQuestionNumberReverse.setText(questionNumberText);
        tvQuestionTextReverse.setText("Gerakan manakah yang artinya '" + question.getCorrectAnswer().word + "'?");

        List<Sign> options = question.getOptions();
        for (int i = 0; i < reverseOptions.length; i++) {
            reverseOptions[i].setTag(options.get(i));
            Glide.with(this).asGif().load(options.get(i).gifUrl).placeholder(R.drawable.logo_splash).into(reverseOptions[i]);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            handleNextButton();
            return;
        }

        if (v.getId() == R.id.btn_close_quiz) {
            // Ganti finish(); dengan pemanggilan dialog
            showExitConfirmationDialog();
            return;
        }

        if (answerSelected) return;

        IQuizQuestion currentQuestion = questionList.get(currentQuestionIndex);
        if (currentQuestion.getQuestionType().equals("STANDARD")) {
            for (Button button : standardOptions) {
                if (button.getId() == v.getId()) {
                    checkStandardAnswer(button);
                    break;
                }
            }
        } else if (currentQuestion.getQuestionType().equals("REVERSE")) {
            for (ImageView imageView : reverseOptions) {
                if (imageView.getId() == v.getId()) {
                    checkReverseAnswer(imageView);
                    break;
                }
            }
        }
    }

    private void checkStandardAnswer(Button selectedOption) {
        answerSelected = true;
        setStandardOptionsEnabled(false);
        QuizQuestion currentQuestion = (QuizQuestion) questionList.get(currentQuestionIndex);
        String correctAnswer = currentQuestion.getCorrectAnswer();

        if (selectedOption.getText().toString().equals(correctAnswer)) {
            score += 10;
            selectedOption.setBackgroundResource(R.drawable.background_answer_correct);
            selectedOption.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            selectedOption.setBackgroundResource(R.drawable.background_answer_wrong);
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

    // --- Helper Methods ---
    private void resetAllUI() {
        for (Button option : standardOptions) {
            option.setBackgroundResource(R.drawable.background_answer_default);
            option.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            option.setEnabled(true);
        }
        for (ImageView option : reverseOptions) {
            option.setBackgroundResource(R.drawable.background_answer_default);
            option.setEnabled(true);
        }
    }

    private void setStandardOptionsEnabled(boolean enabled) {
        for (Button option : standardOptions) option.setEnabled(enabled);
    }

    private void setReverseOptionsEnabled(boolean enabled) {
        for (ImageView option : reverseOptions) option.setEnabled(enabled);
    }

    private void showCorrectStandardAnswer(String correctAnswer) {
        for (Button option : standardOptions) {
            if (option.getText().toString().equals(correctAnswer)) {
                option.setBackgroundResource(R.drawable.background_answer_correct);
                option.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
        }
    }

    private void showCorrectReverseAnswer(Sign correctAnswer) {
        for (ImageView option : reverseOptions) {
            if (((Sign) option.getTag()).word.equals(correctAnswer.word)) {
                option.setBackgroundResource(R.drawable.background_answer_correct);
                break;
            }
        }
    }

    private void showExitConfirmationDialog() {
        final android.app.Dialog dialog = new android.app.Dialog(this);

        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_exit_confirmation);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        Button btnYes = dialog.findViewById(R.id.btn_dialog_yes);
        Button btnNo = dialog.findViewById(R.id.btn_dialog_no);

        btnYes.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        btnNo.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }
}