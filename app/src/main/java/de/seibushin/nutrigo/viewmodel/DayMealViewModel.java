package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import de.seibushin.nutrigo.model.nutrition.Meal;
import de.seibushin.nutrigo.model.nutrition.MealDay;

public class DayMealViewModel extends AndroidViewModel {
    private Repo repo;

    private LiveData<List<MealDay>> dayMeal;
    private LiveData<List<Long>> days;

    public DayMealViewModel(Application application) {
        super(application);
        repo = new Repo(application);
        dayMeal = repo.getDayMeal();
        days = repo.getDaysMeal();
    }

    public LiveData<List<MealDay>> getDayMeal() {
        return dayMeal;
    }

    public MealDay insert(Meal meal) {
        return repo.insertDayMeal(meal);
    }

    public MealDay insertDayFood(MealDay meal) {
        return repo.insertDayMeal(meal);
    }

    public void delete(MealDay meal) {
        repo.deleteDayMeal(meal);
    }

    public void update(MealDay meal, double serving) {
        repo.updateDayFood(meal, serving);
    }

    public LiveData<List<Long>> getDays() {
        return days;
    }

}
