package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodPortion;
import de.seibushin.nutrigo.model.nutrition.Meal;

public class MealViewModel extends AndroidViewModel {
    private Repo repo;

    private LiveData<List<Meal>> allMeal;

    public MealViewModel(Application application) {
        super(application);
        repo = new Repo(application);
        allMeal = repo.getAllMeal();
    }

    public LiveData<List<Meal>> getAllMeal() {
        return allMeal;
    }

    public void insert(Meal meal) {
        repo.insertMeal(meal);
    }

    public void delete(Meal meal) {
        repo.deleteMeal(meal);
    }

}
