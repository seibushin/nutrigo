package de.seibushin.nutrigo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.seibushin.nutrigo.model.Day;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodPortion;
import de.seibushin.nutrigo.model.nutrition.Meal;
import de.seibushin.nutrigo.model.nutrition.NutritionUnit;

public class Database {
    private static final Map<Integer, Food> allFoods = new HashMap<>();
    private static final Map<Integer, Meal> allMeals = new HashMap<>();
    private static final Map<Long, Day> allDays = new HashMap<>();

    private static Date selectedDay = today();

    /**
     * Get all Foods
     *
     * @return
     */
    public static List<NutritionUnit> getAllFoods() {
        return new ArrayList<>(allFoods.values());
    }

    /**
     * Get all meals
     *
     * @return
     */
    public static List<NutritionUnit> getAllMeals() {
        return new ArrayList<>(allMeals.values());
    }

    /**
     * Get all days
     *
     * @return
     */
    public static List<Day> getAllDays() {
        return new ArrayList<>(allDays.values());
    }

    /**
     *
     * @return
     */
    public static Day getSelectedDay() {
        return allDays.get(selectedDay.getTime());
    }

    /**
     * Get the current day at 0:00:00
     * @return
     */
    private static Date today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    static {
        allFoods.put(1, new Food(1, "Ananas", 59, 0.2, 12.2, 12.2, 0.5, 100, 800));
        allFoods.put(2, new Food(2, "Apfel", 54, 0.1, 11.4, 10.3, 0.3, 100, 155));
        allFoods.put(3, new Food(3, "Apfelmus", 54, 0.1, 11.4, 10.3, 0.3, 100, 155));
        allFoods.put(4, new Food(4, "Kiwi", 54, 0.1, 11.4, 10.3, 0.3, 100, 155));

        allMeals.put(1, new Meal(1, "Test", new FoodPortion(allFoods.get(1), 50), new FoodPortion(allFoods.get(2), 200)));
        allMeals.put(2, new Meal(2, "Tolles Meal", new FoodPortion(allFoods.get(3), 66), new FoodPortion(allFoods.get(4), 12.3)));

        Day today = new Day(today());
        today.addNutrition(allFoods.get(2));
        today.addNutrition(allMeals.get(1));

        allDays.put(today().getTime(), today);
    }
}
