package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.seibushin.nutrigo.dao.DayFood;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodPortion;

public class DayFoodViewModel extends AndroidViewModel {
    private Repo repo;

    private LiveData<List<FoodPortion>> dayFood;

    public DayFoodViewModel(Application application) {
        super(application);
        repo = new Repo(application);
        dayFood = repo.getDayFood();
    }

    public LiveData<List<FoodPortion>> getDayFood() {
        return dayFood;
    }

    public DayFood insert(Food food) {
        return repo.insertDayFood(food);
    }

    public void delete(DayFood dayFood) {
        repo.deleteDayFood(dayFood);
    }

}
