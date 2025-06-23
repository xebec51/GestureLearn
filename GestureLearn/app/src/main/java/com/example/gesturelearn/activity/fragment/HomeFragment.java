package com.example.gesturelearn.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gesturelearn.R;
import com.example.gesturelearn.adapter.HomeCardAdapter;
import com.example.gesturelearn.data.DatabaseHelper;
import com.example.gesturelearn.model.CardItem;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeCardAdapter adapter;
    private List<CardItem> cardItems;
    private TextView tvName;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gunakan layout yang sudah Anda buat
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi DatabaseHelper
        databaseHelper = new DatabaseHelper(getContext());

        // Inisialisasi Views
        tvName = view.findViewById(R.id.tv_name);
        recyclerView = view.findViewById(R.id.rv_home_cards);

        // Memuat data pengguna dan setup RecyclerView
        loadUserData();
        setupRecyclerView();
    }

    private void loadUserData() {
        // Ambil email dari intent yang dikirim ke MainActivity
        if (getActivity() != null && getActivity().getIntent() != null) {
            String userEmail = getActivity().getIntent().getStringExtra("USER_EMAIL");
            if (userEmail != null) {
                // Ambil nama dari database menggunakan email
                String userName = databaseHelper.getUserName(userEmail); // Anda perlu membuat metode ini di DatabaseHelper
                if (userName != null && !userName.isEmpty()) {
                    tvName.setText("Hi, " + userName + "!");
                }
            }
        }
    }

    private void setupRecyclerView() {
        cardItems = new ArrayList<>();
        // Tambahkan data untuk setiap kartu di sini
        cardItems.add(new CardItem("ABJAD BISINDO", "Pelajari huruf dengan pendekatan alami dan ekspresif khas BISINDO.", R.drawable.abjadbisindo));
        cardItems.add(new CardItem("ABJAD SIBI", "Kenali huruf dari Sistem Isyarat Bahasa Indonesia (SIBI) yang terstruktur.", R.drawable.abjadsibi));
        cardItems.add(new CardItem("KOSAKATA", "Perluas perbendaharaan kata isyarat Anda untuk komunikasi sehari-hari.", R.drawable.imgkosakata));

        // Setup Adapter
        adapter = new HomeCardAdapter(getContext(), cardItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}