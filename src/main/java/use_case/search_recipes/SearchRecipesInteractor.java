package use_case.search_recipes;

import use_case.common.RecipeServiceException;

import java.util.List;

/**
 * The interactor for the Search Recipes use case (README use case 1). Sends
 * the search to the recipe service by name or by ingredient and presents the
 * matches.
 */
public class SearchRecipesInteractor implements SearchRecipesInputBoundary {
    static final String BLANK_QUERY_MESSAGE = "Please enter a search term.";
    static final String NO_RESULTS_MESSAGE = "No recipes found.";
    static final String SERVICE_ERROR_MESSAGE = "The search failed. Please try again.";

    private final SearchRecipesGateway gateway;
    private final SearchRecipesOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param gateway   where matching recipes come from
     * @param presenter receives the success or fail result
     */
    public SearchRecipesInteractor(SearchRecipesGateway gateway,
                                   SearchRecipesOutputBoundary presenter) {
        this.gateway = gateway;
        this.presenter = presenter;
    }

    /**
     * Searches for recipes. Presents an error, without calling the recipe
     * service, if the search text is null or blank. Presents an error if
     * nothing matches or the service fails.
     *
     * @param inputData the search text and the kind of search
     */
    @Override
    public void execute(SearchRecipesInputData inputData) {
        String query = inputData.getQuery();
        if (query == null || query.isBlank()) {
            presenter.prepareFailView(BLANK_QUERY_MESSAGE);
            return;
        }
        query = query.trim();

        List<RecipeSummary> results;
        try {
            if (inputData.getSearchType() == SearchType.INGREDIENT) {
                results = gateway.searchByIngredient(query);
            } else {
                results = gateway.searchByName(query);
            }
        } catch (RecipeServiceException e) {
            presenter.prepareFailView(SERVICE_ERROR_MESSAGE);
            return;
        }

        if (results == null || results.isEmpty()) {
            presenter.prepareFailView(NO_RESULTS_MESSAGE);
            return;
        }
        presenter.prepareSuccessView(new SearchRecipesOutputData(results));
    }
}