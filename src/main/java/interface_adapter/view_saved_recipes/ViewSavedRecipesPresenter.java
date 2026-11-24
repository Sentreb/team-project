package interface_adapter.view_saved_recipes;

import java.util.ArrayList;
import java.util.List;
import use_case.view_saved_recipes.SavedRecipeSummary;
import use_case.view_saved_recipes.ViewSavedRecipesOutputBoundary;
import use_case.view_saved_recipes.ViewSavedRecipesOutputData;

/**
 * The Presenter for the View Saved Recipes use case. Turns the saved recipes
 * into names, or a message when there are none.
 */
public class ViewSavedRecipesPresenter implements ViewSavedRecipesOutputBoundary {

  private static final String EMPTY_MESSAGE = "No saved recipes yet.";

  private final ViewSavedRecipesViewModel viewSavedRecipesViewModel;

  /**
   * Creates the presenter.
   *
   * @param viewSavedRecipesViewModel the view model that holds the list
   */
  public ViewSavedRecipesPresenter(ViewSavedRecipesViewModel viewSavedRecipesViewModel) {
    this.viewSavedRecipesViewModel = viewSavedRecipesViewModel;
  }

  /**
   * Shows the recipe names in saved order, or a message when nothing is saved.
   *
   * @param outputData the saved recipes, in saved order
   */
  @Override
  public void prepareSuccessView(ViewSavedRecipesOutputData outputData) {
    final List<String> names = toNames(outputData.getSavedRecipes());
    final ViewSavedRecipesState state = viewSavedRecipesViewModel.getState();
    state.setRecipeNames(names);
    state.setMessage(names.isEmpty() ? EMPTY_MESSAGE : null);
    viewSavedRecipesViewModel.notifyStateChanged();
  }

  private List<String> toNames(List<SavedRecipeSummary> summaries) {
    final List<String> names = new ArrayList<>();
    for (SavedRecipeSummary summary : summaries) {
      names.add(summary.getName());
    }
    return names;
  }
}
