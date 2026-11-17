package use_case.add_meal_to_plan;

import entity.MealPlan;
import entity.MealPlanEntry;
import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Puts a saved recipe in a day and meal slot of the weekly plan.
 */
public class AddMealToPlanInteractor implements AddMealToPlanInputBoundary {
    private final AddMealToPlanDataAccessInterface dataAccess;
    private final AddMealToPlanOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param dataAccess where saved recipes and the plan are read and stored
     * @param presenter  receives the result
     */
    public AddMealToPlanInteractor(AddMealToPlanDataAccessInterface dataAccess,
                                   AddMealToPlanOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddMealToPlanInputData inputData) {
        Recipe recipe = dataAccess.getSavedRecipe(inputData.getRecipeApiId());
        if (recipe == null) {
            presenter.prepareFailView("That recipe is not in your saved recipes.");
            return;
        }

        MealPlanEntry current = dataAccess.getMealPlan()
                .getEntry(inputData.getDay(), inputData.getMealType());
        if (current != null && !inputData.isReplaceExisting()) {
            presenter.prepareConfirmReplaceView(current.getRecipe().getName());
            return;
        }

        MealPlanEntry entry = new MealPlanEntry(inputData.getDay(), inputData.getMealType(), recipe);
        try {
            dataAccess.saveEntry(entry);
        }
        catch (MealPlanStorageException exception) {
            presenter.prepareFailView("The meal was not added. Please try again.");
            return;
        }

        presenter.prepareSuccessView(
                new AddMealToPlanOutputData(recipe.getName(), toPlannedMeals(dataAccess.getMealPlan())));
    }

    private static List<PlannedMeal> toPlannedMeals(MealPlan plan) {
        List<PlannedMeal> plannedMeals = new ArrayList<>();
        for (MealPlanEntry entry : plan.getEntries()) {
            plannedMeals.add(new PlannedMeal(entry.getDay(), entry.getMealType(),
                    entry.getRecipe().getName()));
        }
        return plannedMeals;
    }
}
