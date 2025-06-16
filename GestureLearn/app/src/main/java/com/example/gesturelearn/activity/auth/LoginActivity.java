package com.example.gesturelearn.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gesturelearn.R;
import com.example.gesturelearn.data.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView tvSignUp;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);
        // Menghubungkan variabel dengan komponen di layout (findViewById)
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        tvSignUp = findViewById(R.id.tv_sign_up);

        // Menambahkan listener klik untuk tombol Sign In
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Validasi input
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cek kredensial ke database
                boolean isValidUser = databaseHelper.checkUser(email, password);

                if (isValidUser) {
                    // Jika login berhasil
                    Toast.makeText(LoginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();

                    // Pindah ke MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Tutup LoginActivity agar pengguna tidak bisa kembali
                } else {
                    // Jika login gagal
                    Toast.makeText(LoginActivity.this, "Email atau Password salah!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Menambahkan listener klik untuk teks Sign Up
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke RegisterActivity (jika sudah dibuat)
                // Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                // startActivity(intent);
                Toast.makeText(LoginActivity.this, "Pindah ke halaman Sign Up", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener untuk teks Sign Up
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GANTI baris Toast dengan Intent
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}