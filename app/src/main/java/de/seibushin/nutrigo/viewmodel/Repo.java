package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import de.seibushin.nutrigo.Nutrigo;
import de.seibushin.nutrigo.dao.DayFood;
import de.seibushin.nutrigo.dao.DayFoodDao;
import de.seibushin.nutrigo.dao.DayMeal;
import de.seibushin.nutrigo.dao.DayMealDao;
import de.seibushin.nutrigo.dao.FoodDao;
import de.seibushin.nutrigo.dao.MealDao;
import de.seibushin.nutrigo.dao.MealFood;
import de.seibushin.nutrigo.dao.MealFoodDao;
import de.seibushin.nutrigo.dao.ProfileDao;
import de.seibushin.nutrigo.model.Profile;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodDay;
import de.seibushin.nutrigo.model.nutrition.FoodPortion;
import de.seibushin.nutrigo.model.nutrition.Meal;
import de.seibushin.nutrigo.model.nutrition.MealDay;

class Repo {
    private FoodDao foodDao;
    private DayFoodDao dayFoodDao;
    private LiveData<List<FoodPortion>> allFood;
    private LiveData<List<FoodDay>> dayFood;
    private ProfileDao profileDao;
    private LiveData<Profile> profile;
    private LiveData<List<Long>> daysFood;
    private MealDao mealDao;
    private DayMealDao dayMealDao;
    private LiveData<List<Meal>> allMeal;
    private LiveData<List<MealDay>> dayMeal;
    private LiveData<List<Long>> daysMeal;

    private MealFoodDao mealFoodDao;



    Repo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        foodDao = db.foodDao();
        allFood = foodDao.getAll();

        dayFoodDao = db.dayFoodDao();
        dayFood = dayFoodDao.getFoods(Nutrigo.selectedDay);
        daysFood = dayFoodDao.getDays();

        profileDao = db.profileDao();
        profile = profileDao.getProfile();

        mealDao = db.mealDao();
        allMeal = mealDao.getAll();

        dayMealDao = db.dayMealDao();
        dayMeal = dayMealDao.getMeals(Nutrigo.selectedDay);
        daysMeal = dayMealDao.getDays();

        mealFoodDao = db.mealFoodDao();
    }

    /*
     * Food Section
     */

    LiveData<List<FoodPortion>> getAllFood() {
        return allFood;
    }

    void insertFood(Food food) {
        AppDatabase.writeExecutor.execute(() -> foodDao.insert(food));
    }

    void deleteFood(Food food) {
        AppDatabase.writeExecutor.execute(() -> foodDao.delete(food));
    }

    /*
     * Day Food Section
     */

    LiveData<List<FoodDay>> getDayFood() {
        return dayFood;
    }

    FoodDay insertDayFood(FoodDay food) {
        DayFood df = new DayFood();
        df.date = food.date;
        df.fid = food.food.getId();
        df.timestamp = food.timestamp;
        df.serving = food.serving;
        df.fdID = food.fdID;

        AppDatabase.writeExecutor.submit(() -> {
           dayFoodDao.insert(df);
        });

        return food;
    }

    FoodDay insertDayFood(Food food) {
        DayFood df = new DayFood();
        df.date = Nutrigo.selectedDay;
        df.fid = food.getId();
        df.timestamp = System.currentTimeMillis();
        df.serving = food.getPortion();

        AtomicInteger id = new AtomicInteger();
        try {
            AppDatabase.writeExecutor.submit(() -> {
                id.set((int) dayFoodDao.insert(df));
            }).get();

            df.fdID = id.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FoodDay fd = new FoodDay();
        fd.food = food;
        fd.date = df.date;
        fd.fdID = df.fdID;
        fd.serving = df.serving;
        fd.timestamp = df.timestamp;

        return fd;
    }

    LiveData<List<Long>> getDaysFood() {
        return daysFood;
    }

    void deleteDayFood(FoodDay food) {
        AppDatabase.writeExecutor.execute(() -> dayFoodDao.delete(food.fdID));
    }

    void updateDayFood(FoodDay food, double serving) {
        AppDatabase.writeExecutor.execute(() -> dayFoodDao.update(food.fdID, serving));
    }

    /*
     * Profile Section
     */

    LiveData<Profile> getProfile() {
        return profile;
    }

    void updateProfile(Profile profile) {
        AppDatabase.writeExecutor.execute(()-> profileDao.update(profile));
    }

    /*
     * Meal Section
     */

    LiveData<List<Meal>> getAllMeal() {
        return allMeal;
    }

    void insertMeal(Meal meal) {
        AppDatabase.writeExecutor.execute(() -> {
            int mealId = (int) mealDao.insert(meal);
            meal.getFoods().forEach(food -> {
                MealFood mf = new MealFood(mealId, food.getId(), food.getPortion());
                System.out.println("MEALFOOD " + mf);
                mealFoodDao.insert(mf);
            });
        });
    }

    void deleteMeal(Meal meal) {
        AppDatabase.writeExecutor.execute(() -> mealDao.delete(meal));
    }

    /*
     * Day Meal Section
     */

    LiveData<List<MealDay>> getDayMeal() {
        return dayMeal;
    }

    MealDay insertDayMeal(MealDay meal) {
        DayMeal df = new DayMeal();
        df.date = meal.date;
        df.mid = meal.meal.getId();
        df.timestamp = meal.timestamp;
        df.serving = meal.serving;
        df.mdID = meal.mdID;

        AppDatabase.writeExecutor.submit(() -> {
            dayMealDao.insert(df);
        });

        return meal;
    }

    MealDay insertDayFood(Meal meal) {
        DayMeal df = new DayMeal();
        df.date = Nutrigo.selectedDay;
        df.mid = meal.getId();
        df.timestamp = System.currentTimeMillis();
        df.serving = meal.getPortion();

        AtomicInteger id = new AtomicInteger();
        try {
            AppDatabase.writeExecutor.submit(() -> {
                id.set((int) dayMealDao.insert(df));
            }).get();

            df.mdID = id.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MealDay fd = new MealDay();
        fd.meal = meal;
        fd.date = df.date;
        fd.mdID = df.mdID;
        fd.serving = df.serving;
        fd.timestamp = df.timestamp;

        return fd;
    }

    LiveData<List<Long>> getDaysMeal() {
        return daysMeal;
    }

    void deleteDayMeal(MealDay meal) {
        AppDatabase.writeExecutor.execute(() -> dayFoodDao.delete(meal.mdID));
    }

    void updateDayFood(MealDay meal, double serving) {
        AppDatabase.writeExecutor.execute(() -> dayFoodDao.update(meal.mdID, serving));
    }

}
