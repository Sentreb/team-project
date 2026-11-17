package use_case.search_recipes;

public class SearchRecipesInteractor implements SearchRecipesInputBoundary{
    private final SearchRecipesOutputBoundary searchRecipesOutputBoundary;
    private final SearchRecipesDataAccessInterface searchRecipesDataAccessInterface;
    private final SearchRecipesGateway searchRecipesGateway;

    public SearchRecipesInteractor(SearchRecipesOutputBoundary searchRecipesOutputBoundary,
                                   SearchRecipesDataAccessInterface searchRecipesDataAccessInterface,
                                   SearchRecipesGateway searchRecipesGateway) {
        this.searchRecipesOutputBoundary = searchRecipesOutputBoundary;
        this.searchRecipesDataAccessInterface = searchRecipesDataAccessInterface;
        this.searchRecipesGateway = searchRecipesGateway;
    }

    @Override
    public void execute(SearchRecipesInputData searchRecipesInputData) {
        // #TODO: Implement this!
    }
}
