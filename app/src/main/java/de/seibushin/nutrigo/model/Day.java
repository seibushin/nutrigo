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
}
