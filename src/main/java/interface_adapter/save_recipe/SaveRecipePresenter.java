package interface_adapter.save_recipe;

import use_case.save_recipe.SaveRecipeOutputBoundary;
import use_case.save_recipe.SaveRecipeOutputData;

/**
 * The Presenter for the Save Recipe use case. Turns the interactor's result
 * into a message in the Save Recipe View Model.
 */
public class SaveRecipePresenter implements SaveRecipeOutputBoundary {

    private final SaveRecipeViewModel saveRecipeViewModel;

    /**
     * Creates the presenter.
     *
     * @param saveRecipeViewModel the view model that shows the save result
     */
    public SaveRecipePresenter(SaveRecipeViewModel saveRecipeViewModel) {
        this.saveRecipeViewModel = saveRecipeViewModel;
    }

    /**
     * Shows that the recipe was saved, using its name.
     *
     * @param outputData the details of the saved recipe
     */
    @Override
    public void prepareSuccessView(SaveRecipeOutputData outputData) {
        final SaveRecipeState saveRecipeState = saveRecipeViewModel.getState();
        saveRecipeState.setSuccessMessage(
                "Saved \"" + outputData.getRecipeName() + "\" to your collection.");
        saveRecipeState.setErrorMessage(null);
        saveRecipeViewModel.notifyStateChanged();
    }

    /**
     * Shows why the recipe was not saved.
     *
     * @param errorMessage why the recipe was not saved
     */
    @Override
    public void prepareFailView(String errorMessage) {
        final SaveRecipeState saveRecipeState = saveRecipeViewModel.getState();
        saveRecipeState.setErrorMessage(errorMessage);
        saveRecipeState.setSuccessMessage(null);
        saveRecipeViewModel.notifyStateChanged();
    }
}
