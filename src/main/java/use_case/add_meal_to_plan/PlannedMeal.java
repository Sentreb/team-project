package use_case.add_meal_to_plan;

import entity.MealType;

import java.time.DayOfWeek;

/**
 * One filled slot of the weekly plan, reduced to what the screen needs to show.
 */
public class PlannedMeal {
    private final DayOfWeek day;
    private final MealType mealType;
    private final String recipeName;

    /**
     * Creates a planned meal.
     *
     * @param day        the day of the week
     * @param mealType   the meal slot
     * @param recipeName the name of the recipe in that slot
     */
    public PlannedMeal(DayOfWeek day, MealType mealType, String recipeName) {
        this.day = day;
        this.mealType = mealType;
        this.recipeName = recipeName;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public MealType getMealType() {
        return mealType;
    }

    public String getRecipeName() {
        return recipeName;
    }
}

