package use_case.add_meal_to_plan;

/**
 * Input boundary for the Add Meal to Plan use case.
 */
public interface AddMealToPlanInputBoundary {

    /**
     * Adds the requested saved recipe to a slot of the weekly plan.
     *
     * @param inputData the recipe, day, meal slot and whether replacing is allowed
     */
    void execute(AddMealToPlanInputData inputData);
}

