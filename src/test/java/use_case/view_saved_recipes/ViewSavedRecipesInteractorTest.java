package use_case.view_saved_recipes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.Recipe;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ViewSavedRecipesInteractorTest {

  @Test
  void severalRecipesAreListedInSavedOrder() {
    // Arrange: a fake holding three recipes, in an order that is neither
    //          alphabetical by name nor sorted by apiId
    InMemoryViewSavedRecipesDataAccess dataAccess = new InMemoryViewSavedRecipesDataAccess();
    dataAccess.add(recipe("300", "Pancakes"));
    dataAccess.add(recipe("100", "Tacos"));
    dataAccess.add(recipe("200", "Lasagne"));
    RecordingPresenter presenter = new RecordingPresenter();
    ViewSavedRecipesInputBoundary interactor =
        new ViewSavedRecipesInteractor(dataAccess, presenter);

    // Act: execute the interactor
    interactor.execute();

    // Assert: success view called, three summaries, right apiId and name at each index
    assertNotNull(presenter.successData);
    List<SavedRecipeSummary> summaries = presenter.successData.getSavedRecipes();
    assertEquals(3, summaries.size());
    assertEquals("300", summaries.get(0).getApiId());
    assertEquals("Pancakes", summaries.get(0).getName());
    assertEquals("100", summaries.get(1).getApiId());
    assertEquals("Tacos", summaries.get(1).getName());
    assertEquals("200", summaries.get(2).getApiId());
    assertEquals("Lasagne", summaries.get(2).getName());
  }

  @Test
  void emptyCollectionSucceedsWithEmptyList() {
    // Arrange: a fake holding no recipes
    InMemoryViewSavedRecipesDataAccess dataAccess = new InMemoryViewSavedRecipesDataAccess();
    RecordingPresenter presenter = new RecordingPresenter();
    ViewSavedRecipesInputBoundary interactor =
        new ViewSavedRecipesInteractor(dataAccess, presenter);

    // Act: execute the interactor
    interactor.execute();

    // Assert: success view called, and its list is empty
    assertNotNull(presenter.successData);
    assertTrue(presenter.successData.getSavedRecipes().isEmpty());
  }

  @Test
  void outputListCannotBeModified() {
    // Arrange: output data built from a mutable list with one summary
    List<SavedRecipeSummary> source = new ArrayList<>();
    source.add(new SavedRecipeSummary("300", "Pancakes"));
    ViewSavedRecipesOutputData outputData = new ViewSavedRecipesOutputData(source);
    List<SavedRecipeSummary> summaries = outputData.getSavedRecipes();
    SavedRecipeSummary extra = new SavedRecipeSummary("100", "Tacos");

    // Act and Assert: adding to the returned list throws
    assertThrows(UnsupportedOperationException.class, () -> summaries.add(extra));
  }

  @Test
  void changingSourceListDoesNotChangeOutputData() {
    // Arrange: a mutable list with one summary, and output data built from it
    List<SavedRecipeSummary> source = new ArrayList<>();
    source.add(new SavedRecipeSummary("300", "Pancakes"));
    ViewSavedRecipesOutputData outputData = new ViewSavedRecipesOutputData(source);

    // Act: add a second summary to the source list
    source.add(new SavedRecipeSummary("100", "Tacos"));

    // Assert: the output data still holds one summary
    assertEquals(1, outputData.getSavedRecipes().size());
  }

  /** Builds a small valid recipe with the given id and name. */
  private static Recipe recipe(String apiId, String name) {
    return new Recipe(apiId, name, "", List.of(), "", "");
  }

  /** Keeps saved recipes in a list, in saved order. */
  private static class InMemoryViewSavedRecipesDataAccess
      implements ViewSavedRecipesDataAccessInterface {

    private final List<Recipe> recipes = new ArrayList<>();

    /** Test helper: adds a recipe to the end of the collection. */
    void add(Recipe recipe) {
      recipes.add(recipe);
    }

    @Override
    public List<Recipe> getSavedRecipes() {
      return recipes;
    }
  }

  /** Remembers what the interactor sent. Null means the success view was not called. */
  private static class RecordingPresenter implements ViewSavedRecipesOutputBoundary {

    private ViewSavedRecipesOutputData successData;

    @Override
    public void prepareSuccessView(ViewSavedRecipesOutputData outputData) {
      successData = outputData;
    }
  }
}
