package use_case.search_recipes;

/**
 * Output boundary for the Search Recipes use case. The interactor calls this;
 * the presenter implements it.
 */
public interface SearchRecipesOutputBoundary {

    /**
     * Shows the recipes that matched the search.
     *
     * @param outputData the matching recipes; never empty
     */
    void prepareSuccessView(SearchRecipesOutputData outputData);

    /**
     * Shows why no results were produced. Results already on screen should
     * stay.
     *
     * @param errorMessage a message for the user, such as that nothing matched
     */
    void prepareFailView(String errorMessage);
}