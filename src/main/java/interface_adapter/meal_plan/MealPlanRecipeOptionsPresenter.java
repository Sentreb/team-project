package interface_adapter.meal_plan;

import use_case.view_saved_recipes.SavedRecipeSummary;
import use_case.view_saved_recipes.ViewSavedRecipesOutputBoundary;
import use_case.view_saved_recipes.ViewSavedRecipesOutputData;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fills the meal plan screen's recipe drop-down from the View Saved Recipes use
 * case. A second presenter for that use case, so the saved-recipes screen and
 * the meal plan screen each get the list in the form they need.
 */
public class MealPlanRecipeOptionsPresenter implements ViewSavedRecipesOutputBoundary {
    private final MealPlanViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the meal plan view model whose recipe options are updated
     */
    public MealPlanRecipeOptionsPresenter(MealPlanViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ViewSavedRecipesOutputData outputData) {
        Map<String, String> options = new LinkedHashMap<>();
        for (SavedRecipeSummary recipe : outputData.getSavedRecipes()) {
            options.put(recipe.getApiId(), recipe.getName());
        }
        viewModel.getState().setRecipeOptions(options);
        viewModel.notifyStateChanged();
    }
}
