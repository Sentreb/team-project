package interface_adapter.meal_plan;

import entity.MealType;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The view model for the meal plan screen. Holds the current state and tells
 * its listeners when the state changes.
 */
public class MealPlanViewModel {
    /** The name the view manager uses for the meal plan screen. */
    public static final String VIEW_NAME = "meal plan";

    private final List<MealPlanViewModelListener> listeners = new ArrayList<>();
    private MealPlanState state = new MealPlanState();

    /**
     * Returns the current state.
     *
     * @return the current meal plan state
     */
    public MealPlanState getState() {
        return state;
    }

    /**
     * Replaces the current state.
     *
     * @param state the new state
     */
    public void setState(MealPlanState state) {
        this.state = state;
    }

    /**
     * Returns the day names in plan order, such as "MONDAY", for the view's choices.
     *
     * @return a read-only list of day names
     */
    public List<String> getDays() {
        List<String> days = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            days.add(day.name());
        }
        return Collections.unmodifiableList(days);
    }

    /**
     * Returns the meal slot names in order, such as "BREAKFAST", for the view's choices.
     *
     * @return a read-only list of meal slot names
     */
    public List<String> getMealSlots() {
        List<String> slots = new ArrayList<>();
        for (MealType mealType : MealType.values()) {
            slots.add(mealType.name());
        }
        return Collections.unmodifiableList(slots);
    }

    /**
     * Registers a listener to be told when the state changes.
     *
     * @param listener the listener to add
     */
    public void addListener(MealPlanViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener that the state changed.
     */
    public void notifyStateChanged() {
        for (MealPlanViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
