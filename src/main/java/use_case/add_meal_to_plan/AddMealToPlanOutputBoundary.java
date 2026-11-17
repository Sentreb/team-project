package use_case.add_meal_to_plan;

/**
 * Output boundary for the Add Meal to Plan use case.
 */
public interface AddMealToPlanOutputBoundary {

    /**
     * Shows the updated weekly plan after a meal was added.
     *
     * @param outputData the added recipe and the updated plan
     */
    void prepareSuccessView(AddMealToPlanOutputData outputData);

    /**
     * Asks the user whether to replace the meal already in the chosen slot.
     *
     * @param currentRecipeName the name of the recipe currently in the slot
     */
    void prepareConfirmReplaceView(String currentRecipeName);

    /**
     * Tells the user the meal was not added.
     *
     * @param errorMessage why the meal was not added
     */
    void prepareFailView(String errorMessage);
}
