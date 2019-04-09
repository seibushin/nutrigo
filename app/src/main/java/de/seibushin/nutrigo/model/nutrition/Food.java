package de.seibushin.nutrigo.model.nutrition;


public class Food implements NutritionUnit {
    private int id;
    private String name;
    private double kcal;
    private double fat;
    private double carbs;
    private double sugar;
    private double protein;
    private double weight;
    private double portion;

    /**
     * Constructor
     *
     * @param id   id
     * @param name name
     */
    public Food(int id, String name, double kcal, double fat, double carbs, double sugar, double protein, double weight, double portion) {
        this.id = id;
        this.name = name;
        this.kcal = kcal;
        this.fat = fat;
        this.carbs = carbs;
        this.sugar = sugar;
        this.protein = protein;
        this.weight = weight;
        this.portion = portion;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public NutritionType getType() {
        return NutritionType.FOOD;
    }

    @Override
    public double getKcal() {
        return kcal;
    }

    @Override
    public double getFat() {
        return fat;
    }

    @Override
    public double getCarbs() {
        return carbs;
    }

    @Override
    public double getSugar() {
        return sugar;
    }

    @Override
    public double getProtein() {
        return protein;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public double getPortion() {
        return portion;
    }
}
