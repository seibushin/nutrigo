package de.seibushin.nutrigo.model;

public class Profile {

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
