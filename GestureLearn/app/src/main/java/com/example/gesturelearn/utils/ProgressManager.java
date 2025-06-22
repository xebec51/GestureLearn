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
    // Kunci baru untuk menyimpan semua tanggal aktif
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

        // Tambahkan tanggal hari ini ke daftar tanggal aktif
        String todayStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Set<String> activeDates = new HashSet<>(prefs.getStringSet(ACTIVE_DATES_KEY, new HashSet<>()));
        activeDates.add(todayStr);
        editor.putStringSet(ACTIVE_DATES_KEY, activeDates);

        editor.apply();
    }

    // METHOD BARU untuk mendapatkan daftar tanggal aktif
    public static Set<String> getActiveDates(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(ACTIVE_DATES_KEY, new HashSet<>());
    }

    // Sisa kode di bawah ini tidak berubah
    public static void updateStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayStr = sdf.format(new Date());
        String lastStreakDateStr = prefs.getString(LAST_STREAK_DATE_KEY, "");
        int currentStreak = prefs.getInt(STREAK_COUNT_KEY, 0);
        if (lastStreakDateStr.isEmpty()) {
            editor.putInt(STREAK_COUNT_KEY, 1);
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
    public static List<Entry> getWeeklyEntries(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        shiftDaysIfNeeded(prefs, prefs.edit());
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int daysAgo = 6 - i;
            int points = prefs.getInt(DAY_KEYS[daysAgo], 0);
            entries.add(new Entry(i, points));
        }
        return entries;
    }
    private static void shiftDaysIfNeeded(SharedPreferences prefs, SharedPreferences.Editor editor) {
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lastUpdateDate = prefs.getString(LAST_UPDATE_DATE_KEY, "");
        if (!todayDate.equals(lastUpdateDate)) {
            for (int i = DAY_KEYS.length - 1; i > 0; i--) {
                int previousDayPoints = prefs.getInt(DAY_KEYS[i - 1], 0);
                editor.putInt(DAY_KEYS[i], previousDayPoints);
            }
            editor.putInt(DAY_KEYS[0], 0);
            editor.putString(LAST_UPDATE_DATE_KEY, todayDate);
            editor.apply();
        }
    }
}