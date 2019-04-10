package de.seibushin.nutrigo.model.nutrition;

import java.util.ArrayList;
import java.util.List;

import de.seibushin.nutrigo.model.ChangeListener;

public class FoodPortion implements NutritionUnit {
    private final List<ChangeListener> changeListeners = new ArrayList<>();

    private Food food;
    private double portion;

    public FoodPortion(Food food, double portion) {
        this.food = food;
        this.portion = portion;
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
        return portion;
    }

    /**
     * Portionize the value according to the portion and the foods values
     *
     * @param v
     * @return
     */
    private double portionize(double v) {
        return v * portion / food.getWeight();
    }

    /**
     * Set the onChangeListener
     *
     * @param changeListener changeListener
     */
    public void addChangeListener(ChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    /**
     * notify the listener about the changes
     */
    private void changed() {
        for (ChangeListener changeListener : changeListeners) {
            changeListener.change();
        }
    }
}
