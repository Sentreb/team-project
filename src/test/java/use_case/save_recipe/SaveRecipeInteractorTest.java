package use_case.save_recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.Ingredient;
import entity.Recipe;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import use_case.common.RecipeServiceException;

class SaveRecipeInteractorTest {

    private static final String API_ID = "52772";

    @Test
    void successSavesRecipeAndPreparesSuccessView() {
        // Arrange: empty collection, gateway returns the sample recipe
        InMemorySaveRecipeDataAccess dataAccess = new InMemorySaveRecipeDataAccess();
        FakeRecipeDetailsGateway gateway = new FakeRecipeDetailsGateway(sampleRecipe());
        RecordingPresenter presenter = new RecordingPresenter();
        SaveRecipeInputBoundary interactor =
                new SaveRecipeInteractor(dataAccess, gateway, presenter);

        // Act
        interactor.execute(new SaveRecipeInputData(API_ID));

        // Assert: recipe is saved, success view got its name, fail view not called
        assertTrue(dataAccess.existsByApiId(API_ID));
        assertEquals(1, dataAccess.count());
        assertNotNull(presenter.successData);
        assertEquals("Teriyaki Chicken Casserole", presenter.successData.getRecipeName());
        assertNull(presenter.failMessage);
    }

    @Test
    void alreadySavedFailsWithoutFetchingOrSaving() {
        // Arrange: collection already holds the recipe (extension 2a)
        InMemorySaveRecipeDataAccess dataAccess = new InMemorySaveRecipeDataAccess();
        FakeRecipeDetailsGateway gateway = new FakeRecipeDetailsGateway(sampleRecipe());
        RecordingPresenter presenter = new RecordingPresenter();
        SaveRecipeInputBoundary interactor =
                new SaveRecipeInteractor(dataAccess, gateway, presenter);
        dataAccess.save(sampleRecipe());

        // Act
        interactor.execute(new SaveRecipeInputData(API_ID));

        // Assert: fail message, gateway never called, collection unchanged
        assertEquals("This recipe is already in your collection.", presenter.failMessage);
        assertEquals(0, gateway.getFetchCount());
        assertEquals(1, dataAccess.count());
        assertNull(presenter.successData);
    }

    @Test
    void serviceErrorFailsWithoutSaving() {
        // Arrange: empty collection, gateway throws (extension 3a).
        InMemorySaveRecipeDataAccess dataAccess = new InMemorySaveRecipeDataAccess();
        FakeRecipeDetailsGateway gateway =
                new FakeRecipeDetailsGateway(null);
        RecordingPresenter presenter = new RecordingPresenter();
        SaveRecipeInputBoundary interactor =
                new SaveRecipeInteractor(dataAccess, gateway, presenter);

        // Act
        interactor.execute(new SaveRecipeInputData(API_ID));

        // Assert: fail message, nothing saved, success view not called
        assertEquals("Could not save this recipe. Please try again.", presenter.failMessage);
        assertEquals(1, gateway.getFetchCount());
        assertEquals(0, dataAccess.count());
        assertNull(presenter.successData);
    }

    /** Builds a small valid recipe with the test's API_ID. */
    private static Recipe sampleRecipe() {
        List<Ingredient> ingredients = List.of(
                new Ingredient("Soy Sauce", "3/4 cup"),
                new Ingredient("Water", "1/2 cup"));
        return new Recipe(API_ID, "Teriyaki Chicken Casserole", "Chicken", ingredients,
                "Preheat oven to 350F.", "");
    }

    /** Stores recipes in a map, keyed by apiId. */
    private static class InMemorySaveRecipeDataAccess implements SaveRecipeDataAccessInterface {
        private final Map<String, Recipe> recipes = new HashMap<>();

        @Override
        public boolean existsByApiId(String apiId) {
            return recipes.containsKey(apiId);
        }

        @Override
        public void save(Recipe recipe) {
            recipes.put(recipe.getApiId(), recipe);
        }

        /** Test helper: how many recipes are saved. */
        int count() {
            return recipes.size();
        }
    }

    /** Returns a fixed recipe or throws, and counts how often it was asked. */
    private static class FakeRecipeDetailsGateway implements RecipeDetailsGateway {
        private final Recipe recipeToReturn;
        private int fetchCount;

        /** Pass null to make every fetch throw RecipeServiceException. */
        FakeRecipeDetailsGateway(Recipe recipeToReturn) {
            this.recipeToReturn = recipeToReturn;
        }

        @Override
        public Recipe fetchById(String apiId) throws RecipeServiceException {
            fetchCount++;
            if (recipeToReturn == null) {
                throw new RecipeServiceException("Service Unavailable");
            }
            return recipeToReturn;
        }

        int getFetchCount() {
            return fetchCount;
        }
    }

    /** Records what the interactor sent, so tests can assert on it. */
    private static class RecordingPresenter implements SaveRecipeOutputBoundary {
        private SaveRecipeOutputData successData;
        private String failMessage;

        @Override
        public void prepareSuccessView(SaveRecipeOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.failMessage = errorMessage;
        }
    }
}
