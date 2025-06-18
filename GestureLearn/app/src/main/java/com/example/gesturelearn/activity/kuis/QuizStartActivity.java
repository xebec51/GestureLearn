package com.example.gesturelearn.activity.kuis;

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

        // Listener untuk tombol "Mulai"
        btnStartQuiz.setOnClickListener(v -> {
            // TODO: Pindah ke halaman pertanyaan kuis yang sebenarnya
            // Untuk sekarang, kita hanya akan menampilkan Toast
            Toast.makeText(this, "Memulai Kuis Kosakata!", Toast.LENGTH_SHORT).show();

            // Contoh Intent untuk ke halaman pertanyaan (jika sudah ada)
            // Intent intent = new Intent(QuizStartActivity.this, QuizQuestionsActivity.class);
            // startActivity(intent);
            // finish();
        });
    }
}