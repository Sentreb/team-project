package use_case.search_recipes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.common.RecipeServiceException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchRecipesInteractorTest {

    private FakeGateway gateway;
    private RecordingPresenter presenter;
    private SearchRecipesInteractor interactor;

    private static RecipeSummary summary(String id, String name) {
        return new RecipeSummary(id, name, "Chicken", "http://img/" + id);
    }

    @BeforeEach
    void setUp() {
        gateway = new FakeGateway();
        presenter = new RecordingPresenter();
        interactor = new SearchRecipesInteractor(gateway, presenter);
    }

    // ---------- success ----------

    @Test
    void nameSearchPresentsResultsInOrder() {
        gateway.nameResults = List.of(summary("1", "Teriyaki"), summary("2", "Tikka"));

        interactor.execute(new SearchRecipesInputData("chicken", SearchType.NAME));

        assertNotNull(presenter.successData);
        assertNull(presenter.failMessage);
        List<RecipeSummary> results = presenter.successData.getResults();
        assertEquals(2, results.size());
        assertEquals("1", results.get(0).getApiId());
        assertEquals("Tikka", results.get(1).getName());
    }

    @Test
    void nameSearchOnlyCallsNameEndpoint() {
        gateway.nameResults = List.of(summary("1", "Teriyaki"));

        interactor.execute(new SearchRecipesInputData("chicken", SearchType.NAME));

        assertEquals(List.of("chicken"), gateway.nameQueries);
        assertTrue(gateway.ingredientQueries.isEmpty());
    }

    @Test
    void ingredientSearchOnlyCallsIngredientEndpoint() {
        gateway.ingredientResults = List.of(summary("3", "Onion Soup"));

        interactor.execute(new SearchRecipesInputData("onion", SearchType.INGREDIENT));

        assertEquals(List.of("onion"), gateway.ingredientQueries);
        assertTrue(gateway.nameQueries.isEmpty());
        assertEquals("Onion Soup", presenter.successData.getResults().get(0).getName());
    }

    @Test
    void singleResultIsPresented() {
        gateway.nameResults = List.of(summary("1", "Teriyaki"));

        interactor.execute(new SearchRecipesInputData("teriyaki", SearchType.NAME));

        assertEquals(1, presenter.successData.getResults().size());
    }

    @Test
    void queryIsTrimmedBeforeSearching() {
        gateway.nameResults = List.of(summary("1", "Teriyaki"));

        interactor.execute(new SearchRecipesInputData("  chicken \t", SearchType.NAME));

        assertEquals(List.of("chicken"), gateway.nameQueries);
    }

    @Test
    void emptyCategoryFromFilterEndpointIsKept() {
        gateway.ingredientResults = List.of(new RecipeSummary("9", "Soup", "", "http://img/9"));

        interactor.execute(new SearchRecipesInputData("onion", SearchType.INGREDIENT));

        assertEquals("", presenter.successData.getResults().get(0).getCategory());
    }

    // ---------- blank / null query ----------

    @Test
    void emptyQueryFailsWithoutCallingGateway() {
        interactor.execute(new SearchRecipesInputData("", SearchType.NAME));

        assertEquals("Please enter a search term.", presenter.failMessage);
        assertNull(presenter.successData);
        assertEquals(0, gateway.totalCalls());
    }

    @Test
    void whitespaceOnlyQueryFailsWithoutCallingGateway() {
        interactor.execute(new SearchRecipesInputData("   \t ", SearchType.INGREDIENT));

        assertEquals("Please enter a search term.", presenter.failMessage);
        assertNull(presenter.successData);
        assertEquals(0, gateway.totalCalls());
    }

    @Test
    void nullQueryFailsWithoutCallingGateway() {
        interactor.execute(new SearchRecipesInputData(null, SearchType.NAME));

        assertEquals("Please enter a search term.", presenter.failMessage);
        assertNull(presenter.successData);
        assertEquals(0, gateway.totalCalls());
    }

    // ---------- no results ----------

    @Test
    void noNameResultsFails() {
        gateway.nameResults = List.of();

        interactor.execute(new SearchRecipesInputData("zzzz", SearchType.NAME));

        assertEquals("No recipes found.", presenter.failMessage);
        assertNull(presenter.successData);
    }

    @Test
    void noIngredientResultsFails() {
        gateway.ingredientResults = List.of();

        interactor.execute(new SearchRecipesInputData("zzzz", SearchType.INGREDIENT));

        assertEquals("No recipes found.", presenter.failMessage);
        assertNull(presenter.successData);
    }

    @Test
    void nullResultsFromGatewayAreTreatedAsNoResults() {
        gateway.nameResults = null;

        interactor.execute(new SearchRecipesInputData("zzzz", SearchType.NAME));

        assertEquals("No recipes found.", presenter.failMessage);
        assertNull(presenter.successData);
    }

    // ---------- service errors ----------

    @Test
    void nameServiceErrorFails() {
        gateway.error = new RecipeServiceException("Could not reach recipe service");

        interactor.execute(new SearchRecipesInputData("chicken", SearchType.NAME));

        assertEquals("The search failed. Please try again.", presenter.failMessage);
        assertNull(presenter.successData);
    }

    @Test
    void ingredientServiceErrorFails() {
        gateway.error = new RecipeServiceException("Recipe service returned HTTP 500");

        interactor.execute(new SearchRecipesInputData("onion", SearchType.INGREDIENT));

        assertEquals("The search failed. Please try again.", presenter.failMessage);
        assertNull(presenter.successData);
    }

    @Test
    void serviceErrorMessageDoesNotLeakInternalDetails() {
        gateway.error = new RecipeServiceException("HTTP 500 at https://internal/url");

        interactor.execute(new SearchRecipesInputData("chicken", SearchType.NAME));

        assertFalse(presenter.failMessage.contains("500"));
    }

    // ---------- output / input data ----------

    @Test
    void outputDataIsNotAffectedByLaterChangesToSourceList() {
        List<RecipeSummary> source = new ArrayList<>(List.of(summary("1", "Teriyaki")));

        SearchRecipesOutputData data = new SearchRecipesOutputData(source);
        source.add(summary("2", "Tikka"));

        assertEquals(1, data.getResults().size());
    }

    @Test
    void outputDataResultsCannotBeModified() {
        SearchRecipesOutputData data =
                new SearchRecipesOutputData(List.of(summary("1", "Teriyaki")));

        assertThrows(UnsupportedOperationException.class,
                () -> data.getResults().add(summary("2", "Tikka")));
    }

    @Test
    void inputDataKeepsValues() {
        SearchRecipesInputData data = new SearchRecipesInputData("rice", SearchType.INGREDIENT);

        assertEquals("rice", data.getQuery());
        assertEquals(SearchType.INGREDIENT, data.getSearchType());
    }

    @Test
    void inputDataWithNullSearchTypeThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new SearchRecipesInputData("rice", null));
    }

    // ---------- test doubles ----------

    /** Returns fixed results or throws, and records every query it receives. */
    private static class FakeGateway implements SearchRecipesGateway {
        List<RecipeSummary> nameResults = List.of();
        List<RecipeSummary> ingredientResults = List.of();
        RecipeServiceException error;
        final List<String> nameQueries = new ArrayList<>();
        final List<String> ingredientQueries = new ArrayList<>();

        @Override
        public List<RecipeSummary> searchByName(String name) throws RecipeServiceException {
            nameQueries.add(name);
            if (error != null) {
                throw error;
            }
            return nameResults;
        }

        @Override
        public List<RecipeSummary> searchByIngredient(String ingredient)
                throws RecipeServiceException {
            ingredientQueries.add(ingredient);
            if (error != null) {
                throw error;
            }
            return ingredientResults;
        }

        int totalCalls() {
            return nameQueries.size() + ingredientQueries.size();
        }
    }

    /** Records what the interactor presented; a field stays null if its method was not called. */
    private static class RecordingPresenter implements SearchRecipesOutputBoundary {
        SearchRecipesOutputData successData;
        String failMessage;

        @Override
        public void prepareSuccessView(SearchRecipesOutputData outputData) {
            successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failMessage = errorMessage;
        }
    }
}