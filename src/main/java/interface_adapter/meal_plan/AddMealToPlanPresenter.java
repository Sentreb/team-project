package interface_adapter.meal_plan;

import use_case.add_meal_to_plan.AddMealToPlanOutputBoundary;
import use_case.add_meal_to_plan.AddMealToPlanOutputData;
import use_case.add_meal_to_plan.PlannedMeal;

/**
 * Turns Add Meal to Plan results into meal plan screen state. This is where the
 * use case's data becomes plain strings, so the view never sees entity types.
 */
public class AddMealToPlanPresenter implements AddMealToPlanOutputBoundary {
    private final MealPlanViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the meal plan view model to update
     */
    public AddMealToPlanPresenter(MealPlanViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(AddMealToPlanOutputData outputData) {
        MealPlanState state = viewModel.getState();
        state.clearPlannedMeals();
        for (PlannedMeal meal : outputData.getPlannedMeals()) {
            state.setRecipeName(meal.getDay().name(), meal.getMealType().name(),
                    meal.getRecipeName());
        }
        state.setStatusMessage("Added " + outputData.getAddedRecipeName() + " to your plan.");
        state.setErrorMessage(null);
        state.setRecipeToReplace(null);
        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareConfirmReplaceView(String currentRecipeName) {
        MealPlanState state = viewModel.getState();
        state.setRecipeToReplace(currentRecipeName);
        state.setErrorMessage(null);
        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        MealPlanState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setRecipeToReplace(null);
        viewModel.notifyStateChanged();
    }
}
