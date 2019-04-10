package de.seibushin.nutrigo.model;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private final List<ChangeListener> changeListeners = new ArrayList<>();

    private int kcal = 2000;
    private int fat = 200;
    private int carbs = 200;
    private int sugar = 50;
    private int protein = 200;

    /**
     * Empty constructor
     */
    public Profile() {

    }

    /**
     * Getter for target kcal
     *
     * @return kcal
     */
    public int getKcal() {
        return kcal;
    }

    /**
     * Setter for target kcal
     *
     * @param kcal kcal
     */
    public void setKcal(int kcal) {
        this.kcal = kcal;
        changed();
        changed();
    }

    /**
     * Getter for target fat
     *
     * @return fat
     */
    public int getFat() {
        return fat;
    }

    /**
     * Setter for target fat
     *
     * @param fat fat
     */
    public void setFat(int fat) {
        this.fat = fat;
        changed();
    }

    /**
     * Getter for target carbs
     *
     * @return carbs
     */
    public int getCarbs() {
        return carbs;
    }

    /**
     * Setter for target carbs
     *
     * @param carbs carbs
     */
    public void setCarbs(int carbs) {
        this.carbs = carbs;
        changed();
    }

    /**
     * Getter for target sugar
     *
     * @return sugar
     */
    public int getSugar() {
        return sugar;
    }

    /**
     * Setter for target sugar
     *
     * @param sugar sugar
     */
    public void setSugar(int sugar) {
        this.sugar = sugar;
        changed();
    }

    /**
     * Getter for target protein
     *
     * @return protein
     */
    public int getProtein() {
        return protein;
    }

    /**
     * Setter for target protein
     *
     * @param protein protein
     */
    public void setProtein(int protein) {
        this.protein = protein;
        changed();
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
