package de.seibushin.nutrigo.dao;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.Meal;

@Entity
public class MealFood {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ForeignKey(entity = Meal.class, parentColumns = "id", childColumns = "mealId")
    public int mealId;
    @ForeignKey(entity = Food.class, parentColumns = "id", childColumns = "foodId")
    public int foodId;
    public double serving;


    public MealFood() {

    }

    @Ignore
    public MealFood(int mealId, int foodId, double serving) {
        this.mealId = mealId;
        this.foodId = foodId;
        this.serving = serving;
    }

    @Override
    public String toString() {
        return "MealFood{" +
                "id=" + id +
                ", mealId=" + mealId +
                ", foodId=" + foodId +
                ", serving=" + serving +
                '}';
    }
}
