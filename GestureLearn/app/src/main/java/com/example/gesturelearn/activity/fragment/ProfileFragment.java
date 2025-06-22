package com.example.gesturelearn.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gesturelearn.R;
import com.example.gesturelearn.activity.ChangePasswordActivity;
import com.example.gesturelearn.activity.EditProfileActivity;
import com.example.gesturelearn.activity.auth.LoginActivity;
import com.example.gesturelearn.data.DatabaseHelper;

// Impor untuk library Grafik
import com.example.gesturelearn.utils.ProgressManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    // Variabel untuk UI yang sudah ada
    private TextView tvNameValue, tvEmailValue, tvPointValue;
    private Button btnEditProfile, btnLogout, btnChangePassword;

    // Variabel BARU untuk Card Statistik
    private TextView tvStatisticUserXp;
    private LineChart weeklyChart;

    private DatabaseHelper databaseHelper;
    private String userEmail;

    private ActivityResultLauncher<Intent> editProfileLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inisialisasi launcher untuk EditProfileActivity
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        boolean isProfileUpdated = result.getData().getBooleanExtra("IS_PROFILE_UPDATED", false);
                        if (isProfileUpdated) {
                            loadUserData(); // Muat ulang data jika ada pembaruan
                            Toast.makeText(getContext(), "Profil berhasil diperbarui.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(getContext());

        // Inisialisasi Views dari bagian profil
        tvNameValue = view.findViewById(R.id.tv_name_value);
        tvEmailValue = view.findViewById(R.id.tv_email_value);
        tvPointValue = view.findViewById(R.id.tv_point_value);
        btnEditProfile = view.findViewById(R.id.btn_editProfile);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnChangePassword = view.findViewById(R.id.btn_change_password);

        // Inisialisasi Views BARU dari card statistik
        weeklyChart = view.findViewById(R.id.profile_weekly_chart);
        tvStatisticUserXp = view.findViewById(R.id.tv_statistic_user_xp);

        // Memuat data dan mengatur UI
        loadUserData();
        setupChart(); // Panggil method untuk mengatur grafik

        // Listeners untuk tombol
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("USER_EMAIL", userEmail);
            editProfileLauncher.launch(intent);
        });

        btnLogout.setOnClickListener(v -> logoutUser());

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        if (getContext() == null) return; // Mencegah error jika fragment belum ter-attach

        // 1. Ambil email dari SharedPreferences, bukan dari Intent Activity
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("GestureLearnPrefs", Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("userEmail", null);

        if (userEmail != null && !userEmail.isEmpty()) {
            String userName = databaseHelper.getUserName(userEmail);

            // 2. Ambil Poin/XP dari database menggunakan method yang sudah dibuat di DatabaseHelper
            int userPoints = databaseHelper.getUserPoints(userEmail);

            if (userName != null) {
                // Set semua data ke UI
                tvNameValue.setText(userName.toUpperCase());
                tvEmailValue.setText(userEmail);
                tvPointValue.setText(String.valueOf(userPoints)); // Ubah int ke String
                tvStatisticUserXp.setText(userPoints + " XP");
            }
        } else {
            // Handle jika userEmail null (seharusnya tidak terjadi jika sudah login)
            Toast.makeText(getContext(), "Gagal memuat data pengguna.", Toast.LENGTH_SHORT).show();
        }
    }

    // METHOD BARU: Untuk mengatur dan mengisi data grafik
    private void setupChart() {
        // Data sudah benar dari ProgressManager
        List<Entry> entries = ProgressManager.getWeeklyEntries(getContext());

        if (entries.isEmpty()) {
            weeklyChart.clear();
            weeklyChart.setNoDataText("Belum ada progres mingguan.");
            return;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Weekly Progress");
        dataSet.setColor(Color.parseColor("#5F7C50"));
        dataSet.setCircleColor(Color.parseColor("#5F7C50"));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(6f);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        weeklyChart.setData(lineData);

        weeklyChart.getDescription().setEnabled(false);
        weeklyChart.getLegend().setEnabled(false);
        weeklyChart.setDrawGridBackground(false);
        weeklyChart.setTouchEnabled(true);
        weeklyChart.setDragEnabled(true);
        weeklyChart.setScaleEnabled(false);
        weeklyChart.setPinchZoom(false);

        // Pengaturan Sumbu X (Hari)
        XAxis xAxis = weeklyChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        // Gunakan label dinamis yang baru dibuat
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getSevenDayLabels()));
        xAxis.setTextColor(Color.GRAY);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        // Pengaturan Sumbu Y (Kiri)
        weeklyChart.getAxisLeft().setTextColor(Color.GRAY);
        weeklyChart.getAxisLeft().setAxisMinimum(0f);
        weeklyChart.getAxisLeft().setDrawGridLines(true);
        weeklyChart.getAxisLeft().setGridColor(Color.parseColor("#E0E0E0"));
        weeklyChart.getAxisLeft().setDrawAxisLine(false);

        weeklyChart.getAxisRight().setEnabled(false);

        weeklyChart.invalidate();
    }


    private void logoutUser() {
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("GestureLearnPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
        Toast.makeText(getContext(), "Logout berhasil!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private String[] getSevenDayLabels() {
        String[] labels = new String[7];
        Calendar cal = Calendar.getInstance();
        // Gunakan format "E" untuk mendapatkan nama hari (e.g., "Sun", "Mon")
        SimpleDateFormat sdf = new SimpleDateFormat("E", Locale.US);

        // Mundur 6 hari dari hari ini untuk memulai loop
        cal.add(Calendar.DAY_OF_YEAR, -6);

        for (int i = 0; i < 7; i++) {
            // Ambil 2 karakter pertama dari nama hari (e.g., "Su", "Mo")
            labels[i] = sdf.format(cal.getTime()).substring(0, 2);
            // Maju satu hari untuk iterasi berikutnya
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return labels;
    }
}