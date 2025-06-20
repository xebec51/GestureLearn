package com.example.gesturelearn.activity.learn;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.gesturelearn.R;

public class LearnDetailActivity extends AppCompatActivity {

    public static final String EXTRA_WORD = "extra_word";
    public static final String EXTRA_GIF_URL = "extra_gif_url";
    public static final String EXTRA_CATEGORY = "extra_category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_detail);

        ImageView ivGif = findViewById(R.id.iv_detail_gif);
        TextView tvWord = findViewById(R.id.tv_detail_word);
        Button btnBack = findViewById(R.id.btn_detail_back);
        // Anda bisa menambahkan referensi ke title dan banner jika ingin mengubahnya juga
        // TextView tvTitle = findViewById(R.id.tv_detail_title);

        String word = getIntent().getStringExtra(EXTRA_WORD);
        String gifUrl = getIntent().getStringExtra(EXTRA_GIF_URL);
        // String category = getIntent().getStringExtra(EXTRA_CATEGORY);

        tvWord.setText(word);
        Glide.with(this).asGif().load(gifUrl).into(ivGif);

        btnBack.setOnClickListener(v -> finish());
    }
}