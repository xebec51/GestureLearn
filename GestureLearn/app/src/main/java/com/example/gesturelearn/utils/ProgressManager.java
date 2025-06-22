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
        shiftDaysIfNeeded(prefs, prefs.edit());

        List<Entry> entries = new ArrayList<>();
        // Loop dari 6 hari yang lalu (i=0) sampai hari ini (i=6)
        for (int i = 0; i < 7; i++) {
            // daysAgo: 6, 5, 4, 3, 2, 1, 0
            int daysAgo = 6 - i;
            int points = prefs.getInt(DAY_KEYS[daysAgo], 0);
            // Tambahkan entry dengan sumbu-x dari 0 hingga 6
            entries.add(new Entry(i, points));
        }
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