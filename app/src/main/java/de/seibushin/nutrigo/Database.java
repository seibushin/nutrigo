package de.seibushin.nutrigo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.seibushin.nutrigo.model.Day;
import de.seibushin.nutrigo.model.Profile;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodPortion;
import de.seibushin.nutrigo.model.nutrition.Meal;
import de.seibushin.nutrigo.model.nutrition.NutritionUnit;

public class Database {
    private final Map<Integer, Food> allFoodsMap;
    private final List<NutritionUnit> allFoods;
    private final Map<Integer, Meal> allMealsMap;
    private final List<NutritionUnit> allMeals;
    private final Map<Long, Day> allDays;

    private Day selectedDay;
    private Profile profile;

    private static Database instance;

    /**
     * Constructor
     */
    public Database() {
        profile = new Profile();
        allFoodsMap = new HashMap<>();
        allFoods = new ArrayList<>();
        allMealsMap = new HashMap<>();
        allMeals = new ArrayList<>();
        allDays = new HashMap<>();
        selectedDay = new Day(today());
        allDays.put(selectedDay.getTime(), selectedDay);

        init();
    }

    /**
     * Initialize the database
     */
    private void init() {
        addFood(new Food("Ananas", 59, 0.2, 12.2, 12.2, 0.5, 100, 800));
        addFood(new Food("Apfel", 54, 0.1, 11.4, 10.3, 0.3, 100, 155));
        addFood(new Food(2, "Apfelmus", 54, 0.1, 11.4, 10.3, 0.3, 100, 155));
        addFood(new Food("Kiwi", 54, 0.1, 11.4, 10.3, 0.3, 100, 155));

        addMeal(new Meal(1, "Test", new FoodPortion(allFoodsMap.get(0), 50), new FoodPortion(allFoodsMap.get(1), 200)));
        addMeal(new Meal(2, "Tolles Meal", new FoodPortion(allFoodsMap.get(2), 66), new FoodPortion(allFoodsMap.get(3), 12.3)));

        // create a new calender of the current time
        Calendar calendar = Calendar.getInstance();
        // set the time to 00:00:00:000
        toDay(calendar);
        // get the date
        Date date = calendar.getTime();
        // try to get the day from the database
        Day day = allDays.get(date.getTime());
        // create new day if it does not exist
        if (day == null) {
            day = new Day(date);
            allDays.put(day.getTime(), day);
        }

        day.addNutrition(allFoodsMap.get(2));
        day.addNutrition(allMealsMap.get(1));
    }

    /**
     * Singleton pattern
     *
     * @return
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Get all Foods
     *
     * @return
     */
    public List<NutritionUnit> getAllFoods() {
        return allFoods;
    }

    /**
     * Add a new Food
     *
     * @param food
     */
    public void addFood(Food food) {
        int id = allFoodsMap.size();
        // search next empty id
        while (allFoodsMap.containsKey(id)) {
            id++;
        }

        food.setId(id);
        allFoods.add(food);
        allFoodsMap.put(id, food);
    }

    /**
     * Get all meals
     *
     * @return
     */
    public List<NutritionUnit> getAllMeals() {
        return allMeals;
    }

    /**
     * Add a new meal
     *
     * @param meal
     */
    public void addMeal(Meal meal) {
        int id = allMealsMap.size();
        // search next empty id
        while (allMealsMap.containsKey(id)) {
            id++;
        }

        meal.setId(id);
        allMeals.add(meal);
        allMealsMap.put(id, meal);
    }

    /**
     * get the selectedDay
     *
     * @return
     */
    public Day getSelectedDay() {
        return selectedDay;
    }

    /**
     * Select a day
     */
    public void selectDay() {
        this.selectedDay = null;
    }

    /**
     * get the profile
     *
     * @return
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Get the current day at 0:00:00
     *
     * @return
     */
    private static Date today() {
        Calendar calendar = Calendar.getInstance();
        toDay(calendar);

        return calendar.getTime();
    }

    private static void toDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
