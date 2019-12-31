package de.seibushin.nutrigo.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.seibushin.nutrigo.model.nutrition.FoodPortion;

@Dao
public interface DayFoodDao {
    @Insert
    long insert(DayFood dayFood);

    @Query("SELECT food.*, dayfood.serving FROM food" +
            " INNER JOIN dayfood on food.id = dayfood.fid" +
            " WHERE dayfood.date = :day")
    LiveData<List<FoodPortion>> getFoods(long day);

    @Query("SELECT date FROM dayfood" +
            " GROUP BY date")
    List<Long> getDays();

    @Insert
    void insertAll(DayFood... dayFoods);

    @Delete
    void delete(DayFood dayFood);
}
