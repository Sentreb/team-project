package interface_adapter.save_recipe;

import use_case.save_recipe.SaveRecipeInputBoundary;
import use_case.save_recipe.SaveRecipeInputData;

/**
 * The Controller for the Save Recipe use case. Turns "save this recipe" from
 * the view into a call on the input boundary.
 */
public class SaveRecipeController {

    private final SaveRecipeInputBoundary saveRecipeInteractor;

    /**
     * Creates the controller.
     *
     * @param saveRecipeInteractor the Save Recipe use case
     */
    public SaveRecipeController(SaveRecipeInputBoundary saveRecipeInteractor) {
        this.saveRecipeInteractor = saveRecipeInteractor;
    }

    /**
     * Executes the Save Recipe use case for one recipe.
     *
     * @param apiId the TheMealDB id of the recipe to save
     */
    public void execute(String apiId) {
        saveRecipeInteractor.execute(new SaveRecipeInputData(apiId));
    }
}
