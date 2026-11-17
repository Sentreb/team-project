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
    private final SavedRecipeLookup recipeLookup;
    private final AddMealToPlanDataAccessInterface planDataAccess;
    private final AddMealToPlanOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param recipeLookup   where saved recipes are found
     * @param planDataAccess where the weekly plan is read and stored
     * @param presenter      receives the result
     */
    public AddMealToPlanInteractor(SavedRecipeLookup recipeLookup,
                                   AddMealToPlanDataAccessInterface planDataAccess,
                                   AddMealToPlanOutputBoundary presenter) {
        this.recipeLookup = recipeLookup;
        this.planDataAccess = planDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddMealToPlanInputData inputData) {
        Recipe recipe = recipeLookup.getSavedRecipe(inputData.getRecipeApiId());
        if (recipe == null) {
            presenter.prepareFailView("That recipe is not in your saved recipes.");
            return;
        }

        MealPlanEntry current = planDataAccess.getMealPlan()
                .getEntry(inputData.getDay(), inputData.getMealType());
        if (current != null && !inputData.isReplaceExisting()) {
            presenter.prepareConfirmReplaceView(current.getRecipe().getName());
            return;
        }

        MealPlanEntry entry = new MealPlanEntry(inputData.getDay(), inputData.getMealType(), recipe);
        try {
            planDataAccess.saveEntry(entry);
        }
        catch (MealPlanStorageException exception) {
            presenter.prepareFailView("The meal was not added. Please try again.");
            return;
        }

        MealPlan updatedPlan = planDataAccess.getMealPlan();
        presenter.prepareSuccessView(
                new AddMealToPlanOutputData(recipe.getName(), toPlannedMeals(updatedPlan)));
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
