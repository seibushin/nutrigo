package de.seibushin.nutrigo.model.nutrition;

public interface NutritionUnit {
    /**
     * Nutrition unit id / database reference
     *
     * @return id
     */
    int getId();

    /**
     * Nutrition unit name
     *
     * @return name
     */
    String getName();

    /**
     * NutritionType
     *
     * @return type
     */
    NutritionType getType();

    /**
     * Nutrition unit kcal
     *
     * @return kcal
     */
    double getKcal();

    /**
     * Nutrition unit fat
     *
     * @return fat
     */
    double getFat();

    /**
     * Nutrition unit carbs
     *
     * @return carbs
     */
    double getCarbs();

    /**
     * Nutrition unit sugar
     *
     * @return sugar
     */
    double getSugar();

    /**
     * Nutrition unit protein
     *
     * @return protein
     */
    double getProtein();

    /**
     * Nutrition unit weight
     *
     * @return weight
     */
    double getWeight();

    /**
     * Nutrition unit portion
     *
     * @return portion
     */
    double getPortion();
}
