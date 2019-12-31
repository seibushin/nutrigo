package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import de.seibushin.nutrigo.Nutrigo;
import de.seibushin.nutrigo.dao.DayFoodDao;
import de.seibushin.nutrigo.dao.FoodDao;
import de.seibushin.nutrigo.dao.DayFood;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodPortion;

class Repo {
    private FoodDao foodDao;
    private DayFoodDao dayFoodDao;
    private LiveData<List<Food>> allFood;
    private LiveData<List<FoodPortion>> dayFood;

    Repo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        foodDao = db.foodDao();
        allFood = foodDao.getAll();

        dayFoodDao = db.dayFoodDao();
        dayFood = dayFoodDao.getFoods(Nutrigo.selectedDay);
    }

    LiveData<List<Food>> getAllFood() {
        return allFood;
    }

    void insertFood(Food food) {
        AppDatabase.writeExecutor.execute(() -> foodDao.insert(food));
    }

    void deleteFood(Food food) {
        AppDatabase.writeExecutor.execute(() -> foodDao.delete(food));
    }

    LiveData<List<FoodPortion>> getDayFood() {
        return dayFood;
    }

    DayFood insertDayFood(Food food) {
        DayFood df = new DayFood();
        df.date = Nutrigo.selectedDay;
        df.fid = food.getId();
        df.timestamp = System.currentTimeMillis();
        df.serving = food.getPortion();

        AtomicInteger id = new AtomicInteger();
        try {
            AppDatabase.writeExecutor.submit(() -> {
                id.set((int) dayFoodDao.insert(df));
            }).get();

            df.id = id.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return df;
    }

    void deleteDayFood(DayFood dayFood) {
        AppDatabase.writeExecutor.execute(() -> dayFoodDao.delete(dayFood));
    }
}
