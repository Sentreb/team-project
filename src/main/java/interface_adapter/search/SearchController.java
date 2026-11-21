package interface_adapter.search;

import use_case.search_recipes.SearchRecipesInputBoundary;
import use_case.search_recipes.SearchRecipesInputData;
import use_case.search_recipes.SearchType;

/**
 * STUB: replace the body once the Search Recipes interactor and its input boundary exist.
 * The view only depends on this one method, so the rest of the view will not need to change.
 */
public class SearchController {
    private final SearchRecipesInputBoundary searchRecipesInteractor;

    public SearchController(SearchRecipesInputBoundary searchRecipesInteractor) {
        this.searchRecipesInteractor = searchRecipesInteractor;
    }

    private static SearchType toSearchType(SearchMode mode) {
        switch (mode) {
            case BY_NAME:
                return SearchType.NAME;
            case BY_INGREDIENT:
                return SearchType.INGREDIENT;
            default:
                throw new IllegalArgumentException("Unknown search mode: " + mode);
        }
    }

    public void execute(String query, SearchMode searchMode) {
        SearchRecipesInputData searchRecipesInputData = new SearchRecipesInputData(query,
                                                                                    toSearchType(searchMode));
        this.searchRecipesInteractor.execute(searchRecipesInputData);
    }
}