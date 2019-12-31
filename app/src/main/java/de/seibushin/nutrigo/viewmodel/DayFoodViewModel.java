package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.seibushin.nutrigo.dao.DayFood;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodDay;

public class DayFoodViewModel extends AndroidViewModel {
    private Repo repo;

    private LiveData<List<FoodDay>> dayFood;

    public DayFoodViewModel(Application application) {
        super(application);
        repo = new Repo(application);
        dayFood = repo.getDayFood();
    }

    public LiveData<List<FoodDay>> getDayFood() {
        return dayFood;
    }

    public FoodDay insert(Food food) {
        return repo.insertDayFood(food);
    }

    public FoodDay insertDayFood(FoodDay food) {
        return repo.insertDayFood(food);
    }

    public void delete(FoodDay food) {
        repo.deleteDayFood(food);
    }

}
