package interface_adapter.save_recipe;

import use_case.save_recipe.SaveRecipeInputBoundary;
import use_case.save_recipe.SaveRecipeInputData;

/**
 * The Controller for the Save Recipe use case. Turns "save this recipe" from
 * the view into a call on the input boundary.
 */
public class SaveRecipeController {

    // TODO(Clifford): declare the field for the use case this controller calls

    /**
     * Creates the controller.
     *
     * @param saveRecipeInteractor the Save Recipe use case
     */
    public SaveRecipeController(SaveRecipeInputBoundary saveRecipeInteractor) {
        // TODO(Clifford): store the input boundary
    }

    /**
     * Executes the Save Recipe use case for one recipe.
     *
     * @param apiId the TheMealDB id of the recipe to save
     */
    public void execute(String apiId) {
        // TODO(Clifford): build the input data and pass it to the use case
    }
}
