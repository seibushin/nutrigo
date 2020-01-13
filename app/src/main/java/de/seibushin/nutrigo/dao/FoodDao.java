package de.seibushin.nutrigo.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import de.seibushin.nutrigo.model.nutrition.Food;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM food")
    LiveData<List<Food>> getAll();

    @Insert
    void insertAll(Food... foods);

    @Insert
    void insert(Food food);

    @Delete
    void delete(Food food);

    @Update
    void update(Food food);

}
