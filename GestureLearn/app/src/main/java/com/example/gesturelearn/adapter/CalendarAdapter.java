package com.example.gesturelearn.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gesturelearn.R;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private final List<String> daysOfMonth;
    private final Set<String> activeDates;
    private final int currentDay;
    private final int currentMonth;
    private final int currentYear;
    private final boolean isCurrentDisplayMonth;

    public CalendarAdapter(List<String> daysOfMonth, Set<String> activeDates, int year, int month, int day) {
        this.daysOfMonth = daysOfMonth;
        this.activeDates = activeDates;
        this.currentYear = year;
        this.currentMonth = month;
        this.currentDay = day;

        Calendar todayCal = Calendar.getInstance();
        this.isCurrentDisplayMonth = (year == todayCal.get(Calendar.YEAR)) && (month == todayCal.get(Calendar.MONTH));
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
            holder.tvDayNumber.setVisibility(View.INVISIBLE);
        } else {
            holder.tvDayNumber.setVisibility(View.VISIBLE);
            holder.tvDayNumber.setText(dayText);

            // Reset ke style default
            holder.tvDayNumber.setBackgroundResource(android.R.color.transparent);
            holder.tvDayNumber.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.text_secondary));
            holder.tvDayNumber.setTypeface(null, Typeface.NORMAL);

            String fullDate = String.format(Locale.US, "%d-%02d-%02d", currentYear, currentMonth + 1, Integer.parseInt(dayText));
            boolean isActive = activeDates.contains(fullDate);
            boolean isToday = isCurrentDisplayMonth && Integer.parseInt(dayText) == currentDay;

            // Terapkan style berdasarkan kondisi, prioritaskan hari ini
            if (isToday) {
                holder.tvDayNumber.setBackgroundResource(R.drawable.calendar_day_today_bg);
                holder.tvDayNumber.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                holder.tvDayNumber.setTypeface(null, Typeface.BOLD);
            }

            // Timpa dengan style aktif jika hari tersebut aktif
            if (isActive) {
                // GUNAKAN DRAWABLE BARU DI SINI
                holder.tvDayNumber.setBackgroundResource(R.drawable.calendar_active_day_bg);
                holder.tvDayNumber.setTextColor(Color.WHITE);
                holder.tvDayNumber.setTypeface(null, Typeface.BOLD);
            }
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDayNumber;

        ViewHolder(View itemView) {
            super(itemView);
            tvDayNumber = itemView.findViewById(R.id.tvDayNumber);
        }
    }
}