package com.example.gesturelearn.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gesturelearn.R;
import com.example.gesturelearn.activity.MainActivity;
import com.example.gesturelearn.data.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;
import android.content.SharedPreferences;

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
        // hubungkan variabel dengan komponen di layout
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        tvSignUp = findViewById(R.id.tv_sign_up);

        // kasi tambah listener klik untuk tombol Sign In
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cek kredensial ke database
                boolean isValidUser = databaseHelper.checkUser(email, password);

                if (isValidUser) {
                    Toast.makeText(LoginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getSharedPreferences("GestureLearnPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("userEmail", email);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USER_EMAIL", email);
                    startActivity(intent);
                    finish(); // Tutup LoginActivity agar pengguna tidak bisa kembali
                } else {
                    Toast.makeText(LoginActivity.this, "Email atau Password salah!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Pindah ke halaman Sign Up", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener untuk teks Sign Up
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}