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

public class QuizQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    // Variabel untuk UI
    private TextView tvQuestionNumber, tvScore, tvKuisCategory;
    private ImageView ivQuizGif;
    private Button btnOption1, btnOption2, btnOption3, btnOption4, btnNext;
    private ImageButton btnCloseQuiz;

    // Variabel untuk data dan state kuis
    private List<QuizQuestion> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean answerSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);

        initViews();

        // Ambil kategori dari intent
        String category = getIntent().getStringExtra("QUIZ_CATEGORY");
        String title = getIntent().getStringExtra("QUIZ_TITLE");
        if (category == null) {
            category = "KOSAKATA"; // Default jika terjadi kesalahan
        }
        if (title != null) {
            tvKuisCategory.setText(title);
        }

        // Dapatkan akses ke database (DAO)
        SignDao signDao = AppDatabase.getDatabase(this).signDao();

        // Buat pertanyaan dari database
        questionList = QuizGenerator.generateQuestions(signDao, category, 10);

        if (questionList == null || questionList.isEmpty()) {
            Toast.makeText(this, "Gagal memuat data kuis.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayQuestion();

        // Set listener
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
        tvKuisCategory = findViewById(R.id.tv_kuis_category);
    }

    // ... Sisa metode (displayQuestion, checkAnswer, dll) tidak perlu diubah,
    // karena sudah dirancang untuk bekerja dengan objek QuizQuestion.
    // Salin sisa metode dari kode sebelumnya atau biarkan seperti apa adanya.

    private void displayQuestion() {
        resetOptionsUI();
        answerSelected = false;
        btnNext.setVisibility(View.GONE);
        QuizQuestion currentQuestion = questionList.get(currentQuestionIndex);
        tvQuestionNumber.setText("Soal " + (currentQuestionIndex + 1));
        tvScore.setText(score + " Skor");
        Glide.with(this).asGif().load(currentQuestion.getGifUrl()).placeholder(R.drawable.logo_splash).into(ivQuizGif);
        List<String> options = currentQuestion.getOptions();
        btnOption1.setText(options.get(0));
        btnOption2.setText(options.get(1));
        btnOption3.setText(options.get(2));
        btnOption4.setText(options.get(3));
    }

    private void resetOptionsUI() {
        Button[] buttons = {btnOption1, btnOption2, btnOption3, btnOption4};
        for (Button btn : buttons) {
            btn.setBackgroundResource(R.drawable.background_answer_default);
            btn.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            btn.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_option1 || viewId == R.id.btn_option2 || viewId == R.id.btn_option3 || viewId == R.id.btn_option4) {
            if (!answerSelected) {
                checkAnswer((Button) v);
            }
        } else if (viewId == R.id.btn_next) {
            handleNextButton();
        } else if (viewId == R.id.btn_close_quiz) {
            finish();
        }
    }

    private void checkAnswer(Button selectedOption) {
        answerSelected = true;
        btnOption1.setEnabled(false);
        btnOption2.setEnabled(false);
        btnOption3.setEnabled(false);
        btnOption4.setEnabled(false);

        String correctAnswer = questionList.get(currentQuestionIndex).getCorrectAnswer();
        if (selectedOption.getText().toString().equals(correctAnswer)) {
            score += 10;
            tvScore.setText(score + " Skor");
            selectedOption.setBackgroundResource(R.drawable.background_answer_correct);
            selectedOption.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            selectedOption.setBackgroundResource(R.drawable.background_answer_wrong);
            selectedOption.setTextColor(ContextCompat.getColor(this, R.color.white));
            showCorrectAnswer();
        }
        btnNext.setVisibility(View.VISIBLE);
    }

    private void showCorrectAnswer() {
        String correctAnswer = questionList.get(currentQuestionIndex).getCorrectAnswer();
        Button[] buttons = {btnOption1, btnOption2, btnOption3, btnOption4};
        for(Button btn : buttons) {
            if(btn.getText().toString().equals(correctAnswer)){
                btn.setBackgroundResource(R.drawable.background_answer_correct);
                btn.setTextColor(ContextCompat.getColor(this, R.color.white));
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