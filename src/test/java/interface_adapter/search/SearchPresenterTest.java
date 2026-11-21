package interface_adapter.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.search_recipes.RecipeSummary;
import use_case.search_recipes.SearchRecipesOutputData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchPresenterTest {

    private SearchViewModel viewModel;
    private SearchPresenter presenter;
    private int notifications;

    @BeforeEach
    void setUp() {
        viewModel = new SearchViewModel();
        notifications = 0;
        viewModel.addPropertyChangeListener(evt -> notifications++);
        presenter = new SearchPresenter(viewModel);
    }

    @Test
    void successShowsRecipesAndClearsMessage() {
        presenter.prepareSuccessView(new SearchRecipesOutputData(List.of(
                new RecipeSummary("1", "Corba", "Side", "corba.jpg"),
                new RecipeSummary("2", "Big Mac", "Beef", "mac.jpg"))));

        SearchState state = viewModel.getState();
        assertEquals(2, state.getResults().size());
        assertEquals("1", state.getResults().get(0).getApiId());
        assertEquals("Corba", state.getResults().get(0).getName());
        assertEquals("Side", state.getResults().get(0).getCategory());
        assertEquals("mac.jpg", state.getResults().get(1).getImageUrl());
        assertEquals("", state.getMessage());
        assertEquals(1, notifications);
    }

    @Test
    void noMatchesClearsOldResultsAndSaysNoneFound() {
        presenter.prepareSuccessView(new SearchRecipesOutputData(List.of(
                new RecipeSummary("1", "Corba", "Side", ""))));

        presenter.prepareSuccessView(new SearchRecipesOutputData(List.of()));

        SearchState state = viewModel.getState();
        assertTrue(state.getResults().isEmpty());
        assertEquals("No recipes found.", state.getMessage());
    }

    @Test
    void failKeepsPreviousResultsAndShowsError() {
        presenter.prepareSuccessView(new SearchRecipesOutputData(List.of(
                new RecipeSummary("1", "Corba", "Side", ""))));

        presenter.prepareFailView("Search failed. Please try again.");

        SearchState state = viewModel.getState();
        assertEquals(1, state.getResults().size());
        assertEquals("Search failed. Please try again.", state.getMessage());
        assertEquals(2, notifications);
    }
}