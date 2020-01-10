package de.seibushin.nutrigo.model.nutrition;

import androidx.room.Embedded;

public class MealDay implements NutritionUnit {
    @Embedded
    public Meal meal;
    public double serving;
    public int mdID;
    public long date;
    public long timestamp;

    public MealDay() {

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
        return portionize(meal.getKcal());
    }

    @Override
    public double getFat() {
        return portionize(meal.getFat());
    }

    @Override
    public double getCarbs() {
        return portionize(meal.getCarbs());
    }

    @Override
    public double getSugar() {
        return portionize(meal.getSugar());
    }

    @Override
    public double getProtein() {
        return portionize(meal.getProtein());
    }

    @Override
    public double getWeight() {
        return meal.getWeight();
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
        return v * serving;
    }
}
