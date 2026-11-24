package use_case.view_saved_recipes;

import entity.Recipe;
import java.util.ArrayList;
import java.util.List;

/**
 * The interactor for the View Saved Recipes use case. Turns each saved recipe
 * into a summary and keeps the saved order.
 */
public class ViewSavedRecipesInteractor implements ViewSavedRecipesInputBoundary {

  private final ViewSavedRecipesDataAccessInterface recipeDataAccess;
  private final ViewSavedRecipesOutputBoundary presenter;

  /**
   * Creates the interactor.
   *
   * @param recipeDataAccess the user's saved-recipe collection
   * @param presenter        receives the list of saved recipes
   */
  public ViewSavedRecipesInteractor(ViewSavedRecipesDataAccessInterface recipeDataAccess,
                                    ViewSavedRecipesOutputBoundary presenter) {
    this.recipeDataAccess = recipeDataAccess;
    this.presenter = presenter;
  }

  /**
   * Lists every saved recipe, or an empty list when nothing is saved.
   */
  @Override
  public void execute() {
    List<Recipe> recipes = recipeDataAccess.getSavedRecipes();
    List<SavedRecipeSummary> summaries = new ArrayList<>();
    for (Recipe recipe : recipes) {
      summaries.add(new SavedRecipeSummary(recipe.getApiId(), recipe.getName()));
    }
    presenter.prepareSuccessView(new ViewSavedRecipesOutputData(summaries));
  }
}
