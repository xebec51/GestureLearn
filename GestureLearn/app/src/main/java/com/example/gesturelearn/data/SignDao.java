package com.example.gesturelearn.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.gesturelearn.model.Sign;
import java.util.List;

@Dao
public interface SignDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Sign> signs);

    @Query("SELECT COUNT(*) FROM signs")
    int getCount();

    @Query("SELECT * FROM signs WHERE category = :category")
    List<Sign> getSignsByCategory(String category);

    @Query("SELECT * FROM signs")
    List<Sign> getAllSigns();
}