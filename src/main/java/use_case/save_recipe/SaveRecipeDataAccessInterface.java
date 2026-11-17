package use_case.save_recipe;

import entity.Recipe;

/**
 * What the Save Recipe use case needs from the saved-recipe collection.
 * The in-memory and SQLite recipe DAOs implement this.
 */
public interface SaveRecipeDataAccessInterface {

    /**
     * Checks whether a recipe is already in the user's collection.
     *
     * @param apiId the TheMealDB id of the recipe
     * @return true if a recipe with this apiId is already saved
     */
    boolean existsByApiId(String apiId);

    /**
     * Adds a recipe to the user's collection.
     *
     * @param recipe the recipe to save
     */
    void save(Recipe recipe);
}
