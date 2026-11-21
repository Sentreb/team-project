package interface_adapter.search;

import use_case.search_recipes.RecipeSummary;
import use_case.search_recipes.SearchRecipesOutputBoundary;
import use_case.search_recipes.SearchRecipesOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Turns the Search Recipes use case's output into SearchViewModel state for the view.
 */
public class SearchPresenter implements SearchRecipesOutputBoundary {
    static final String NO_RESULTS_MESSAGE = "No recipes found.";

    private final SearchViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the view model the search view listens to
     */
    public SearchPresenter(SearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Replaces the displayed results with the matches. If there are none, the
     * list is cleared and the user is told nothing was found (use case 1, 2b).
     */
    @Override
    public void prepareSuccessView(SearchRecipesOutputData outputData) {
        List<SearchResultItem> items = new ArrayList<>();
        for (RecipeSummary summary : outputData.getResults()) {
            items.add(new SearchResultItem(summary.getApiId(), summary.getName(),
                    summary.getCategory(), summary.getImageUrl()));
        }

        SearchState state = new SearchState();
        state.setResults(items);
        state.setMessage(items.isEmpty() ? NO_RESULTS_MESSAGE : "");
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    /**
     * Shows the error but keeps the previously displayed results (use case 1, 2a).
     */
    @Override
    public void prepareFailView(String errorMessage) {
        SearchState state = viewModel.getState();
        state.setMessage(errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}