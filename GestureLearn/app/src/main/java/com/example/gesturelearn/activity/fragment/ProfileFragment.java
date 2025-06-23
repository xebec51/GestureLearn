package com.example.gesturelearn.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.example.gesturelearn.R;
import com.example.gesturelearn.activity.ChangePasswordActivity;
import com.example.gesturelearn.activity.EditProfileActivity;
import com.example.gesturelearn.activity.auth.LoginActivity;
import com.example.gesturelearn.adapter.CalendarAdapter;
import com.example.gesturelearn.data.DatabaseHelper;
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
import java.util.Set;

public class ProfileFragment extends Fragment {

    // Variabel UI
    private TextView tvNameValue, tvEmailValue, tvPointValue, tvStatisticUserXp, tvStreakCount, tvCurrentMonth;
    private Button btnEditProfile, btnLogout, btnChangePassword;
    private LineChart weeklyChart;
    private ImageView ivStreakIcon, iv_profilePicture;
    private RecyclerView rvCalendar;
    private ImageButton btnPreviousMonth, btnNextMonth, btn_edit_photo;

    private DatabaseHelper databaseHelper;
    private String userEmail;
    private ActivityResultLauncher<Intent> editProfileLauncher;
    private Calendar selectedDate;

    private ActivityResultLauncher<CropImageContractOptions> cropImageLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        if (result.getData().getBooleanExtra("IS_PROFILE_UPDATED", false)) {
                            loadUserData();
                            Toast.makeText(getContext(), "Profil berhasil diperbarui.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        cropImageLauncher = registerForActivityResult(new CropImageContract(), result -> {
            if (result.isSuccessful()) {
                Uri croppedImageUri = result.getUriContent();
                if (getContext() != null) {
                    Glide.with(getContext()).load(croppedImageUri).into(iv_profilePicture);
                }
                if (userEmail != null) {
                    databaseHelper.updateProfilePhotoUri(userEmail, croppedImageUri.toString());
                }
            } else {
                Exception error = result.getError();
                if (error != null && getContext() != null) {
                    Toast.makeText(getContext(), "Gagal memotong gambar: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

        iv_profilePicture = view.findViewById(R.id.iv_profilePicture);
        btn_edit_photo = view.findViewById(R.id.btn_edit_photo);
        tvNameValue = view.findViewById(R.id.tv_name_value);
        tvEmailValue = view.findViewById(R.id.tv_email_value);
        tvPointValue = view.findViewById(R.id.tv_point_value);
        btnEditProfile = view.findViewById(R.id.btn_editProfile);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
        weeklyChart = view.findViewById(R.id.profile_weekly_chart);
        tvStatisticUserXp = view.findViewById(R.id.tv_statistic_user_xp);
        tvStreakCount = view.findViewById(R.id.tv_streak_count);
        ivStreakIcon = view.findViewById(R.id.iv_streak_icon);
        rvCalendar = view.findViewById(R.id.rvCalendar);
        tvCurrentMonth = view.findViewById(R.id.tvCurrentMonth);
        btnPreviousMonth = view.findViewById(R.id.btnPreviousMonth);
        btnNextMonth = view.findViewById(R.id.btnNextMonth);

        selectedDate = Calendar.getInstance();

        loadUserData();
        setupChart();
        setupCalendar();

        btn_edit_photo.setOnClickListener(v -> startCropImage());

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
        btnPreviousMonth.setOnClickListener(v -> {
            selectedDate.add(Calendar.MONTH, -1);
            setupCalendar();
        });
        btnNextMonth.setOnClickListener(v -> {
            selectedDate.add(Calendar.MONTH, 1);
            setupCalendar();
        });
    }

    private void startCropImage() {
        CropImageOptions cropImageOptions = new CropImageOptions();

        cropImageOptions.cropShape = CropImageView.CropShape.OVAL; // DIUBAH: Menggunakan CropImageView
        cropImageOptions.aspectRatioX = 1;
        cropImageOptions.aspectRatioY = 1;
        cropImageOptions.fixAspectRatio = true;
        cropImageOptions.outputCompressQuality = 70;
        cropImageOptions.guidelines = CropImageView.Guidelines.ON_TOUCH; // DIUBAH: Menggunakan CropImageView

        CropImageContractOptions options = new CropImageContractOptions(null, cropImageOptions);

        cropImageLauncher.launch(options);
    }

    private void loadUserData() {
        if (getContext() == null) return;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("GestureLearnPrefs", Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("userEmail", null);

        if (userEmail != null && !userEmail.isEmpty()) {
            String photoUriString = databaseHelper.getProfilePhotoUri(userEmail);
            if (photoUriString != null) {
                Glide.with(getContext())
                        .load(Uri.parse(photoUriString))
                        .placeholder(R.drawable.img_profile_default)
                        .into(iv_profilePicture);
            } else {
                iv_profilePicture.setImageResource(R.drawable.img_profile_default);
            }

            String userName = databaseHelper.getUserName(userEmail);
            int userPoints = databaseHelper.getUserPoints(userEmail);
            int currentStreak = ProgressManager.getCurrentStreak(getContext());

            if (userName != null) {
                tvNameValue.setText(userName.toUpperCase());
                tvEmailValue.setText(userEmail);
                tvPointValue.setText(String.valueOf(userPoints));
                tvStatisticUserXp.setText(userPoints + " XP");
                tvStreakCount.setText(String.valueOf(currentStreak));
                if (currentStreak > 0) {
                    ivStreakIcon.setImageResource(R.drawable.img_fire);
                } else {
                    ivStreakIcon.setImageResource(R.drawable.img_frozen);
                }
            }
        } else {
            Toast.makeText(getContext(), "Gagal memuat data pengguna.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCalendar() {
        if (getContext() == null) return;
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", new Locale("id", "ID"));
        tvCurrentMonth.setText(monthYearFormat.format(selectedDate.getTime()));

        ArrayList<String> daysInMonth = new ArrayList<>();
        Calendar monthCalendar = (Calendar) selectedDate.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int daysInCurrentMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dayOfWeekOfFirst = monthCalendar.get(Calendar.DAY_OF_WEEK);

        for (int i = 1; i < dayOfWeekOfFirst; i++) {
            daysInMonth.add("");
        }
        for (int i = 1; i <= daysInCurrentMonth; i++) {
            daysInMonth.add(String.valueOf(i));
        }

        Set<String> activeDates = ProgressManager.getActiveDates(getContext());
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);

        Calendar today = Calendar.getInstance();
        int currentDay = (selectedDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                selectedDate.get(Calendar.MONTH) == today.get(Calendar.MONTH))
                ? today.get(Calendar.DAY_OF_MONTH) : 0;

        CalendarAdapter adapter = new CalendarAdapter(daysInMonth, activeDates, year, month, currentDay);
        rvCalendar.setLayoutManager(new GridLayoutManager(getContext(), 7));
        rvCalendar.setAdapter(adapter);
    }

    private String[] getSevenDayLabels() {
        String[] labels = new String[7];
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("E", Locale.US);
        cal.add(Calendar.DAY_OF_YEAR, -6);
        for (int i = 0; i < 7; i++) {
            labels[i] = sdf.format(cal.getTime()).substring(0, 2);
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return labels;
    }

    private void setupChart() {
        if (getContext() == null) return;
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
        dataSet.setMode(LineDataSet.Mode.LINEAR);

        dataSet.setDrawFilled(true);

        dataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.chart_fade_green));

        dataSet.setDrawCircles(true); // Pastikan lingkaran selalu digambar
        dataSet.setDrawCircleHole(true);
        dataSet.setCircleHoleRadius(2.5f);
        dataSet.setCircleColor(Color.parseColor("#5F7C50"));
        dataSet.setCircleHoleColor(Color.WHITE);

        LineData lineData = new LineData(dataSet);
        weeklyChart.setData(lineData);
        weeklyChart.getDescription().setEnabled(false);
        weeklyChart.getLegend().setEnabled(false);
        weeklyChart.setDrawGridBackground(false);
        weeklyChart.setTouchEnabled(true);
        weeklyChart.setDragEnabled(true);
        weeklyChart.setScaleEnabled(false);
        weeklyChart.setPinchZoom(false);

        XAxis xAxis = weeklyChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getSevenDayLabels()));
        xAxis.setTextColor(Color.GRAY);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

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

            Toast.makeText(getContext(), "Logout berhasil!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }
}