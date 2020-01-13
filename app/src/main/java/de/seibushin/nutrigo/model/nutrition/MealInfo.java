package de.seibushin.nutrigo.model.nutrition;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MealInfo {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    public String name;

    public MealInfo() {

    }
}
