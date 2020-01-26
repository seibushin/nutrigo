package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import java.util.ArrayList;
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
        dayMeal = repo.getDayMeal();
        return dayMeal;
    }

    public List<MealDay> getServedMeals() {
        List<MealDay> meals = new ArrayList<>();
        List<MealDay> current = dayMeal.getValue();
        if (current != null) {
            meals.addAll(current);
        }

        meals.forEach(meal -> {
            meal.meal.foods.forEach(food -> {
                food.portionize = true;
                food.served = repo.getMealXFoodServing(meal.getId(), food.getId());
            });
        });

        return new ArrayList<>(meals);
    }

    public MealDay insert(Meal meal) {
        return repo.insertDayMeal(meal);
    }

    public MealDay insertDayMeal(MealDay meal) {
        return repo.insertDayMeal(meal);
    }

    public void delete(MealDay meal) {
        repo.deleteDayMeal(meal);
    }

    public void update(MealDay meal, double serving) {
        repo.updateDayMeal(meal, serving);
    }

    public void update(MealDay meal) {
        repo.updateDayMeal(meal);
    }

    public LiveData<List<Long>> getDays() {
        return days;
    }

}
