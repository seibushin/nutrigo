package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodPortion;

public class FoodViewModel extends AndroidViewModel {
    private Repo repo;

    private LiveData<List<FoodPortion>> allFood;

    public FoodViewModel (Application application) {
        super(application);
        repo = new Repo(application);
        allFood = repo.getAllFood();
    }

    public LiveData<List<FoodPortion>> getAllFood() {
        return allFood;
    }

    public void insert(Food food) {
        repo.insertFood(food);
    }

    public void delete(Food food) {
        repo.deleteFood(food);
    }

}
