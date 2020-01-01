package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import de.seibushin.nutrigo.Nutrigo;
import de.seibushin.nutrigo.dao.DayFood;
import de.seibushin.nutrigo.dao.DayFoodDao;
import de.seibushin.nutrigo.dao.FoodDao;
import de.seibushin.nutrigo.dao.ProfileDao;
import de.seibushin.nutrigo.model.Profile;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodDay;
import de.seibushin.nutrigo.model.nutrition.FoodPortion;

class Repo {
    private FoodDao foodDao;
    private DayFoodDao dayFoodDao;
    private LiveData<List<FoodPortion>> allFood;
    private LiveData<List<FoodDay>> dayFood;
    private ProfileDao profileDao;
    private LiveData<Profile> profile;
    private LiveData<List<Long>> days;

    Repo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        foodDao = db.foodDao();
        allFood = foodDao.getAll();

        dayFoodDao = db.dayFoodDao();
        dayFood = dayFoodDao.getFoods(Nutrigo.selectedDay);
        days = dayFoodDao.getDays();

        profileDao = db.profileDao();
        profile = profileDao.getProfile();
    }

    LiveData<List<FoodPortion>> getAllFood() {
        return allFood;
    }

    void insertFood(Food food) {
        AppDatabase.writeExecutor.execute(() -> foodDao.insert(food));
    }

    void deleteFood(Food food) {
        AppDatabase.writeExecutor.execute(() -> foodDao.delete(food));
    }

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

    LiveData<List<Long>> getDays() {
        return days;
    }

    void deleteDayFood(FoodDay food) {
        AppDatabase.writeExecutor.execute(() -> dayFoodDao.delete(food.fdID));
    }

    void updateDayFood(FoodDay food, double serving) {
        AppDatabase.writeExecutor.execute(() -> dayFoodDao.update(food.fdID, serving));
    }

    LiveData<Profile> getProfile() {
        return profile;
    }

    void updateProfile(Profile profile) {
        AppDatabase.writeExecutor.execute(()-> profileDao.update(profile));
    }
}
