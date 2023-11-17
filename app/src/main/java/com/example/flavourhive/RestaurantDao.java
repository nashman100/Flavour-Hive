package com.example.flavourhive;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RestaurantEntity restaurant);

    @Query("SELECT * FROM restaurants")
    LiveData<List<RestaurantEntity>> getAllRestaurants();

    @Delete
    void delete(RestaurantEntity restaurant);

    @Update
    void update(RestaurantEntity restaurant);

    @Query("SELECT * FROM restaurants WHERE type = :type")
    List<RestaurantEntity> findRestaurantsByType(String type);

    @Query("DELETE FROM restaurants WHERE name = :restaurantName")
    void deleteByName(String restaurantName);
}


