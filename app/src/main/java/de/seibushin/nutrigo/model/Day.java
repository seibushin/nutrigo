package de.seibushin.nutrigo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.seibushin.nutrigo.model.nutrition.NutritionUnit;

public class Day implements ChangeListener {
    private final List<ChangeListener> changeListeners = new ArrayList<>();

    private List<NutritionUnit> nutrition;
    private Date date;

    public Day(Date date) {
        this.date = date;
        this.nutrition = new ArrayList<>();
    }

    /**
     * Add a nutrition unit
     *
     * @param nus
     */
    public void addNutrition(NutritionUnit... nus) {
        // observe nu
        for (NutritionUnit nu : nus) {
            nu.addChangeListener(this);
            nutrition.add(nu);
        }
        change();
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
     *
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

    /**
     * Set the onChangeListener
     *
     * @param changeListener changeListener
     */
    public void addChangeListener(ChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    @Override
    public void change() {
        for (ChangeListener changeListener : changeListeners) {
            changeListener.change();
        }
    }
}
