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
import com.example.gesturelearn.adapter.AbjadBisindoAdapter;

public class LearnAbjadBisindoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_abjad_bisindo);

        RecyclerView rvBisindo = findViewById(R.id.rv_bisindo);
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        List<Sign> signList = AppDatabase.getDatabase(this).signDao().getSignsByCategory("ABJAD_BISINDO");

        AbjadBisindoAdapter adapter = new AbjadBisindoAdapter(signList, sign -> {
            Intent intent = new Intent(LearnAbjadBisindoActivity.this, LearnDetailActivity.class);
            intent.putExtra(LearnDetailActivity.EXTRA_WORD, sign.word);
            intent.putExtra(LearnDetailActivity.EXTRA_GIF_URL, sign.gifUrl);
            intent.putExtra(LearnDetailActivity.EXTRA_CATEGORY, "ABJAD_BISINDO");
            startActivity(intent);
        });

        rvBisindo.setLayoutManager(new GridLayoutManager(this, 3));
        rvBisindo.setAdapter(adapter);
    }
}