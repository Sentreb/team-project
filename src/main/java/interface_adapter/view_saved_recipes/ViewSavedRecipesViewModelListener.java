package interface_adapter.view_saved_recipes;

/**
 * Something that wants to know when the View Saved Recipes state changes, such
 * as the view that lists the recipes.
 */
public interface ViewSavedRecipesViewModelListener {

  /**
   * The View Saved Recipes state was updated.
   */
  void stateChanged();
}
