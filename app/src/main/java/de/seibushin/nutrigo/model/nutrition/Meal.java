package de.seibushin.nutrigo.model.nutrition;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Meal implements NutritionUnit {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    @Ignore
    private final List<FoodPortion> foods = new ArrayList<>();

    public Meal() {

    }

    /**
     * Constructor
     *
     * @param id   id
     * @param name name
     */
    @Ignore
    public Meal(int id, String name, FoodPortion... foods) {
        this.id = id;
        this.name = name;
        for (FoodPortion food : foods) {
            this.foods.add(food);
        }
    }

    /**
     * Add food to the meal
     *
     * @param food food
     */
    public void addFood(FoodPortion food) {
        this.foods.add(food);
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public NutritionType getType() {
        return NutritionType.MEAL;
    }

    @Override
    public double getKcal() {
        return foods.stream().mapToDouble(FoodPortion::getKcal).sum();
    }

    @Override
    public double getFat() {
        return foods.stream().mapToDouble(FoodPortion::getFat).sum();
    }

    @Override
    public double getCarbs() {
        return foods.stream().mapToDouble(FoodPortion::getCarbs).sum();
    }

    @Override
    public double getSugar() {
        return foods.stream().mapToDouble(FoodPortion::getSugar).sum();
    }

    @Override
    public double getProtein() {
        return foods.stream().mapToDouble(FoodPortion::getProtein).sum();
    }

    @Override
    public double getWeight() {
        return foods.stream().mapToDouble(FoodPortion::getPortion).sum();
    }

    @Override
    public double getPortion() {
        return 1;
    }
}
