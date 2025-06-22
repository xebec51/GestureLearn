package com.example.gesturelearn.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesturelearn.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private final List<String> daysOfMonth;
    private final Set<String> activeDates; // Set tanggal "yyyy-MM-dd"
    private final int currentDay;
    private final int currentMonth;
    private final int currentYear;

    public CalendarAdapter(List<String> daysOfMonth, Set<String> activeDates, int year, int month, int day) {
        this.daysOfMonth = daysOfMonth;
        this.activeDates = activeDates;
        this.currentYear = year;
        this.currentMonth = month;
        this.currentDay = day;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String dayText = daysOfMonth.get(position);

        if (dayText.isEmpty()) {
            // Ini adalah sel kosong sebelum tanggal 1
            holder.cardDay.setVisibility(View.INVISIBLE);
        } else {
            holder.cardDay.setVisibility(View.VISIBLE);
            holder.tvDayNumber.setText(dayText);
            holder.ivFire.setVisibility(View.GONE); // Sembunyikan ikon api secara default
            holder.tvDayNumber.setVisibility(View.VISIBLE);

            // Buat format tanggal "yyyy-MM-dd" untuk hari ini
            String fullDate = String.format(Locale.US, "%d-%02d-%02d", currentYear, currentMonth + 1, Integer.parseInt(dayText));

            // Cek apakah tanggal ini ada di daftar tanggal aktif
            if (activeDates.contains(fullDate)) {
                holder.ivFire.setVisibility(View.VISIBLE);
                holder.tvDayNumber.setVisibility(View.GONE);
            }

            // Tandai hari ini
            if (Integer.parseInt(dayText) == currentDay) {
                holder.tvDayNumber.setTextColor(Color.WHITE);
                holder.tvDayNumber.setTypeface(null, Typeface.BOLD);
                holder.cardDay.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
            } else {
                holder.tvDayNumber.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.text_primary));
                holder.tvDayNumber.setTypeface(null, Typeface.NORMAL);
                holder.cardDay.setCardBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDayNumber;
        final ImageView ivFire;
        final CardView cardDay;

        ViewHolder(View itemView) {
            super(itemView);
            tvDayNumber = itemView.findViewById(R.id.tvDayNumber);
            ivFire = itemView.findViewById(R.id.ivFire);
            cardDay = itemView.findViewById(R.id.cardDay);
        }
    }
}