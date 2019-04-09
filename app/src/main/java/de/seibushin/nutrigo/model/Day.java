package de.seibushin.nutrigo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.seibushin.nutrigo.model.nutrition.NutritionUnit;

public class Day {
    private List<NutritionUnit> nutrition;
    private Date date;

    public Day(Date date) {
        this.date = date;
        this.nutrition = new ArrayList<>();
    }

    /**
     * Add a nutrition unit
     *
     * @param nu
     */
    public void addNutrition(NutritionUnit nu) {
        nutrition.add(nu);
    }

    /**
     * Get nutrition
     *
     * @return
     */
    public List<NutritionUnit> getNutrition() {
        return nutrition;
    }

    /**
     * Get time for the day
     * @return
     */
    public long getTime() {
        return date.getTime();
    }

    public double getKcal() {
        return nutrition.stream().mapToDouble(NutritionUnit::getKcal).sum();
    }

    public double getFat() {
        return nutrition.stream().mapToDouble(NutritionUnit::getFat).sum();
    }

    public double getCarbs() {
        return nutrition.stream().mapToDouble(NutritionUnit::getCarbs).sum();
    }

    public double getSugar() {
        return nutrition.stream().mapToDouble(NutritionUnit::getSugar).sum();
    }

    public double getProtein() {
        return nutrition.stream().mapToDouble(NutritionUnit::getProtein).sum();
    }
}
