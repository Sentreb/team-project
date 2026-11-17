package use_case.search_recipes;

public class SearchRecipesInputData {
    private final String searchPhrase;
    private final boolean searchByName;
    private final boolean searchByIngredient;

    public SearchRecipesInputData(String searchPhrase, boolean searchByName,
                                  boolean searchByIngredient) {
        this.searchPhrase = searchPhrase;
        this.searchByName = searchByName;
        this.searchByIngredient = searchByIngredient;
    }

    public String getSearchPhrase() {
        return searchPhrase;
    }

    public boolean isSearchByName() {
        return searchByName;
    }
    public boolean isSearchByIngredient() {
        return searchByIngredient;
    }
}
