package de.seibushin.nutrigo.model.nutrition;

public interface EatPoint {
    /**
     * Get the Nutrition timestamp
     *
     * @return
     */
    Long getTimestamp();

    NutritionType getType();

    double getKcal();
}
