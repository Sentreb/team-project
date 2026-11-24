package interface_adapter.grocery_list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Everything the grocery list screen shows, as plain strings, so the view
 * never depends on entities.
 */
public class GroceryListState {

  private final List<GroceryListItem> items = new ArrayList<>();
  private String errorMessage;

  /**
   * Returns the lines of the grocery list, in display order.
   *
   * @return a read-only list, empty before a list is generated
   */
  public List<GroceryListItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  /**
   * Replaces the lines of the grocery list.
   *
   * @param items the new lines, in display order
   */
  public void setItems(List<GroceryListItem> items) {
    this.items.clear();
    this.items.addAll(items);
  }

  /**
   * Returns the error to show the user, or null if the last request succeeded.
   *
   * @return the error message, or null
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * Sets the error to show the user.
   *
   * @param errorMessage the error message, or null to show none
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
