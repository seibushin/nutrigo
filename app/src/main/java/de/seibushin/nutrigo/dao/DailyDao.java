package de.seibushin.nutrigo.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import de.seibushin.nutrigo.model.Daily;

@Dao
public interface DailyDao {
    @Query("SELECT timestamp, sum(kcal) as kcal, sum(fat) as fat, sum(carbs) as carbs, sum(sugar) as sugar, sum(protein) as protein" +
            " FROM (" +
            " SELECT date as timestamp, sum(serving * kcal / weight) as kcal, sum(serving * fat / weight) as fat, sum(serving * carbs / weight) as carbs, sum(serving * sugar / weight) as sugar, sum(serving * protein / weight) as protein" +
            " FROM dayfood" +
            " INNER JOIN FOOD on dayfood.fid = food.id" +
            " GROUP BY date" +
            " UNION" +
            " SELECT date as timestamp, sum(daymeal.serving * meals.kcal) as kcal, sum(daymeal.serving * meals.fat) as fat, sum(daymeal.serving * meals.carbs) as carbs, sum(daymeal.serving * meals.sugar) as sugar, sum(daymeal.serving * meals.protein) as protein" +
            " FROM daymeal," +
            " (SELECT mealId, sum(mealxfood.serving * food.kcal / food.weight) as kcal, sum(mealxfood.serving * food.fat / food.weight) as fat, sum(mealxfood.serving * food.carbs / food.weight) as carbs, sum(mealxfood.serving * food.sugar / food.weight) as sugar, sum(mealxfood.serving * food.protein / food.weight) as protein" +
            " FROM mealxfood" +
            " INNER JOIN food on mealxfood.foodId = food.id" +
            " GROUP BY mealId) as meals" +
            " WHERE daymeal.mid = meals.mealId" +
            " GROUP BY date)" +
            " GROUP BY timestamp" +
            " ORDER BY timestamp")
    LiveData<List<Daily>> getDaily();
}
