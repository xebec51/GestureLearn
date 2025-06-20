package com.example.gesturelearn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gesturelearn.R;
import com.example.gesturelearn.model.Sign;
import java.util.List;

public class KosakataAdapter extends RecyclerView.Adapter<KosakataAdapter.ViewHolder> {

    private final List<Sign> signList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Sign sign);
    }

    public KosakataAdapter(List<Sign> signList, OnItemClickListener listener) {
        this.signList = signList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kosakata, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sign currentSign = signList.get(position);
        holder.bind(currentSign, listener);
    }

    @Override
    public int getItemCount() {
        return signList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKosakata;

        ViewHolder(View itemView) {
            super(itemView);
            tvKosakata = itemView.findViewById(R.id.tv_kosakata);
        }

        void bind(final Sign sign, final OnItemClickListener listener) {
            tvKosakata.setText(sign.word);
            itemView.setOnClickListener(v -> listener.onItemClick(sign));
        }
    }
}