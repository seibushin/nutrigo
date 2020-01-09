package de.seibushin.nutrigo.model.nutrition;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Food implements NutritionUnit {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    private double kcal;
    private double fat;
    private double carbs;
    private double sugar;
    private double protein;
    private double weight;
    private double portion;

    public Food() {

    }

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
    @Ignore
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
    @Ignore
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

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public NutritionType getType() {
        return NutritionType.FOOD;
    }

    @Override
    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    @Override
    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    @Override
    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    @Override
    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    @Override
    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public double getPortion() {
        return portion;
    }

    public void setPortion(double portion) {
        this.portion = portion;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", kcal=" + kcal +
                ", fat=" + fat +
                ", carbs=" + carbs +
                ", sugar=" + sugar +
                ", protein=" + protein +
                ", weight=" + weight +
                ", portion=" + portion +
                '}';
    }

    public static Food[] populateData() {
        return new Food[]{
                new Food(1, "Avocado", 138, 1.4, 12.5, 3.6, 0.4, 100, 100),
                new Food(2, "Banane", 100, 100, 93, 1.2, 0.2, 20, 17.3),
                new Food(3, "Nudeln", 100, 125, 350, 12, 1.2, 71, 3.2),
                new Food(4, "Thunfisch Aldi", 70, 140, 127, 17, 6.5, 0, 0),
                new Food(5, "Salat Dressing TiP", 100, 30, 196, 2.1, 16, 10, 8.1),
                new Food(6, "Kaki", 100, 200, 71, 0.6, 0.3, 16, 16),
                new Food(7, "Strauchtomate", 100, 80, 18, 1, 0.2, 2.6, 2.5),
                new Food(8, "Eisbergsalat", 100, 200, 13, 1, 0.2, 1.6, 1.6),
                new Food(9, "Salatgurke", 100, 100, 12, 0.6, 0.2, 1.8, 1.7),
                new Food(10, "Pizza Stk - Thunfisch Tomate", 100, 80, 150, 8, 6, 18, 6),
                new Food(11, "Kinder Pingui", 100, 30, 450, 7, 29.7, 37.8, 33.1),
                new Food(12, "Mehl", 100, 15, 344, 10, 1, 72.3, 0.7),
                new Food(13, "Erbsen", 100, 140, 91, 5.6, 0.9, 11.9, 4.1),
                new Food(14, "Kartoffeln", 100, 250, 73, 1.3, 0, 15, 0.7),
                new Food(15, "Milch 1,5%", 100, 100, 47, 3.4, 1.5, 4.9, 4.9),
                new Food(16, "Fischstäbchen", 100, 150, 195, 13, 7.7, 18, 0.8),
                new Food(17, "Honey Wheat", 100, 50, 373, 10, 1.4, 78, 39),
                new Food(18, "Kartoffel-Hähnchen Pfanne Iglo", 100, 450, 92, 6.2, 3.6, 8, 2.5),
                new Food(19, "Yomaro Frozen Yogurt", 100, 280, 230, 5, 12, 24, 24),
                new Food(20, "Obstkuchen 1/4", 100, 100, 620, 30, 20, 75, 30),
                new Food(21, "Mandarine", 100, 120, 54, 0.7, 0.3, 10.1, 10.1),
                new Food(22, "Apfel", 100, 155, 54, 0.3, 0.1, 11.4, 10.3),
                new Food(23, "Tasty Whey", 100, 30, 390, 77, 6, 7, 3.5),
                new Food(24, "Creatine", 100, 3, 0, 0, 0, 0, 0),
                new Food(25, "Joghurt", 100, 150, 72, 4.3, 3.5, 5.2, 5.2),
                new Food(26, "Mozzarella Mini", 100, 125, 248, 18, 19, 1.5, 1.5),
                new Food(27, "Kefir Drink", 100, 500, 72, 3.1, 1.4, 10.9, 10.8),
                new Food(28, "Geflügel Würstchen", 100, 42, 195, 14, 15, 1, 1),
                new Food(29, "Hot Dog Brötchen", 100, 62, 284, 8.7, 4.4, 51, 7.5),
                new Food(30, "Dänischer Gurkensalat", 100, 20, 73, 0.6, 0.1, 17, 16),
                new Food(31, "Honey Mustart - Senf", 100, 5, 154, 5.1, 4.8, 20, 16),
                new Food(32, "Ketchup", 100, 15, 103, 1.9, 0.1, 22.9, 21.9),
                new Food(33, "Cini Minis", 100, 50, 418, 5.7, 10.1, 73.4, 24.8),
                new Food(34, "Ananas", 100, 800, 59, 0.5, 0.2, 12.4, 12.2),
                new Food(35, "Toast", 100, 38, 254, 8.5, 3.7, 45, 3.9),
                new Food(36, "Studentenfutter", 100, 100, 509, 9.9, 36, 32, 29),
                new Food(37, "Erdnüsse", 100, 100, 622, 25, 50, 14, 5.4),
                new Food(38, "Duplo", 100, 18.2, 555, 6.1, 33.5, 56, 50.4),
                new Food(39, "Corny Zartbitter", 100, 23, 451, 5.9, 17.4, 64.6, 33.2),
                new Food(40, "Ei", 100, 55, 150, 11.9, 9.3, 1.5, 1.5),
                new Food(41, "Brötchen", 100, 60, 246, 7.8, 1.2, 49, 3.5)
        };
    }
}
