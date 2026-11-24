package interface_adapter.view_saved_recipes;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for View Saved Recipes. Holds the current state and tells its
 * listeners when the state changes.
 */
public class ViewSavedRecipesViewModel {

  /** The name of this view in the card layout. */
  public static final String VIEW_NAME = "saved recipes";

  private final List<ViewSavedRecipesViewModelListener> listeners = new ArrayList<>();
  private final ViewSavedRecipesState state = new ViewSavedRecipesState();

  /**
   * Returns the current state.
   *
   * @return the current View Saved Recipes state
   */
  public ViewSavedRecipesState getState() {
    return state;
  }

  /**
   * Registers a listener to be told when the state changes.
   *
   * @param listener the listener to add
   */
  public void addListener(ViewSavedRecipesViewModelListener listener) {
    listeners.add(listener);
  }

  /**
   * Tells every listener that the state changed.
   */
  public void notifyStateChanged() {
    for (ViewSavedRecipesViewModelListener listener : listeners) {
      listener.stateChanged();
    }
  }
}
