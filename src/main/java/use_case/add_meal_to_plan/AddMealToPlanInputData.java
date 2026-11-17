package use_case.add_meal_to_plan;

import entity.MealType;

import java.time.DayOfWeek;

/**
 * The user's request to put a saved recipe in a day and meal slot of the weekly plan.
 */
public class AddMealToPlanInputData {
    private final String recipeApiId;
    private final DayOfWeek day;
    private final MealType mealType;
    private final boolean replaceExisting;

    /**
     * Creates a request to add a meal to the plan.
     *
     * @param recipeApiId     the TheMealDB id of a saved recipe
     * @param day             the day of the week
     * @param mealType        the meal slot
     * @param replaceExisting true if the user has already agreed to replace
     *                        the meal currently in the slot
     */
    public AddMealToPlanInputData(String recipeApiId, DayOfWeek day, MealType mealType,
                                  boolean replaceExisting) {
        this.recipeApiId = recipeApiId;
        this.day = day;
        this.mealType = mealType;
        this.replaceExisting = replaceExisting;
    }

    public String getRecipeApiId() {
        return recipeApiId;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public MealType getMealType() {
        return mealType;
    }

    public boolean isReplaceExisting() {
        return replaceExisting;
    }
}

