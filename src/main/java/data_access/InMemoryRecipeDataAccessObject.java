package data_access;

import entity.Recipe;
import java.util.HashMap;
import java.util.Map;
import use_case.add_meal_to_plan.SavedRecipeLookup;
import use_case.save_recipe.SaveRecipeDataAccessInterface;

/**
 * Keeps the user's saved recipes in memory. Used for tests and until the SQLite
 * version exists. Nothing is kept after the app closes.
 *
 * <p>Lookups return the stored recipe itself, not a copy.</p>
 */
public class InMemoryRecipeDataAccessObject
        implements SaveRecipeDataAccessInterface, SavedRecipeLookup {
  private final Map<String, Recipe> recipes = new HashMap<>();

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
}
