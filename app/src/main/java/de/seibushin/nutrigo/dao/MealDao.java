package de.seibushin.nutrigo.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import de.seibushin.nutrigo.model.nutrition.Meal;
import de.seibushin.nutrigo.model.nutrition.MealX;

@Dao
public interface MealDao {
    @Query("SELECT * FROM meal")
    LiveData<List<Meal>> getAll();

    @Transaction
    @Query("SELECT meal.*, food.*, mealFood.serving FROM meal" +
            " INNER JOIN mealFood on meal.id = mealfood.mealId" +
            " INNER JOIN food on food.id = mealFood.foodId")
    LiveData<List<MealX>> getAll2();

    @Insert
    void insertAll(Meal... meals);

    @Insert
    long insert(Meal meal);

    @Delete
    void delete(Meal meal);

    @Update
    void update(Meal meal);

}
