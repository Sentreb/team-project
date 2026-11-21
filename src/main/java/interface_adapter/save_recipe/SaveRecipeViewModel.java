package interface_adapter.save_recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for Save Recipe. Holds the current state and tells its
 * listeners when the state changes.
 */
public class SaveRecipeViewModel {

    private final List<SaveRecipeViewModelListener> listeners = new ArrayList<>();

    private SaveRecipeState state = new SaveRecipeState();

    /**
     * Returns the current state.
     *
     * @return the current Save Recipe state
     */
    public SaveRecipeState getState() {
        return state;
    }

    /**
     * Replaces the current state.
     *
     * @param state the new state
     */
    public void setState(SaveRecipeState state) {
        this.state = state;
    }

    /**
     * Registers a listener to be told when the state changes.
     *
     * @param listener the listener to add
     */
    public void addListener(SaveRecipeViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener that the state changed.
     */
    public void notifyStateChanged() {
        for (SaveRecipeViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
