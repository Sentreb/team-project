package use_case.add_meal_to_plan;

import entity.MealPlan;
import entity.MealPlanEntry;
import entity.Recipe;

/**
 * The data the Add Meal to Plan use case needs to read and store.
 */
public interface AddMealToPlanDataAccessInterface {

    /**
     * Returns the saved recipe with the given TheMealDB id.
     *
     * @param apiId the TheMealDB id of the recipe
     * @return the saved recipe, or null if it is not in the user's collection
     */
    Recipe getSavedRecipe(String apiId);

    /**
     * Returns the user's current weekly plan. Never returns null: if nothing
     * has been planned yet, the plan is empty.
     *
     * @return the weekly plan
     */
    MealPlan getMealPlan();

    /**
     * Stores an entry in its slot, replacing any entry already there.
     *
     * @param entry the entry to store
     * @throws MealPlanStorageException if the entry could not be stored
     */
    void saveEntry(MealPlanEntry entry) throws MealPlanStorageException;
}

