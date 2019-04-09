package de.seibushin.nutrigo.model.nutrition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Meal implements NutritionUnit {
    private int id;
    private String name;
    private List<FoodPortion> foods;

    /**
     * Constructor
     *
     * @param id   id
     * @param name name
     */
    public Meal(int id, String name, FoodPortion... foods) {
        this.id = id;
        this.name = name;
        this.foods = new ArrayList<>(Arrays.asList(foods));
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
        System.out.println();
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
