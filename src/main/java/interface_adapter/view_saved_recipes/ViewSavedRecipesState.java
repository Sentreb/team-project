package interface_adapter.view_saved_recipes;

import java.util.List;

/**
 * The state for the View Saved Recipes View Model: the names to list, and the
 * message shown when there are none.
 */
public class ViewSavedRecipesState {

  private List<String> recipeNames = List.of();
  private String message = null;

  /**
   * Returns the names of the saved recipes, in saved order.
   *
   * @return a read-only list, empty when nothing is saved
   */
  public List<String> getRecipeNames() {
    return recipeNames;
  }

  /**
   * Replaces the names with a copy of the given list.
   *
   * @param recipeNames the names, in saved order
   */
  public void setRecipeNames(List<String> recipeNames) {
    this.recipeNames = List.copyOf(recipeNames);
  }

  /**
   * Returns the message to show instead of names.
   *
   * @return the message, or null when there are recipes to list
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the message to show instead of names.
   *
   * @param message the message, or null to clear it
   */
  public void setMessage(String message) {
    this.message = message;
  }
}
