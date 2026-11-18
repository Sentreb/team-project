package use_case.search_recipes;

/**
 * The user's request to search for recipes: the text they entered and
 * whether it is a recipe name or a single ingredient.
 */
public class SearchRecipesInputData {
    private final String query;
    private final SearchType searchType;

    /**
     * Creates a search request.
     *
     * @param query      the text the user entered; may be null or blank, the
     *                   interactor reports that to the user
     * @param searchType whether to search by name or by ingredient
     * @throws IllegalArgumentException if searchType is null
     */
    public SearchRecipesInputData(String query, SearchType searchType) {
        if (searchType == null) {
            throw new IllegalArgumentException("Search type must not be null.");
        }
        this.query = query;
        this.searchType = searchType;
    }

    public String getQuery() {
        return query;
    }

    public SearchType getSearchType() {
        return searchType;
    }
}