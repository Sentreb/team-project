package use_case.search_recipes;

import java.util.List;
import use_case.common.RecipeServiceException;


public interface SearchRecipesGateway {
    List<RecipeSummary> searchByName(String name) throws RecipeServiceException;
    List<RecipeSummary> searchByIngredient(String ingredient) throws RecipeServiceException;
}
