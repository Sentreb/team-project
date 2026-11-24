package data_access;

import entity.Recipe;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import use_case.add_meal_to_plan.SavedRecipeLookup;
import use_case.save_recipe.SaveRecipeDataAccessInterface;
import use_case.view_saved_recipes.ViewSavedRecipesDataAccessInterface;

/**
 * Keeps the user's saved recipes in memory. Used for tests and until the SQLite
 * version exists. Nothing is kept after the app closes.
 *
 * <p>Lookups return the stored recipe itself, not a copy.</p>
 */
public class InMemoryRecipeDataAccessObject
        implements SaveRecipeDataAccessInterface, SavedRecipeLookup,
        ViewSavedRecipesDataAccessInterface {
  private final Map<String, Recipe> recipes = new LinkedHashMap<>();

  @Override
  public boolean existsByApiId(String apiId) {
    return recipes.containsKey(apiId);
  }

  @Override
  public void save(Recipe recipe) {
    recipes.put(recipe.getApiId(), recipe);
  }

  @Override
  public Recipe getSavedRecipe(String apiId) {
    return recipes.get(apiId);
  }

  @Override
  public List<Recipe> getSavedRecipes() {
    return new ArrayList<>(recipes.values());
  }
}
