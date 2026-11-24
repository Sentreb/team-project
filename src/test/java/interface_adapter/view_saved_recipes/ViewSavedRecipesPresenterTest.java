package interface_adapter.view_saved_recipes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.view_saved_recipes.SavedRecipeSummary;
import use_case.view_saved_recipes.ViewSavedRecipesOutputData;

class ViewSavedRecipesPresenterTest {

  private static final String EMPTY_MESSAGE = "No saved recipes yet.";

  private ViewSavedRecipesViewModel viewModel;
  private CountingListener listener;
  private ViewSavedRecipesPresenter presenter;

  @BeforeEach
  void setUp() {
    viewModel = new ViewSavedRecipesViewModel();
    listener = new CountingListener();
    viewModel.addListener(listener);
    presenter = new ViewSavedRecipesPresenter(viewModel);
  }

  @Test
  void namesAreCopiedInSavedOrder() {
    presenter.prepareSuccessView(outputData("Pancakes", "Tacos", "Lasagne"));

    assertEquals(List.of("Pancakes", "Tacos", "Lasagne"),
        viewModel.getState().getRecipeNames());
    assertNull(viewModel.getState().getMessage());
  }

  @Test
  void emptyListSetsMessage() {
    presenter.prepareSuccessView(outputData());

    assertTrue(viewModel.getState().getRecipeNames().isEmpty());
    assertEquals(EMPTY_MESSAGE, viewModel.getState().getMessage());
  }

  @Test
  void messageIsClearedWhenRecipesArrive() {
    presenter.prepareSuccessView(outputData());

    presenter.prepareSuccessView(outputData("Tacos"));

    assertNull(viewModel.getState().getMessage());
    assertEquals(List.of("Tacos"), viewModel.getState().getRecipeNames());
  }

  @Test
  void oldNamesAreClearedWhenEmpty() {
    presenter.prepareSuccessView(outputData("Tacos"));

    presenter.prepareSuccessView(outputData());

    assertTrue(viewModel.getState().getRecipeNames().isEmpty());
    assertEquals(EMPTY_MESSAGE, viewModel.getState().getMessage());
  }

  @Test
  void listenerIsNotifiedOncePerResult() {
    presenter.prepareSuccessView(outputData("Tacos"));

    assertEquals(1, listener.callCount);
  }

  @Test
  void recipeNamesAreReadOnly() {
    presenter.prepareSuccessView(outputData("Tacos"));
    List<String> names = viewModel.getState().getRecipeNames();

    assertThrows(UnsupportedOperationException.class, () -> names.add("Lasagne"));
  }

  private static ViewSavedRecipesOutputData outputData(String... names) {
    List<SavedRecipeSummary> summaries = new ArrayList<>();
    for (String name : names) {
      summaries.add(new SavedRecipeSummary("id-" + name, name));
    }
    return new ViewSavedRecipesOutputData(summaries);
  }

  private static class CountingListener implements ViewSavedRecipesViewModelListener {
    int callCount = 0;

    @Override
    public void stateChanged() {
      callCount++;
    }
  }
}
