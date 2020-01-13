package de.seibushin.nutrigo.dao;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(primaryKeys = {"mealId", "foodId"}, indices = {@Index("mealId"), @Index("foodId")})
public class MealXFood {
    public int mealId;
    public int foodId;

    public double serving;

    public MealXFood() {

    }

    @Ignore
    public MealXFood(int mealId, int foodId, double serving) {
        this.mealId = mealId;
        this.foodId = foodId;
        this.serving = serving;
    }
}
