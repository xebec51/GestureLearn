package com.example.gesturelearn.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gesturelearn.R;
import com.example.gesturelearn.data.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private TextView tvSignIn;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);

        // Inisialisasi komponen
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSignUp = findViewById(R.id.btn_sign_up);
        tvSignIn = findViewById(R.id.tv_sign_in);

        // Mengatur teks untuk link "Sign In" dengan format HTML
        tvSignIn.setText(Html.fromHtml(getString(R.string.prompt_signin), Html.FROM_HTML_MODE_COMPACT));

        // Listener untuk tombol Sign Up
        btnSignUp.setOnClickListener(v -> {
            // Ambil data dari input
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Validasi input kosong
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validasi password dan konfirmasi password
            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Password tidak cocok!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cek apakah email sudah ada di database
            if (databaseHelper.checkEmail(email)) {
                Toast.makeText(RegisterActivity.this, "Email sudah terdaftar!", Toast.LENGTH_SHORT).show();
            } else {
                // Jika email belum ada, tambahkan user baru
                boolean isAdded = databaseHelper.addUser(name, email, password);
                if (isAdded) {
                    Toast.makeText(RegisterActivity.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                    // Setelah registrasi, arahkan ke halaman Login
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Tutup activity register
                } else {
                    Toast.makeText(RegisterActivity.this, "Registrasi gagal, coba lagi.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener untuk teks "Sign In"
        tvSignIn.setOnClickListener(v -> {
            // Kembali ke LoginActivity
            finish();
        });
    }
}