package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import de.seibushin.nutrigo.model.nutrition.Food;

public class FoodViewModel extends AndroidViewModel {
    private Repo repo;

    private LiveData<List<Food>> allFood;

    public FoodViewModel(Application application) {
        super(application);
        repo = new Repo(application);
        allFood = repo.getAllFood();
    }

    public LiveData<List<Food>> getAllFood() {
        return allFood;
    }

    public void insert(Food food) {
        repo.insertFood(food);
    }

    public void delete(Food food) {
        repo.deleteFood(food);
    }

    public void update(Food food) {
        repo.updateFood(food);
    }
}
