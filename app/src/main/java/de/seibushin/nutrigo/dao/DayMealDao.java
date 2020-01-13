package de.seibushin.nutrigo.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import de.seibushin.nutrigo.model.nutrition.MealDay;

@Dao
public interface DayMealDao {
    @Insert
    long insert(DayMeal dayMeal);

    @Transaction
    @Query("SELECT mealinfo.*, daymeal.serving, daymeal.mdID, daymeal.date, daymeal.timestamp FROM mealinfo" +
            " INNER JOIN daymeal on mealinfo.id = daymeal.mid" +
            " WHERE daymeal.date = :day")
    LiveData<List<MealDay>> getMeals(long day);

    @Query("SELECT date FROM daymeal" +
            " GROUP BY date")
    LiveData<List<Long>> getDays();

    @Insert
    void insertAll(DayMeal... dayMeals);

    @Query("DELETE FROM daymeal WHERE daymeal.mdID = :id")
    void delete(int id);

    @Query("UPDATE daymeal SET serving = :serving WHERE daymeal.mdID = :fdID")
    void update(int fdID, double serving);
}
