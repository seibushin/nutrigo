package de.seibushin.nutrigo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MealFoodDao {
    @Insert
    long insert(MealFood mealFood);

    @Query("DELETE FROM mealFood WHERE mealfood.mealId = :mealid")
    void delete(int mealid);
}
