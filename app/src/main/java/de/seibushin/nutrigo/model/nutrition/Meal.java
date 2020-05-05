package de.seibushin.nutrigo.model.nutrition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Junction;
import androidx.room.Relation;
import de.seibushin.nutrigo.dao.MealXFood;

public class Meal implements NutritionUnit {
    @Embedded
    public MealInfo mealInfo = new MealInfo();
    @Relation(parentColumn = "id",
            entity = Food.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = MealXFood.class,
                    parentColumn = "mealId",
                    entityColumn = "foodId")
    )
    public List<Food> foods = new ArrayList<>();

    @Ignore
    public Meal() {

    }

    public Meal(MealInfo mealInfo, List<Food> foods) {
        this.mealInfo = mealInfo;
        this.foods = foods;
    }

    @Override
    public int getId() {
        return mealInfo.id;
    }

    @Override
    public String getName() {
        return mealInfo.name;
    }

    @Override
    public NutritionType getType() {
        return NutritionType.MEAL;
    }

    @Override
    public double getKcal() {
        return foods.stream().mapToDouble(Food::getKcal).sum();
    }

    @Override
    public double getFat() {
        return foods.stream().mapToDouble(Food::getFat).sum();
    }

    @Override
    public double getCarbs() {
        return foods.stream().mapToDouble(Food::getCarbs).sum();
    }

    @Override
    public double getSugar() {
        return foods.stream().mapToDouble(Food::getSugar).sum();
    }

    @Override
    public double getProtein() {
        return foods.stream().mapToDouble(Food::getProtein).sum();
    }

    @Override
    public double getWeight() {
        return foods.stream().mapToDouble(Food::getPortion).sum();
    }

    @Override
    public double getPortion() {
        return 1;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", kcal=" + getKcal() +
                ", fat=" + getFat() +
                ", carbs=" + getCarbs() +
                ", sugar=" + getSugar() +
                ", protein=" + getProtein() +
                ", weight=" + getWeight() +
                ", portion=" + getPortion() +
                '}';
    }
}
