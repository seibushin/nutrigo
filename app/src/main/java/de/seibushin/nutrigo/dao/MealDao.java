package de.seibushin.nutrigo.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.Meal;
import de.seibushin.nutrigo.model.nutrition.MealInfo;

@Dao
public interface MealDao {
    @Transaction
    @Query("SELECT * FROM mealinfo")
    LiveData<List<Meal>> getAll();

    @Query("SELECT * FROM mealxfood")
    LiveData<List<MealXFood>> getServings();

    @Query("SELECT serving FROM mealxfood WHERE mealId = :mealId AND foodId = :foodId")
    double getMealXFoodServing(int mealId, int foodId);

    @Insert
    long insert(MealInfo meal);

    @Insert
    long insert(MealXFood mealXFood);

    @Delete
    void delete(MealInfo meal);

    @Query("DELETE FROM mealxfood WHERE mealId = :mealId")
    void delete(int mealId);

    @Update
    void update(MealInfo meal);

}
