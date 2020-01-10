package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodDay;

public class DayFoodViewModel extends AndroidViewModel {
    private Repo repo;

    private LiveData<List<FoodDay>> dayFood;
    private LiveData<List<Long>> days;

    public DayFoodViewModel(Application application) {
        super(application);
        repo = new Repo(application);
        dayFood = repo.getDayFood();
        days = repo.getDaysFood();
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

    public void update(FoodDay food, double serving) {
        repo.updateDayFood(food, serving);
    }

    public LiveData<List<Long>> getDays() {
        return days;
    }

}
