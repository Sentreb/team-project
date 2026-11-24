package interface_adapter.grocery_list;

import java.util.ArrayList;
import java.util.List;

/**
 * The view model for the grocery list screen. Holds the current state and
 * tells its listeners when the state changes.
 */
public class GroceryListViewModel {

  /** The name the view manager uses for the grocery list screen. */
  public static final String VIEW_NAME = "grocery list";

  private final List<GroceryListViewModelListener> listeners = new ArrayList<>();
  private GroceryListState state = new GroceryListState();

  /**
   * Returns the current state.
   *
   * @return the current grocery list state
   */
  public GroceryListState getState() {
    return state;
  }

  /**
   * Replaces the current state.
   *
   * @param state the new state
   */
  public void setState(GroceryListState state) {
    this.state = state;
  }

  /**
   * Registers a listener to be told when the state changes.
   *
   * @param listener the listener to add
   */
  public void addListener(GroceryListViewModelListener listener) {
    listeners.add(listener);
  }

  /**
   * Tells every listener that the state changed.
   */
  public void notifyStateChanged() {
    for (GroceryListViewModelListener listener : listeners) {
      listener.stateChanged();
    }
  }
}
