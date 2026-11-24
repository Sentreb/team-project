package interface_adapter.grocery_list;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import use_case.generate_grocery_list.GenerateGroceryListOutputBoundary;
import use_case.generate_grocery_list.GenerateGroceryListOutputData;

/**
 * Turns Generate Grocery List results into grocery list screen state.
 */
public class GenerateGroceryListPresenter implements GenerateGroceryListOutputBoundary {

  private final GroceryListViewModel viewModel;

  /**
   * Creates the presenter.
   *
   * @param viewModel the grocery list view model to update
   */
  public GenerateGroceryListPresenter(GroceryListViewModel viewModel) {
    this.viewModel = viewModel;
  }

  /**
   * Shows the new grocery list, one line per ingredient with its measures
   * formatted by {@link #formatMeasures(List)}, and clears any earlier error.
   *
   * @param outputData the generated list
   */
  @Override
  public void prepareSuccessView(GenerateGroceryListOutputData outputData) {
    List<GroceryListItem> items = new ArrayList<>();
    for (Map.Entry<String, List<String>> entry : outputData.getItems().entrySet()) {
      items.add(new GroceryListItem(entry.getKey(), formatMeasures(entry.getValue())));
    }
    GroceryListState state = viewModel.getState();
    state.setItems(items);
    state.setErrorMessage(null);
    viewModel.notifyStateChanged();
  }

  /**
   * Shows why no list was generated. The list already on screen stays, because
   * the saved list was not replaced.
   *
   * @param error a message for the user
   */
  @Override
  public void prepareFailView(String error) {
    viewModel.getState().setErrorMessage(error);
    viewModel.notifyStateChanged();
  }

  /**
   * Joins measures with ", ", showing a measure that appears more than once
   * a single time with its count, such as "8 oz × 3" for a recipe planned
   * three times. Measures are free text from TheMealDB, so only identical
   * measures (ignoring surrounding spaces) are grouped, never added up.
   *
   * @param measures the measures, in the order they were added
   * @return the measures in order of first appearance, such as "8 oz × 3, 2 slices"
   */
  private static String formatMeasures(List<String> measures) {
    Map<String, Integer> counts = new LinkedHashMap<>();
    for (String measure : measures) {
      counts.merge(measure.trim(), 1, Integer::sum);
    }
    List<String> parts = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : counts.entrySet()) {
      int count = entry.getValue();
      parts.add(count == 1 ? entry.getKey() : entry.getKey() + " × " + count);
    }
    return String.join(", ", parts);
  }
}
