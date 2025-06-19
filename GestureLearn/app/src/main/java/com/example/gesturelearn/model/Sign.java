package com.example.gesturelearn.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "signs")
public class Sign {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String word;
    public String gifUrl;
    public String category; // Kategori: KOSAKATA, ABJAD_SIBI, ABJAD_BISINDO

    // Room memerlukan constructor kosong jika ada constructor lain
    public Sign() {}

    public Sign(String word, String gifUrl, String category) {
        this.word = word;
        this.gifUrl = gifUrl;
        this.category = category;
    }
}