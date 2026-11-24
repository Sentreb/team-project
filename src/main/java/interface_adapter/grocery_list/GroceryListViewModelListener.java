package interface_adapter.grocery_list;

/**
 * Something that wants to know when the grocery list state changes, such as
 * the grocery list view.
 */
public interface GroceryListViewModelListener {

  /**
   * The grocery list state was updated.
   */
  void stateChanged();
}
