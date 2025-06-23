package com.example.gesturelearn.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GestureLearn.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_USERS = "users";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_POINTS = "points";
    public static final String COLUMN_PROFILE_PHOTO_URI = "profile_photo_uri";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT UNIQUE,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_POINTS + " INTEGER DEFAULT 0,"
            + COLUMN_PROFILE_PHOTO_URI + " TEXT"
            + ")";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_POINTS, 0);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public int getUserPoints(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_POINTS},
                COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int points = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POINTS));
            cursor.close();
            return points;
        }
        if (cursor != null) {
            cursor.close();
        }
        return 0;
    }

    public boolean updateUserPoints(String email, int pointsToAdd) {
        SQLiteDatabase db = this.getWritableDatabase();
        int currentPoints = getUserPoints(email);
        int newPoints = currentPoints + pointsToAdd;

        ContentValues values = new ContentValues();
        values.put(COLUMN_POINTS, newPoints);

        int result = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{email});
        return result > 0;
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + " = ?",
                new String[]{email},
                null, null, null);

        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

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
        return count > 0;
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_NAME},
                COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            cursor.close();
            return name;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public boolean updateUser(String email, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newName);

        int result = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{email});
        return result > 0;
    }

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int result = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{email});
        return result > 0;
    }

    public void updateProfilePhotoUri(String email, String uri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILE_PHOTO_URI, uri);
        db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{email});
    }

    public String getProfilePhotoUri(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_PROFILE_PHOTO_URI},
                COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String uri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_PHOTO_URI));
            cursor.close();
            return uri;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }
}