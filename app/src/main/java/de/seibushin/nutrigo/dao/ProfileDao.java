package de.seibushin.nutrigo.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import de.seibushin.nutrigo.model.Profile;

@Dao
public interface ProfileDao {
    @Query("SELECT * FROM profile LIMIT 1")
    LiveData<Profile> getProfile();

    @Insert
    void insert(Profile profile);

    @Delete
    void delete(Profile profile);

    @Update
    void update(Profile profile);

}
