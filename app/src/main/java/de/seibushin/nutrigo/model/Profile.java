package de.seibushin.nutrigo.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Profile {
    @PrimaryKey
    @NonNull
    private int id = 1;
    private int kcal = 2000;
    private int fat = 200;
    private int carbs = 200;
    private int sugar = 50;
    private int protein = 200;

    public float weight = 75.0f;
    public int height = 180;
    public int age = 20;
    public boolean male = true;

    /**
     * Empty constructor
     */
    public Profile() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    }
}
