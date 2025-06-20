package com.example.gesturelearn.activity.learn;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gesturelearn.R;
import com.example.gesturelearn.adapter.AbjadSibiAdapter;
import com.example.gesturelearn.data.AppDatabase;
import com.example.gesturelearn.model.Sign;
import java.util.List;

public class LearnAbjadSibiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_abjad_sibi);

        RecyclerView rvSibi = findViewById(R.id.rv_sibi);
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        List<Sign> signList = AppDatabase.getDatabase(this).signDao().getSignsByCategory("ABJAD_SIBI");

        AbjadSibiAdapter adapter = new AbjadSibiAdapter(signList, sign -> {
            Intent intent = new Intent(LearnAbjadSibiActivity.this, LearnDetailActivity.class);
            intent.putExtra(LearnDetailActivity.EXTRA_WORD, sign.word);
            intent.putExtra(LearnDetailActivity.EXTRA_GIF_URL, sign.gifUrl);
            intent.putExtra(LearnDetailActivity.EXTRA_CATEGORY, "ABJAD_SIBI");
            startActivity(intent);
        });

        rvSibi.setLayoutManager(new GridLayoutManager(this, 3));
        rvSibi.setAdapter(adapter);
    }
}