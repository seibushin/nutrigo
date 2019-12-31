package de.seibushin.nutrigo.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.seibushin.nutrigo.model.nutrition.FoodDay;

@Dao
public interface DayFoodDao {
    @Insert
    long insert(DayFood dayFood);

    @Query("SELECT food.*, dayfood.serving, dayfood.fdID, dayfood.date, dayfood.timestamp FROM food" +
            " INNER JOIN dayfood on food.id = dayfood.fid" +
            " WHERE dayfood.date = :day")
    LiveData<List<FoodDay>> getFoods(long day);

    @Query("SELECT date FROM dayfood" +
            " GROUP BY date")
    List<Long> getDays();

    @Insert
    void insertAll(DayFood... dayFoods);

    @Query("DELETE FROM dayfood WHERE dayfood.fdID = :id")
    void delete(int id);
}
