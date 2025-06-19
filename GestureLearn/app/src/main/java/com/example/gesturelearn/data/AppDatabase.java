package com.example.gesturelearn.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.gesturelearn.model.Sign;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Sign.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SignDao signDao();

    private static volatile AppDatabase INSTANCE;
    private static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    // --- PERUBAHAN DIMULAI DI SINI ---
    // Callback sekarang didefinisikan di dalam metode getDatabase agar bisa mengakses 'context'
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "gesturelearn_database")
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    databaseWriteExecutor.execute(() -> {
                                        // Langsung gunakan 'context' dari parameter getDatabase()
                                        populateDatabase(context, getDatabase(context).signDao());
                                    });
                                }
                            })
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void populateDatabase(Context context, SignDao dao) {
        if (dao.getCount() > 0) return;

        List<Sign> allSigns = new ArrayList<>();
        allSigns.addAll(loadSignsFromFile(context, "kosakata_bisindo.txt", "KOSAKATA"));
        allSigns.addAll(loadSignsFromFile(context, "abjad_sibi.txt", "ABJAD_SIBI"));
        allSigns.addAll(loadSignsFromFile(context, "abjad_bisindo.txt", "ABJAD_BISINDO"));

        dao.insertAll(allSigns);
    }
    // --- AKHIR PERUBAHAN ---

    private static List<Sign> loadSignsFromFile(Context context, String fileName, String category) {
        List<Sign> signs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    // Pastikan tidak ada spasi ekstra
                    signs.add(new Sign(parts[0].trim(), parts[1].trim(), category));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return signs;
    }
}