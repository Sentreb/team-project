package use_case.search_recipes;

/**
 * Input boundary for the Search Recipes use case. The controller calls this;
 * the interactor implements it.
 */
public interface SearchRecipesInputBoundary {

    /**
     * Searches for recipes and presents the matches, or an error if the
     * search text is blank, nothing matches, or the recipe service fails.
     *
     * @param inputData the search text and the kind of search
     */
    void execute(SearchRecipesInputData inputData);
}