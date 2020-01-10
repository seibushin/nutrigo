package de.seibushin.nutrigo.dao;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.Meal;

@Entity
public class DayMeal {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int mdID;

    @ForeignKey(entity = Meal.class, parentColumns = "id", childColumns = "fid")
    public int mid;

    public long date;

    public double serving;

    public long timestamp;
}
