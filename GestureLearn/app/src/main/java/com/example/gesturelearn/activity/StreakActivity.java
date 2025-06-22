package com.example.gesturelearn.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesturelearn.R;
import com.example.gesturelearn.adapter.CalendarAdapter;
import com.example.gesturelearn.utils.ProgressManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

public class StreakActivity extends AppCompatActivity {

    private TextView tvCurrentMonth, tvStreakCount;
    private RecyclerView rvCalendar;
    private ImageButton btnBack, btnPreviousMonth, btnNextMonth;
    private Calendar selectedDate; // Kalender untuk melacak bulan yang ditampilkan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streak);

        // Inisialisasi Views dengan ID yang benar
        tvCurrentMonth = findViewById(R.id.tvCurrentMonth);
        tvStreakCount = findViewById(R.id.tvStreakCount); // ID dari CardView besar
        rvCalendar = findViewById(R.id.rvCalendar);
        btnBack = findViewById(R.id.btn_back);
        btnPreviousMonth = findViewById(R.id.btnPreviousMonth);
        btnNextMonth = findViewById(R.id.btnNextMonth);

        selectedDate = Calendar.getInstance(); // Mulai dari bulan ini

        // Setup Listeners
        btnBack.setOnClickListener(v -> finish());
        btnPreviousMonth.setOnClickListener(v -> {
            selectedDate.add(Calendar.MONTH, -1);
            setupCalendar();
        });
        btnNextMonth.setOnClickListener(v -> {
            selectedDate.add(Calendar.MONTH, 1);
            setupCalendar();
        });

        setupCalendar();
    }

    private void setupCalendar() {
        // Set judul bulan dan tahun
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", new Locale("id", "ID"));
        tvCurrentMonth.setText(monthYearFormat.format(selectedDate.getTime()));

        // Set jumlah streak (tidak berubah berdasarkan bulan)
        int currentStreak = ProgressManager.getCurrentStreak(this);
        tvStreakCount.setText(String.valueOf(currentStreak));

        // Siapkan data untuk grid kalender
        ArrayList<String> daysInMonth = new ArrayList<>();
        Calendar monthCalendar = (Calendar) selectedDate.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        int daysInCurrentMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        // Format hari: Minggu=1, Senin=2, ..., Sabtu=7
        int dayOfWeekOfFirst = monthCalendar.get(Calendar.DAY_OF_WEEK);

        // Tambahkan sel kosong untuk hari sebelum tanggal 1
        // (Minggu sebagai hari pertama)
        for (int i = 1; i < dayOfWeekOfFirst; i++) {
            daysInMonth.add("");
        }

        // Tambahkan semua tanggal dalam bulan
        for (int i = 1; i <= daysInCurrentMonth; i++) {
            daysInMonth.add(String.valueOf(i));
        }

        // Dapatkan data tanggal aktif dari ProgressManager
        Set<String> activeDates = ProgressManager.getActiveDates(this);

        // Buat dan set adapter
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        // Cek apakah bulan yang ditampilkan adalah bulan ini
        boolean isCurrentMonth = (year == Calendar.getInstance().get(Calendar.YEAR)) &&
                (month == Calendar.getInstance().get(Calendar.MONTH));
        int day = isCurrentMonth ? Calendar.getInstance().get(Calendar.DAY_OF_MONTH) : 0;

        CalendarAdapter adapter = new CalendarAdapter(daysInMonth, activeDates, year, month, day);
        rvCalendar.setLayoutManager(new GridLayoutManager(this, 7));
        rvCalendar.setAdapter(adapter);
    }
}