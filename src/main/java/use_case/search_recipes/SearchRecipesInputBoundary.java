package use_case.search_recipes;

public interface SearchRecipesInputBoundary {
    /**
     * Execute the Search Recipes Use Case.
     * @param searchRecipesInputData the input data for this use case
     */
    public void execute(SearchRecipesInputData searchRecipesInputData);
}
