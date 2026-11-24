package use_case.search_recipes;

import java.util.List;
import use_case.common.RecipeServiceException;

/**
 * Where the Search Recipes use case gets matching recipes from. Each result is
 * a {@link RecipeSummary}, not a full recipe: the full details are fetched
 * later, when the user saves one. {@code MealDbApiGateway} implements this,
 * so the use case never depends on how the search is done.
 */
public interface SearchRecipesGateway {

    /**
     * Searches for recipes whose name matches the given text.
     *
     * @param name the text to search for; the interactor passes it already trimmed
     * @return the matching recipes, in the order the service returned them;
     *         an empty list if nothing matches, never null
     * @throws RecipeServiceException if the service cannot be reached or
     *                                returns an error or an unexpected response
     */
    List<RecipeSummary> searchByName(String name) throws RecipeServiceException;

    /**
     * Searches for recipes that use the given ingredient. Only a single
     * ingredient is supported, because TheMealDB offers multi-ingredient
     * filtering only on its paid plan.
     *
     * <p>The service does not return a category for this kind of search, so the
     * category of each result may be an empty string.</p>
     *
     * @param ingredient the ingredient to search for; the interactor passes it
     *                   already trimmed
     * @return the matching recipes, in the order the service returned them;
     *         an empty list if nothing matches, never null
     * @throws RecipeServiceException if the service cannot be reached or
     *                                returns an error or an unexpected response
     */
    List<RecipeSummary> searchByIngredient(String ingredient) throws RecipeServiceException;
}
