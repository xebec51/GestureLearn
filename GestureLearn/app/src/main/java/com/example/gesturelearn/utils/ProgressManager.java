package com.example.gesturelearn.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.github.mikephil.charting.data.Entry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ProgressManager {

    private static final String PREFS_NAME = "WeeklyProgressPrefs";
    private static final String LAST_UPDATE_DATE_KEY = "last_update_date";
    private static final String STREAK_COUNT_KEY = "streak_count";
    private static final String LAST_STREAK_DATE_KEY = "last_streak_date";
    private static final String ACTIVE_DATES_KEY = "active_dates";

    private static final String[] DAY_KEYS = {
            "day_0_points", "day_1_points", "day_2_points", "day_3_points",
            "day_4_points", "day_5_points", "day_6_points"
    };

    public static void addPoints(Context context, int pointsToAdd) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        shiftDaysIfNeeded(prefs, editor);
        updateStreak(context);

        int currentPoints = prefs.getInt(DAY_KEYS[0], 0);
        editor.putInt(DAY_KEYS[0], currentPoints + pointsToAdd);

        String todayStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Set<String> activeDates = new HashSet<>(prefs.getStringSet(ACTIVE_DATES_KEY, new HashSet<>()));
        activeDates.add(todayStr);
        editor.putStringSet(ACTIVE_DATES_KEY, activeDates);

        editor.apply();
    }

    public static List<Entry> getWeeklyEntries(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        shiftDaysIfNeeded(prefs, prefs.edit());

        List<Entry> entries = new ArrayList<>();
        // Loop untuk 7 hari, di mana i=0 adalah 6 hari yang lalu, dan i=6 adalah hari ini.
        // Ini memastikan sumbu-x selalu kontinu dari 0 hingga 6.
        for (int i = 0; i < 7; i++) {
            // daysAgo akan menghitung mundur: 6, 5, 4, ..., 0
            int daysAgo = 6 - i;
            int points = prefs.getInt(DAY_KEYS[daysAgo], 0);
            // Tambahkan entri dengan sumbu-x yang berurutan (0, 1, 2, ..., 6)
            entries.add(new Entry(i, points));
        }
        return entries; // Data sudah terurut berdasarkan sumbu-x
    }

    public static Set<String> getActiveDates(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(ACTIVE_DATES_KEY, new HashSet<>());
    }
    public static void updateStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayStr = sdf.format(new Date());
        String lastStreakDateStr = prefs.getString(LAST_STREAK_DATE_KEY, "");
        int currentStreak = prefs.getInt(STREAK_COUNT_KEY, 0);
        if (lastStreakDateStr.isEmpty()) {
            if (currentStreak > 0) editor.putInt(STREAK_COUNT_KEY, currentStreak); else editor.putInt(STREAK_COUNT_KEY, 1);
        } else if (!lastStreakDateStr.equals(todayStr)) {
            try {
                Date lastDate = sdf.parse(lastStreakDateStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(lastDate);
                cal.add(Calendar.DAY_OF_YEAR, 1);
                String tomorrowOfLastDate = sdf.format(cal.getTime());
                if (tomorrowOfLastDate.equals(todayStr)) {
                    editor.putInt(STREAK_COUNT_KEY, currentStreak + 1);
                } else {
                    editor.putInt(STREAK_COUNT_KEY, 1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                editor.putInt(STREAK_COUNT_KEY, 1);
            }
        }
        editor.putString(LAST_STREAK_DATE_KEY, todayStr);
        editor.apply();
    }
    public static int getCurrentStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        updateStreak(context);
        return prefs.getInt(STREAK_COUNT_KEY, 0);
    }
    private static void shiftDaysIfNeeded(SharedPreferences prefs, SharedPreferences.Editor editor) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayStr = sdf.format(new Date());
        String lastUpdateDateStr = prefs.getString(LAST_UPDATE_DATE_KEY, "");

        if (lastUpdateDateStr.isEmpty()) {
            // Jika ini adalah pertama kalinya, simpan tanggal hari ini
            editor.putString(LAST_UPDATE_DATE_KEY, todayStr);
            editor.apply();
            return;
        }

        if (todayStr.equals(lastUpdateDateStr)) {
            return; // Tidak perlu digeser, masih di hari yang sama
        }

        try {
            Date lastDate = sdf.parse(lastUpdateDateStr);
            Date todayDate = sdf.parse(todayStr);

            // Hitung berapa hari telah berlalu
            long diffInMillis = todayDate.getTime() - lastDate.getTime();
            int daysPassed = (int) (diffInMillis / (1000 * 60 * 60 * 24));

            if (daysPassed <= 0) {
                // Jam dimundurkan, jangan lakukan apa-apa untuk menghindari kehilangan data
                return;
            }

            if (daysPassed >= DAY_KEYS.length) {
                // Jika sudah seminggu atau lebih, reset semua data
                for (String dayKey : DAY_KEYS) {
                    editor.putInt(dayKey, 0);
                }
            } else {
                // Geser data sebanyak hari yang telah berlalu
                // Pindahkan data lama ke posisi barunya
                for (int i = DAY_KEYS.length - 1; i >= daysPassed; i--) {
                    int sourcePoints = prefs.getInt(DAY_KEYS[i - daysPassed], 0);
                    editor.putInt(DAY_KEYS[i], sourcePoints);
                }
                // Isi hari-hari yang terlewat (celah) dengan nilai 0
                for (int i = 0; i < daysPassed; i++) {
                    editor.putInt(DAY_KEYS[i], 0);
                }
            }

            // Simpan tanggal pembaruan terakhir
            editor.putString(LAST_UPDATE_DATE_KEY, todayStr);
            editor.apply();

        } catch (ParseException e) {
            e.printStackTrace();
            // Jika terjadi error saat parsing, lebih aman untuk mereset data
            for (String dayKey : DAY_KEYS) {
                editor.putInt(dayKey, 0);
            }
            editor.putString(LAST_UPDATE_DATE_KEY, todayStr);
            editor.apply();
        }
    }
}