package use_case.save_recipe;

import entity.Recipe;
import use_case.common.RecipeServiceException;

/**
 * Where the Save Recipe use case gets the full details of a recipe: its
 * ingredients, measures and instructions. The search results only carry a
 * summary, so this is called before a recipe is stored. {@code MealDbApiGateway}
 * implements this, so the use case never depends on how the details are fetched.
 */
public interface RecipeDetailsGateway {

    /**
     * Fetches the full details of one recipe.
     *
     * @param apiId the TheMealDB id of the recipe
     * @return a new recipe with default servings and no user notes; never null
     * @throws RecipeServiceException if the service cannot be reached, returns
     *                                an error or an unexpected response, or has
     *                                no recipe with this id
     */
    Recipe fetchById(String apiId) throws RecipeServiceException;
}
