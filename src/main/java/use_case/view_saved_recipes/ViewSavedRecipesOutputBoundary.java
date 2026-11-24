package use_case.view_saved_recipes;

/**
 * The output boundary for the View Saved Recipes use case. The interactor calls
 * this; the presenter implements it.
 */
public interface ViewSavedRecipesOutputBoundary {

  /**
   * Prepares the view with the saved recipes. The list may be empty.
   *
   * @param outputData the saved recipes, in saved order
   */
  void prepareSuccessView(ViewSavedRecipesOutputData outputData);
}
