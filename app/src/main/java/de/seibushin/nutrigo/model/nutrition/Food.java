package de.seibushin.nutrigo.model.nutrition;


public class Food implements NutritionUnit {
    private int id = -1;
    private String name;
    private double kcal;
    private double fat;
    private double carbs;
    private double sugar;
    private double protein;
    private double weight;
    private double portion;

    /**
     * Constructor without id
     *
     * @param name    name
     * @param kcal    kcal
     * @param fat     fat
     * @param carbs   carbs
     * @param sugar   sugar
     * @param protein protein
     * @param weight  weight
     * @param portion portion
     */
    public Food(String name, double kcal, double fat, double carbs, double sugar, double protein, double weight, double portion) {
        this.name = name;
        this.kcal = kcal;
        this.fat = fat;
        this.carbs = carbs;
        this.sugar = sugar;
        this.protein = protein;
        this.weight = weight;
        this.portion = portion;
    }

    /**
     * Constructor
     *
     * @param id   id
     * @param name name
     */
    public Food(int id, String name, double kcal, double fat, double carbs, double sugar, double protein, double weight, double portion) {
        this(name, kcal, fat, carbs, sugar, protein, weight, portion);
        this.id = id;
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
