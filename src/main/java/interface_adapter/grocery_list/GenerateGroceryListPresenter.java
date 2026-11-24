package interface_adapter.grocery_list;

import java.util.ArrayList;
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
   * joined by ", ", and clears any earlier error.
   *
   * @param outputData the generated list
   */
  @Override
  public void prepareSuccessView(GenerateGroceryListOutputData outputData) {
    List<GroceryListItem> items = new ArrayList<>();
    for (Map.Entry<String, List<String>> entry : outputData.getItems().entrySet()) {
      items.add(new GroceryListItem(entry.getKey(), String.join(", ", entry.getValue())));
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
}
