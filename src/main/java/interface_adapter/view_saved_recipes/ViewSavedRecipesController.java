package interface_adapter.view_saved_recipes;

import use_case.view_saved_recipes.ViewSavedRecipesInputBoundary;

/**
 * The Controller for the View Saved Recipes use case. Turns "show my saved
 * recipes" from the view into a call on the input boundary.
 */
public class ViewSavedRecipesController {

  private final ViewSavedRecipesInputBoundary viewSavedRecipesInteractor;

  /**
   * Creates the controller.
   *
   * @param viewSavedRecipesInteractor the View Saved Recipes use case
   */
  public ViewSavedRecipesController(ViewSavedRecipesInputBoundary viewSavedRecipesInteractor) {
    this.viewSavedRecipesInteractor = viewSavedRecipesInteractor;
  }

  /**
   * Executes the View Saved Recipes use case.
   */
  public void execute() {
    viewSavedRecipesInteractor.execute();
  }
}
