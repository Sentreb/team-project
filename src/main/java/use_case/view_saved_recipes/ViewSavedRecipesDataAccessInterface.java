package use_case.view_saved_recipes;

import entity.Recipe;
import java.util.List;

/**
 * What the View Saved Recipes use case needs from the saved-recipe collection.
 */
public interface ViewSavedRecipesDataAccessInterface {

  /**
   * Returns every saved recipe, in the order they were saved.
   *
   * @return the saved recipes, or an empty list when nothing is saved; never null
   */
  List<Recipe> getSavedRecipes();
}
