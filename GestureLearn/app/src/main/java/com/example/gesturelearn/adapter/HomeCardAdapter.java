package com.example.gesturelearn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gesturelearn.R;
import com.example.gesturelearn.model.CardItem;
import java.util.List;

public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardAdapter.CardViewHolder> {

    private final List<CardItem> cardItems;
    private final Context context;

    public HomeCardAdapter(Context context, List<CardItem> cardItems) {
        this.context = context;
        this.cardItems = cardItems;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem currentItem = cardItems.get(position);

        holder.imageView.setImageResource(currentItem.getImageResId());
        holder.titleButton.setText(currentItem.getTitle());
        holder.descriptionTextView.setText(currentItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Button titleButton;
        TextView descriptionTextView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_example);
            titleButton = itemView.findViewById(R.id.btn_bisindoLetter);
            // Anda perlu menambahkan id ke TextView deskripsi di item_card.xml
            // Misalnya: android:id="@+id/tv_description"
            descriptionTextView = itemView.findViewById(R.id.tv_description);
        }
    }
}