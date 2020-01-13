package de.seibushin.nutrigo.viewmodel;

import android.app.Application;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import de.seibushin.nutrigo.dao.MealXFood;
import de.seibushin.nutrigo.model.nutrition.Meal;
import de.seibushin.nutrigo.model.nutrition.NutritionUnit;

public class MealViewModel extends AndroidViewModel {
    private Repo repo;

    private LiveData<List<Meal>> allMeal;
    private LiveData<List<MealXFood>> servings;

    public MealViewModel(Application application) {
        super(application);
        repo = new Repo(application);
        allMeal = repo.getAllMeal();
        servings = repo.getServings();
    }

    public LiveData<List<Meal>> getAllMeal() {
        return allMeal;
    }

    public LiveData<List<MealXFood>> getServing() {
        return servings;
    }

    public List<NutritionUnit> getServedMeals() {
        List<Meal> meals = new ArrayList<>(allMeal.getValue());

        meals.forEach(meal -> {
            meal.foods.forEach(food -> {
                food.portionize = true;
                food.served = repo.getMealXFoodServing(meal.getId(), food.getId());
                System.out.println(food);
            });
        });

        return new ArrayList<>(meals);
    }

    public void insert(Meal meal) {
        repo.insertMeal(meal);
    }

    public void delete(Meal meal) {
        repo.deleteMeal(meal);
    }

}
