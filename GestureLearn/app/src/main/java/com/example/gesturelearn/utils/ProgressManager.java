package com.example.gesturelearn.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgressManager {

    private static final String PREFS_NAME = "WeeklyProgressPrefs";
    private static final String LAST_UPDATE_DATE_KEY = "last_update_date";

    // Simpan poin untuk 7 hari terakhir (0 = hari ini, 1 = kemarin, dst.)
    private static final String[] DAY_KEYS = {
            "day_0_points", "day_1_points", "day_2_points", "day_3_points",
            "day_4_points", "day_5_points", "day_6_points"
    };

    // Method untuk menambahkan poin ke progres hari ini
    public static void addPoints(Context context, int pointsToAdd) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Cek dan geser data jika hari telah berganti
        shiftDaysIfNeeded(prefs, editor);

        // Tambahkan poin ke hari ini
        int currentPoints = prefs.getInt(DAY_KEYS[0], 0);
        editor.putInt(DAY_KEYS[0], currentPoints + pointsToAdd);
        editor.apply();
    }

    // Method untuk mendapatkan data untuk grafik
    public static List<Entry> getWeeklyEntries(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Panggil shiftDaysIfNeeded untuk memastikan data up-to-date sebelum ditampilkan
        shiftDaysIfNeeded(prefs, prefs.edit());

        List<Entry> entries = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int todayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // Sunday=1, Monday=2, ..., Saturday=7

        // Buat data untuk 7 hari di grafik
        for (int i = 0; i < 7; i++) {
            // Tentukan hari di chart (0=Su, 1=Mo, ..., 6=Sa)
            int chartDayIndex = (todayOfWeek - 1 - i + 7) % 7;
            int points = prefs.getInt(DAY_KEYS[i], 0);
            entries.add(new Entry(chartDayIndex, points));
        }

        // Urutkan berdasarkan index hari agar grafik tergambar dengan benar
        Collections.sort(entries, (o1, o2) -> Float.compare(o1.getX(), o2.getX()));

        return entries;
    }

    private static void shiftDaysIfNeeded(SharedPreferences prefs, SharedPreferences.Editor editor) {
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lastUpdateDate = prefs.getString(LAST_UPDATE_DATE_KEY, "");

        if (!todayDate.equals(lastUpdateDate)) {
            // Hari telah berganti, geser data
            for (int i = DAY_KEYS.length - 1; i > 0; i--) {
                int previousDayPoints = prefs.getInt(DAY_KEYS[i - 1], 0);
                editor.putInt(DAY_KEYS[i], previousDayPoints);
            }
            // Reset hari ini ke 0
            editor.putInt(DAY_KEYS[0], 0);
            // Perbarui tanggal update
            editor.putString(LAST_UPDATE_DATE_KEY, todayDate);
            editor.apply();
        }
    }
}