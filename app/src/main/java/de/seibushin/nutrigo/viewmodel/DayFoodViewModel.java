package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import de.seibushin.nutrigo.model.Daily;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodDay;

public class DayFoodViewModel extends AndroidViewModel {
    private Repo repo;

    private LiveData<List<FoodDay>> dayFood;
    private LiveData<List<Long>> days;
    private LiveData<List<Daily>> daily;

    public DayFoodViewModel(Application application) {
        super(application);
        repo = new Repo(application);
        dayFood = repo.getDayFood();
        days = repo.getDaysFood();
        daily = repo.getDaily();
    }

    public LiveData<List<FoodDay>> getDayFood() {
        // this might cause some issues with false nutrition updates on the day fragment
        // but this is required to have the correct day
        dayFood = repo.getDayFood();
        return dayFood;
    }

    public List<FoodDay> getCurrentDayFood() {
        List<FoodDay> food = new ArrayList<>();
        List<FoodDay> current = dayFood.getValue();
        if (current != null) {
            food.addAll(current);
        }
        return food;
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

    public void update(FoodDay food) {
        repo.updateDayFood(food);
    }

    public LiveData<List<Long>> getDays() {
        return days;
    }

    public LiveData<List<Daily>> getDaily() {
        return daily;
    }

    public FoodDay insertClone(FoodDay food) {
        return repo.insertFoodClone(food);
    }
}
