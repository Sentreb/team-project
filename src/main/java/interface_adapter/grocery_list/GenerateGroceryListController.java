package interface_adapter.grocery_list;

import use_case.generate_grocery_list.GenerateGroceryListInputBoundary;

/**
 * Turns the grocery list screen's "generate" request into a Generate Grocery
 * List call.
 */
public class GenerateGroceryListController {

  private final GenerateGroceryListInputBoundary interactor;

  /**
   * Creates the controller.
   *
   * @param interactor the Generate Grocery List use case
   */
  public GenerateGroceryListController(GenerateGroceryListInputBoundary interactor) {
    this.interactor = interactor;
  }

  /**
   * Asks for a grocery list made from the current weekly plan.
   */
  public void execute() {
    interactor.execute();
  }
}
