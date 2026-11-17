package use_case.save_recipe;

/**
 * The input boundary for the Save Recipe use case. The controller calls this;
 * the interactor implements it.
 */
public interface SaveRecipeInputBoundary {

    /**
     * Saves the chosen recipe to the user's collection.
     *
     * @param inputData the recipe the user picked
     */
    void execute(SaveRecipeInputData inputData);
}
