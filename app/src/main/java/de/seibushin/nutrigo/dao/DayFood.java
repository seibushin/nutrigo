package de.seibushin.nutrigo.dao;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import de.seibushin.nutrigo.model.nutrition.Food;

@Entity
public class DayFood {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ForeignKey(entity = Food.class, parentColumns = "id", childColumns = "fid")
    public int fid;

    public long date;

    public double serving;

    public long timestamp;
}
