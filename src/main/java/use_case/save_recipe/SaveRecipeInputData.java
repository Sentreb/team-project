package use_case.save_recipe;

/**
 * The input data for the Save Recipe use case: which recipe the user picked
 * from the search results.
 */
public class SaveRecipeInputData {

    private final String apiId;

    /**
     * Creates the input data for saving one recipe.
     *
     * @param apiId the TheMealDB id of the recipe to save
     */
    public SaveRecipeInputData(String apiId) {
        this.apiId = apiId;
    }

    /**
     * Returns the TheMealDB id of the recipe to save.
     *
     * @return the recipe's apiId
     */
    public String getApiId() {
        return apiId;
    }
}
