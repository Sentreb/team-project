package interface_adapter.meal_plan;

/**
 * Something that wants to know when the meal plan state changes, such as the
 * meal plan view.
 */
public interface MealPlanViewModelListener {

    /**
     * The meal plan state was updated.
     */
    void stateChanged();
}

