package use_case.save_recipe;

/**
 * The output boundary for the Save Recipe use case. The interactor calls this;
 * the presenter implements it.
 */
public interface SaveRecipeOutputBoundary {

    /**
     * Prepares the view after the recipe was saved.
     *
     * @param outputData the details of the saved recipe
     */
    void prepareSuccessView(SaveRecipeOutputData outputData);

    /**
     * Prepares the view after the recipe could not be saved.
     *
     * @param errorMessage why the recipe was not saved
     */
    void prepareFailView(String errorMessage);
}
