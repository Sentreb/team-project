package interface_adapter.search;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Holds the SearchState and notifies the view when it changes.
 * The presenter calls setState(...) followed by firePropertyChanged().
 */
public class SearchViewModel {
    public static final String VIEW_NAME = "search";
    public static final String TITLE_LABEL = "Search Recipes";
    public static final String STATE_PROPERTY = "state";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private SearchState state = new SearchState();

    public SearchState getState() {
        return state;
    }

    public void setState(SearchState state) {
        this.state = state;
    }

    public void firePropertyChanged() {
        // old value is null so the event always fires, even if the same state object is reused
        support.firePropertyChange(STATE_PROPERTY, null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}