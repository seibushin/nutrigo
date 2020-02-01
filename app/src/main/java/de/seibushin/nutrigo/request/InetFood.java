package de.seibushin.nutrigo.request;

import java.util.List;

import de.seibushin.nutrigo.model.nutrition.NutritionType;
import de.seibushin.nutrigo.model.nutrition.NutritionUnit;

public class InetFood  implements NutritionUnit {
    public String name;
    public double kcal;
    public double fat;
    public double protein;
    public double carbs;
    public double sugar;
    public double weight;
    public double portion;
    public List<Serving> servings;

    public InetFood(String name, double kcal, double fat, double carbs, double sugar, double protein, double weight, double portion, List<Serving> servings) {
        this.name = name;
        this.kcal = kcal;
        this.fat = fat;
        this.protein = protein;
        this.carbs = carbs;
        this.sugar = sugar;
        this.weight = weight;
        this.portion = portion;
        this.servings = servings;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public NutritionType getType() {
        return NutritionType.FOOD;
    }

    @Override
    public double getKcal() {
        return kcal;
    }

    @Override
    public double getFat() {
        return fat;
    }

    @Override
    public double getCarbs() {
        return carbs;
    }

    @Override
    public double getSugar() {
        return sugar;
    }

    @Override
    public double getProtein() {
        return protein;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public double getPortion() {
        return portion;
    }
}
