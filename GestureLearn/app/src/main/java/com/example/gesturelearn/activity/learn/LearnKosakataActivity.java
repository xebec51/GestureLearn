package com.example.gesturelearn.activity.learn;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gesturelearn.R;
import com.example.gesturelearn.adapter.KosakataAdapter;
import com.example.gesturelearn.data.AppDatabase;
import com.example.gesturelearn.model.Sign;
import java.util.List;

public class LearnKosakataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_kosakata); // Gunakan layout yang Anda berikan

        RecyclerView rvKosakata = findViewById(R.id.rv_kosakata);
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        List<Sign> signList = AppDatabase.getDatabase(this).signDao().getSignsByCategory("KOSAKATA");

        KosakataAdapter adapter = new KosakataAdapter(signList, sign -> {
            Intent intent = new Intent(LearnKosakataActivity.this, LearnDetailActivity.class);
            intent.putExtra(LearnDetailActivity.EXTRA_WORD, sign.word);
            intent.putExtra(LearnDetailActivity.EXTRA_GIF_URL, sign.gifUrl);
            intent.putExtra(LearnDetailActivity.EXTRA_CATEGORY, "KOSAKATA");
            startActivity(intent);
        });

        rvKosakata.setLayoutManager(new GridLayoutManager(this, 2));
        rvKosakata.setAdapter(adapter);
    }
}