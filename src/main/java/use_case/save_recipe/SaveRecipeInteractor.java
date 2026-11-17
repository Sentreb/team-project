package use_case.save_recipe;

import entity.Recipe;
import use_case.common.RecipeServiceException;

/**
 * The interactor for the Save Recipe use case (README use case 2). Checks the
 * recipe is not already saved, fetches its full details, then saves it.
 */
public class SaveRecipeInteractor implements SaveRecipeInputBoundary {

    private final SaveRecipeDataAccessInterface recipeDataAccess;
    private final RecipeDetailsGateway recipeDetailsGateway;
    private final SaveRecipeOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param recipeDataAccess     the user's saved-recipe collection
     * @param recipeDetailsGateway where full recipe details come from
     * @param presenter            receives the success or fail result
     */
    public SaveRecipeInteractor(SaveRecipeDataAccessInterface recipeDataAccess,
                                RecipeDetailsGateway recipeDetailsGateway,
                                SaveRecipeOutputBoundary presenter) {
        this.recipeDataAccess = recipeDataAccess;
        this.recipeDetailsGateway = recipeDetailsGateway;
        this.presenter = presenter;
    }

    /**
     * Saves the recipe unless it is already saved or cannot be fetched.
     *
     * @param inputData the recipe the user picked
     */
    @Override
    public void execute(SaveRecipeInputData inputData) {
        String apiId = inputData.getApiId();
        if (recipeDataAccess.existsByApiId(apiId)) {
            presenter.prepareFailView("This recipe is already in your collection.");
            return;
        }

        Recipe recipe;
        try {
            recipe = recipeDetailsGateway.fetchById(apiId);
        } catch (RecipeServiceException e) {
            presenter.prepareFailView("Could not save this recipe. Please try again.");
            return;
        }
        recipeDataAccess.save(recipe);
        presenter.prepareSuccessView(new SaveRecipeOutputData(recipe.getName()));
    }
}
