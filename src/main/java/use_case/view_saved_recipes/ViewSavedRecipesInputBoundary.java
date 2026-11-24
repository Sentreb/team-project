package use_case.view_saved_recipes;

/**
 * The input boundary for the View Saved Recipes use case. The controller calls
 * this; the interactor implements it.
 */
public interface ViewSavedRecipesInputBoundary {

  /**
   * Lists every recipe in the user's collection.
   */
  void execute();
}
