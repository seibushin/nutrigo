package de.seibushin.nutrigo.model.nutrition;

import java.util.Objects;

import androidx.room.Embedded;

public class MealDay implements NutritionUnit, NutritionDay, EatPoint {
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
        return portionize(meal.getWeight());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealDay mealDay = (MealDay) o;
        return Double.compare(mealDay.serving, serving) == 0 &&
                mdID == mealDay.mdID &&
                date == mealDay.date &&
                timestamp == mealDay.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(meal, serving, mdID, date, timestamp);
    }

    @Override
    public Long getTimestamp() {
        return timestamp;
    }
}
