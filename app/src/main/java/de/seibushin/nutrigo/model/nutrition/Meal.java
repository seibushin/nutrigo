package de.seibushin.nutrigo.model.nutrition;

import java.util.ArrayList;
import java.util.List;

import de.seibushin.nutrigo.model.ChangeListener;

public class Meal implements NutritionUnit, ChangeListener {
    private final List<ChangeListener> changeListeners = new ArrayList<>();

    private int id;
    private String name;
    private final List<FoodPortion> foods = new ArrayList<>();

    /**
     * Constructor
     *
     * @param id   id
     * @param name name
     */
    public Meal(int id, String name, FoodPortion... foods) {
        this.id = id;
        this.name = name;
        for (FoodPortion food : foods) {
            // observe food
            food.addChangeListener(this);
            this.foods.add(food);
        }
    }

    /**
     * Add food to the meal
     *
     * @param food food
     */
    public void addFood(FoodPortion food) {
        food.addChangeListener(this);
        this.foods.add(food);
        change();
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public NutritionType getType() {
        return NutritionType.MEAL;
    }

    @Override
    public double getKcal() {
        System.out.println();
        return foods.stream().mapToDouble(FoodPortion::getKcal).sum();
    }

    @Override
    public double getFat() {
        return foods.stream().mapToDouble(FoodPortion::getFat).sum();
    }

    @Override
    public double getCarbs() {
        return foods.stream().mapToDouble(FoodPortion::getCarbs).sum();
    }

    @Override
    public double getSugar() {
        return foods.stream().mapToDouble(FoodPortion::getSugar).sum();
    }

    @Override
    public double getProtein() {
        return foods.stream().mapToDouble(FoodPortion::getProtein).sum();
    }

    @Override
    public double getWeight() {
        return foods.stream().mapToDouble(FoodPortion::getPortion).sum();
    }

    @Override
    public double getPortion() {
        return 1;
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
