package interface_adapter.grocery_list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.generate_grocery_list.GenerateGroceryListOutputData;

class GenerateGroceryListPresenterTest {

  private GroceryListViewModel viewModel;
  private GenerateGroceryListPresenter presenter;
  private int notifications;

  @BeforeEach
  void setUp() {
    viewModel = new GroceryListViewModel();
    viewModel.addListener(() -> notifications++);
    presenter = new GenerateGroceryListPresenter(viewModel);
  }

  @Test
  void successShowsEachIngredientWithItsMeasuresInOrder() {
    // Arrange: two ingredients, not in alphabetical order, one with two measures
    Map<String, List<String>> items = new LinkedHashMap<>();
    items.put("Onion", List.of("1", "2 slices"));
    items.put("Chicken", List.of("500g"));

    // Act
    presenter.prepareSuccessView(new GenerateGroceryListOutputData(items));

    // Assert: one line per ingredient, in order, measures joined, no error, notified once
    List<GroceryListItem> lines = viewModel.getState().getItems();
    assertEquals(2, lines.size());
    assertEquals("Onion", lines.get(0).getIngredientName());
    assertEquals("1, 2 slices", lines.get(0).getMeasures());
    assertEquals("Chicken", lines.get(1).getIngredientName());
    assertEquals("500g", lines.get(1).getMeasures());
    assertNull(viewModel.getState().getErrorMessage());
    assertEquals(1, notifications);
  }

  @Test
  void repeatedMeasureIsShownOnceWithItsCount() {
    // Arrange: the same recipe planned three times, so each measure comes three times
    Map<String, List<String>> items = new LinkedHashMap<>();
    items.put("Chicken", List.of("8 oz", "8 oz", "8 oz"));

    // Act
    presenter.prepareSuccessView(new GenerateGroceryListOutputData(items));

    // Assert: one measure with its count, not "8 oz, 8 oz, 8 oz"
    assertEquals("8 oz × 3", viewModel.getState().getItems().get(0).getMeasures());
  }

  @Test
  void onlyIdenticalMeasuresAreGroupedInFirstSeenOrder() {
    // Arrange: two different measures, one of them twice and once with extra spaces
    Map<String, List<String>> items = new LinkedHashMap<>();
    items.put("Onion", List.of("1", "2 slices", " 1 "));

    // Act
    presenter.prepareSuccessView(new GenerateGroceryListOutputData(items));

    // Assert: "1" is grouped, "2 slices" stays separate, order of first appearance kept
    assertEquals("1 × 2, 2 slices", viewModel.getState().getItems().get(0).getMeasures());
  }

  @Test
  void ingredientWithNoMeasuresShowsEmptyMeasures() {
    // Arrange: an ingredient whose measures were all blank in TheMealDB
    Map<String, List<String>> items = new LinkedHashMap<>();
    items.put("Salt", List.of());

    // Act
    presenter.prepareSuccessView(new GenerateGroceryListOutputData(items));

    // Assert: the line is still shown, with no measures
    GroceryListItem line = viewModel.getState().getItems().get(0);
    assertEquals("Salt", line.getIngredientName());
    assertEquals("", line.getMeasures());
  }

  @Test
  void newListReplacesThePreviousOne() {
    // Arrange: a list already on screen
    Map<String, List<String>> first = new LinkedHashMap<>();
    first.put("Onion", List.of("1"));
    presenter.prepareSuccessView(new GenerateGroceryListOutputData(first));
    Map<String, List<String>> second = new LinkedHashMap<>();
    second.put("Rice", List.of("2 cups"));

    // Act
    presenter.prepareSuccessView(new GenerateGroceryListOutputData(second));

    // Assert: only the new list is shown
    List<GroceryListItem> lines = viewModel.getState().getItems();
    assertEquals(1, lines.size());
    assertEquals("Rice", lines.get(0).getIngredientName());
  }

  @Test
  void failShowsErrorAndNotifiesOnce() {
    // Act
    presenter.prepareFailView("Add meals to your plan first.");

    // Assert: error shown, still no list, notified once
    assertEquals("Add meals to your plan first.", viewModel.getState().getErrorMessage());
    assertTrue(viewModel.getState().getItems().isEmpty());
    assertEquals(1, notifications);
  }

  @Test
  void failKeepsTheListAlreadyShown() {
    // Arrange: a list already on screen
    Map<String, List<String>> items = new LinkedHashMap<>();
    items.put("Onion", List.of("1"));
    presenter.prepareSuccessView(new GenerateGroceryListOutputData(items));

    // Act
    presenter.prepareFailView("Add meals to your plan first.");

    // Assert: the saved list was not replaced, so it stays on screen with the error
    assertEquals(1, viewModel.getState().getItems().size());
    assertEquals("Add meals to your plan first.", viewModel.getState().getErrorMessage());
  }

  @Test
  void successAfterFailClearsTheError() {
    // Arrange: an error on screen
    presenter.prepareFailView("Add meals to your plan first.");
    Map<String, List<String>> items = new LinkedHashMap<>();
    items.put("Onion", List.of("1"));

    // Act
    presenter.prepareSuccessView(new GenerateGroceryListOutputData(items));

    // Assert: the view never shows the old error next to the new list
    assertNull(viewModel.getState().getErrorMessage());
  }
}
