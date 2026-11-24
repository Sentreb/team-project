package use_case.view_saved_recipes;

import java.util.List;

/**
 * The output data for the View Saved Recipes use case: a read-only list of
 * summaries in saved order. Holds plain values, never a Recipe.
 */
public class ViewSavedRecipesOutputData {

  private final List<SavedRecipeSummary> savedRecipes;

  /**
   * Creates the output data from a copy of the given list.
   *
   * @param savedRecipes the summaries, in saved order
   */
  public ViewSavedRecipesOutputData(List<SavedRecipeSummary> savedRecipes) {
    this.savedRecipes = List.copyOf(savedRecipes);
  }

  /**
   * Returns the saved recipes in saved order.
   *
   * @return a read-only list, empty when nothing is saved
   */
  public List<SavedRecipeSummary> getSavedRecipes() {
    return savedRecipes;
  }
}
