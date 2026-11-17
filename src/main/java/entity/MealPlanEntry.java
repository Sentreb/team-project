package entity;

import java.time.DayOfWeek;

/**
 * One planned meal: a saved recipe in a day and meal slot of the weekly plan.
 * Immutable: to move a meal, remove this entry and add a new one.
 */
public class MealPlanEntry {
    private final DayOfWeek day;
    private final MealType mealType;
    private final Recipe recipe;

    /**
     * @throws IllegalArgumentException if any argument is null
     */
    public MealPlanEntry(DayOfWeek day, MealType mealType, Recipe recipe) {
        if (day == null) {
            throw new IllegalArgumentException("Day must not be null.");
        }
        if (mealType == null) {
            throw new IllegalArgumentException("Meal type must not be null.");
        }
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe must not be null.");
        }
        this.day = day;
        this.mealType = mealType;
        this.recipe = recipe;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public MealType getMealType() {
        return mealType;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    /**
     * Returns true if this entry is in the given day and meal slot.
     */
    public boolean isInSlot(DayOfWeek day, MealType mealType) {
        return this.day == day && this.mealType == mealType;
    }
}
