package de.seibushin.nutrigo.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.seibushin.nutrigo.dao.FoodDao;
import de.seibushin.nutrigo.model.nutrition.Food;

@Database(entities = {Food.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FoodDao foodDao();

    private static volatile AppDatabase INSTANCE;
    private static final int THREADS = 4;
    static final ExecutorService writeExecutor = Executors.newFixedThreadPool(THREADS);

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "ba_nutrigo")
                            .addCallback(callback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    // populate database with initial data
    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            writeExecutor.execute(() -> {
                FoodDao foodDao = INSTANCE.foodDao();
                Food food = new Food("Avocado", 138, 1.4, 12.5, 3.6, 0.4, 100, 100);
                foodDao.insert(food);
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