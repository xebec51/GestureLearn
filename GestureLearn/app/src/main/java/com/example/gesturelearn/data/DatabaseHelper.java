package com.example.gesturelearn.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nama database
    public static final String DATABASE_NAME = "GestureLearn.db";
    // Versi database. Naikkan jika ada perubahan skema
    private static final int DATABASE_VERSION = 1;

    // Nama tabel
    public static final String TABLE_USERS = "users";

    // Kolom-kolom tabel users
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // SQL statement untuk membuat tabel users
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT UNIQUE,"
            + COLUMN_PASSWORD + " TEXT"
            + ")";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Eksekusi perintah SQL untuk membuat tabel
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Jika tabel sudah ada, hapus (untuk skenario upgrade sederhana)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Buat tabel lagi
        onCreate(db);
    }

    /**
     * Metode untuk menambahkan user baru ke database.
     * @param name Nama user
     * @param email Email user
     * @param password Password user
     * @return true jika berhasil, false jika gagal
     */
    public boolean addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password); // Di aplikasi nyata, password harus di-hash!

        // Insert baris baru
        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        // Jika result = -1, berarti terjadi error
        return result != -1;
    }

    /**
     * Metode untuk memeriksa apakah email sudah terdaftar.
     * @param email Email yang akan dicek
     * @return true jika email sudah ada, false jika belum
     */
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + " = ?",
                new String[]{email},
                null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    /**
     * Metode untuk memeriksa kredensial user saat login.
     * @param email Email user
     * @param password Password user
     * @return true jika email dan password cocok, false jika tidak
     */
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }
}