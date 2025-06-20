package com.example.gesturelearn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gesturelearn.R;
import com.example.gesturelearn.model.Sign;
import java.util.List;

public class AbjadSibiAdapter extends RecyclerView.Adapter<AbjadSibiAdapter.ViewHolder> {

    private final List<Sign> signList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Sign sign);
    }

    public AbjadSibiAdapter(List<Sign> signList, OnItemClickListener listener) {
        this.signList = signList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Menggunakan item_abjad_sibi.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_abjad_sibi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(signList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return signList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAbjad;
        ViewHolder(View itemView) {
            super(itemView);
            tvAbjad = itemView.findViewById(R.id.tv_abjad);
        }
        void bind(final Sign sign, final OnItemClickListener listener) {
            tvAbjad.setText(sign.word);
            itemView.setOnClickListener(v -> listener.onItemClick(sign));
        }
    }
}