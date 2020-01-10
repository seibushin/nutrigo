package de.seibushin.nutrigo.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import de.seibushin.nutrigo.model.nutrition.Meal;

@Dao
public interface MealDao {
    @Query("SELECT * FROM meal")
    LiveData<List<Meal>> getAll();

    @Insert
    void insertAll(Meal... meals);

    @Insert
    long insert(Meal meal);

    @Delete
    void delete(Meal meal);

    @Update
    void update(Meal meal);

}
