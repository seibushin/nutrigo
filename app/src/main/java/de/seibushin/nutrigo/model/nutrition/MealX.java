package de.seibushin.nutrigo.model.nutrition;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;
import de.seibushin.nutrigo.dao.MealFood;

public class MealX implements NutritionUnit {
    @Embedded
    public Meal meal;
    @Relation(parentColumn = "id", entityColumn = "id", entity = Food.class, associateBy = @Junction(value = MealFood.class, parentColumn = "mealId", entityColumn = "foodId"))
    private List<Food> foods = new ArrayList<>();

    public MealX() {

    }


    /**
     * Add food to the meal
     *
     * @param food food
     */
    public void addFood(Food food) {
        this.foods.add(food);
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }


    @Override
    public int getId() {
        return meal.getId();
    }

    @Override
    public String getName() {
        return meal.getName();
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
}
