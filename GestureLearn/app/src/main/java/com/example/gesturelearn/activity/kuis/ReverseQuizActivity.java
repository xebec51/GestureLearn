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
import com.example.gesturelearn.model.ReverseQuizQuestion;
import com.example.gesturelearn.model.Sign;
import com.example.gesturelearn.utils.QuizGenerator;

import java.util.List;

public class ReverseQuizActivity extends AppCompatActivity implements View.OnClickListener {

    // Variabel untuk UI
    private TextView tvQuestionNumber, tvScore, tvKuisCategory, tvQuestionText;
    private ImageView ivOption1, ivOption2, ivOption3, ivOption4;
    private Button btnNext;
    private ImageButton btnCloseQuiz;

    // Variabel untuk data dan state kuis
    private List<ReverseQuizQuestion> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean answerSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pastikan Anda menggunakan layout yang sudah kita perbaiki
        setContentView(R.layout.activity_reverse_quiz);

        initViews();
        setListeners();

        // Ambil data dari Intent
        String category = getIntent().getStringExtra("QUIZ_CATEGORY");
        String title = getIntent().getStringExtra("QUIZ_TITLE");

        if (title != null) {
            tvKuisCategory.setText(title);
        }
        if (category == null) {
            category = "ABJAD_SIBI"; // Default jika terjadi kesalahan
        }

        // Dapatkan akses ke database dan buat pertanyaan
        SignDao signDao = AppDatabase.getDatabase(this).signDao();
        questionList = QuizGenerator.generateReverseQuestions(signDao, category, 10);

        if (questionList == null || questionList.isEmpty()) {
            Toast.makeText(this, "Gagal memuat data kuis.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Tampilkan pertanyaan pertama
        displayQuestion();
    }

    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvScore = findViewById(R.id.tv_score);
        tvKuisCategory = findViewById(R.id.tv_kuis_category);
        tvQuestionText = findViewById(R.id.tv_question_text);
        ivOption1 = findViewById(R.id.iv_option1);
        ivOption2 = findViewById(R.id.iv_option2);
        ivOption3 = findViewById(R.id.iv_option3);
        ivOption4 = findViewById(R.id.iv_option4);
        btnNext = findViewById(R.id.btn_next);
        btnCloseQuiz = findViewById(R.id.btn_close_quiz);
    }

    private void setListeners() {
        ivOption1.setOnClickListener(this);
        ivOption2.setOnClickListener(this);
        ivOption3.setOnClickListener(this);
        ivOption4.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnCloseQuiz.setOnClickListener(this);
    }

    private void displayQuestion() {
        resetOptionsUI();
        answerSelected = false;
        btnNext.setVisibility(View.GONE);

        ReverseQuizQuestion currentQuestion = questionList.get(currentQuestionIndex);
        Sign correctAnswer = currentQuestion.getCorrectAnswer();
        List<Sign> options = currentQuestion.getOptions();
        ImageView[] imageViews = {ivOption1, ivOption2, ivOption3, ivOption4};

        // Set UI
        tvQuestionNumber.setText("Soal " + (currentQuestionIndex + 1));
        tvScore.setText(score + " Skor");
        tvQuestionText.setText("Yang manakah isyarat huruf " + correctAnswer.word + "?");

        // Muat gambar ke ImageView dan simpan data Sign di tag
        for (int i = 0; i < options.size(); i++) {
            imageViews[i].setTag(options.get(i)); // Menyimpan objek Sign di tag
            Glide.with(this)
                    .asGif()
                    .load(options.get(i).gifUrl)
                    .placeholder(R.drawable.logo_splash)
                    .into(imageViews[i]);
        }
    }

    private void resetOptionsUI() {
        ImageView[] imageViews = {ivOption1, ivOption2, ivOption3, ivOption4};
        for (ImageView iv : imageViews) {
            iv.setBackgroundResource(R.drawable.background_answer_default);
            iv.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_option1 || id == R.id.iv_option2 || id == R.id.iv_option3 || id == R.id.iv_option4) {
            if (!answerSelected) {
                checkAnswer((ImageView) v);
            }
        } else if (id == R.id.btn_next) {
            handleNextButton();
        } else if (id == R.id.btn_close_quiz) {
            finish();
        }
    }

    private void checkAnswer(ImageView selectedImageView) {
        answerSelected = true;
        setOptionsEnabled(false);

        Sign selectedSign = (Sign) selectedImageView.getTag();
        Sign correctSign = questionList.get(currentQuestionIndex).getCorrectAnswer();

        if (selectedSign.word.equals(correctSign.word)) {
            score += 10;
            tvScore.setText(score + " Skor");
            selectedImageView.setBackgroundResource(R.drawable.background_answer_correct);
        } else {
            selectedImageView.setBackgroundResource(R.drawable.background_answer_wrong);
            showCorrectAnswer();
        }
        btnNext.setVisibility(View.VISIBLE);
    }

    private void showCorrectAnswer() {
        Sign correctSign = questionList.get(currentQuestionIndex).getCorrectAnswer();
        ImageView[] imageViews = {ivOption1, ivOption2, ivOption3, ivOption4};
        for (ImageView iv : imageViews) {
            Sign sign = (Sign) iv.getTag();
            if (sign.word.equals(correctSign.word)) {
                iv.setBackgroundResource(R.drawable.background_answer_correct);
                break;
            }
        }
    }

    private void setOptionsEnabled(boolean enabled) {
        ivOption1.setEnabled(enabled);
        ivOption2.setEnabled(enabled);
        ivOption3.setEnabled(enabled);
        ivOption4.setEnabled(enabled);
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