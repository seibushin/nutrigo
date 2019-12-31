package de.seibushin.nutrigo.model.nutrition;

import androidx.room.Embedded;

public class FoodDay implements NutritionUnit {
    @Embedded
    public Food food;
    public double serving;
    public int fdID;
    public long date;
    public long timestamp;

    public FoodDay() {

    }

    @Override
    public int getId() {
        return food.getId();
    }

    @Override
    public String getName() {
        return food.getName();
    }

    @Override
    public NutritionType getType() {
        return NutritionType.FOOD;
    }

    @Override
    public double getKcal() {
        return portionize(food.getKcal());
    }

    @Override
    public double getFat() {
        return portionize(food.getFat());
    }

    @Override
    public double getCarbs() {
        return portionize(food.getCarbs());
    }

    @Override
    public double getSugar() {
        return portionize(food.getSugar());
    }

    @Override
    public double getProtein() {
        return portionize(food.getProtein());
    }

    @Override
    public double getWeight() {
        return food.getWeight();
    }

    @Override
    public double getPortion() {
        return serving;
    }

    /**
     * Portionize the value according to the portion and the foods values
     *
     * @param v
     * @return
     */
    private double portionize(double v) {
        return v * serving / food.getWeight();
    }
}
