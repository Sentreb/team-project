package use_case.add_meal_to_plan;

import entity.Recipe;

/**
 * How the Add Meal to Plan use case finds a recipe in the user's saved collection.
 * The recipe DAO implements this, so this use case never depends on how recipes are saved.
 */
public interface SavedRecipeLookup {

    /**
     * Returns the saved recipe with the given TheMealDB id.
     *
     * @param apiId the TheMealDB id of the recipe
     * @return the saved recipe, or null if it is not in the user's collection
     */
    Recipe getSavedRecipe(String apiId);
}
