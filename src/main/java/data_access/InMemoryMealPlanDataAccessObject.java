package data_access;

import entity.MealPlan;
import entity.MealPlanEntry;
import entity.Recipe;
import use_case.add_meal_to_plan.AddMealToPlanDataAccessInterface;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Keeps saved recipes and the weekly plan in memory. Used for tests and until
 * the SQLite version exists. Nothing is kept after the app closes.
 */
public class InMemoryMealPlanDataAccessObject implements AddMealToPlanDataAccessInterface {
    private final Map<String, Recipe> savedRecipes = new HashMap<>();
    private final MealPlan plan;

    /**
     * Creates an empty store whose plan starts on the Monday of the current week.
     */
    public InMemoryMealPlanDataAccessObject() {
        this(LocalDate.now().with(DayOfWeek.MONDAY));
    }

    /**
     * Creates an empty store whose plan starts on the given date.
     *
     * @param weekStartDate the first day of the planned week
     */
    public InMemoryMealPlanDataAccessObject(LocalDate weekStartDate) {
        this.plan = new MealPlan(weekStartDate);
    }

    /**
     * Adds a recipe to the saved collection. Stands in for the Save Recipe use case.
     *
     * @param recipe the recipe to save
     */
    public void addSavedRecipe(Recipe recipe) {
        savedRecipes.put(recipe.getApiId(), recipe);
    }

    @Override
    public Recipe getSavedRecipe(String apiId) {
        return savedRecipes.get(apiId);
    }

    /**
     * Returns a copy of the plan, so callers cannot change the stored plan
     * without going through {@link #saveEntry(MealPlanEntry)}.
     */
    @Override
    public MealPlan getMealPlan() {
        MealPlan copy = new MealPlan(plan.getWeekStartDate());
        for (MealPlanEntry entry : plan.getEntries()) {
            copy.addEntry(entry);
        }
        return copy;
    }

    @Override
    public void saveEntry(MealPlanEntry entry) {
        plan.addEntry(entry);
    }
}

