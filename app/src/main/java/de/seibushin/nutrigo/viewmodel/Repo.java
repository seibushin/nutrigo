package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.seibushin.nutrigo.dao.FoodDao;
import de.seibushin.nutrigo.model.nutrition.Food;

class Repo {
    private FoodDao foodDao;
    private LiveData<List<Food>> allFood;

    Repo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        foodDao = db.foodDao();
        allFood = foodDao.getAll();
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
}
