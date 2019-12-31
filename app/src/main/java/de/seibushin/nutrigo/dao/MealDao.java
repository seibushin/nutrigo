package de.seibushin.nutrigo.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.seibushin.nutrigo.model.nutrition.Food;

@Dao
public interface MealDao {
    @Query("SELECT * FROM meal")
    LiveData<List<Food>> getAll();

    @Insert
    void insertAll(Food... foods);

    @Insert
    void insert(Food food);

    @Delete
    void delete(Food food);

    @Update
    void update(Food food);

}
