package de.seibushin.nutrigo.viewmodel;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import de.seibushin.nutrigo.dao.DailyDao;
import de.seibushin.nutrigo.dao.DayFood;
import de.seibushin.nutrigo.dao.DayFoodDao;
import de.seibushin.nutrigo.dao.DayMeal;
import de.seibushin.nutrigo.dao.DayMealDao;
import de.seibushin.nutrigo.dao.FoodDao;
import de.seibushin.nutrigo.dao.MealDao;
import de.seibushin.nutrigo.dao.MealXFood;
import de.seibushin.nutrigo.dao.ProfileDao;
import de.seibushin.nutrigo.model.Profile;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.MealInfo;

@Database(entities = {Food.class, DayFood.class, Profile.class, MealInfo.class, DayMeal.class, MealXFood.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FoodDao foodDao();

    public abstract DayFoodDao dayFoodDao();

    public abstract ProfileDao profileDao();

    public abstract MealDao mealDao();

    public abstract DayMealDao dayMealDao();

    public abstract DailyDao dailyDao();

    private static volatile AppDatabase INSTANCE;
    private static final int THREADS = 4;
    static final ExecutorService writeExecutor = Executors.newFixedThreadPool(THREADS);

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "ba_nutrigo")
                            .addMigrations(MIGRATION_2_3)
                            .addCallback(callback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS Profile");
            database.execSQL("CREATE TABLE IF NOT EXISTS Profile (" +
                    "id INTEGER NOT NULL," +
                    "kcal INTEGER NOT NULL," +
                    "fat INTEGER NOT NULL," +
                    "carbs INTEGER NOT NULL," +
                    "sugar INTEGER NOT NULL," +
                    "protein INTEGER NOT NULL," +
                    "weight REAL NOT NULL," +
                    "height INTEGER NOT NULL," +
                    "age INTEGER NOT NULL," +
                    "male INTEGER NOT NULL," +
                    "PRIMARY KEY(id)" +
                    ")");

            database.execSQL("INSERT INTO Profile VALUES (1, 2500, 80, 300, 75, 100,75.5,180,20,1)");
        }
    };

    // populate database with initial data
    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            writeExecutor.execute(() -> {
                FoodDao foodDao = INSTANCE.foodDao();
                Food food = new Food("Avocado", 138, 1.4, 12.5, 3.6, 0.4, 100, 150);
                food.portionize = false;
                food.served = food.getPortion();
                foodDao.insert(food);

                ProfileDao profileDao = INSTANCE.profileDao();
                Profile profile = new Profile();
                profileDao.insert(profile);
            });
        }
    };

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
