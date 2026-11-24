package app;

import interface_adapter.meal_plan.AddMealToPlanController;
import interface_adapter.meal_plan.AddMealToPlanPresenter;
import interface_adapter.meal_plan.MealPlanViewModel;
import use_case.add_meal_to_plan.AddMealToPlanDataAccessInterface;
import use_case.add_meal_to_plan.AddMealToPlanInteractor;
import use_case.add_meal_to_plan.SavedRecipeLookup;
import view.MealPlanView;

/**
 * Builds the meal plan screen with the Add Meal to Plan use case wired in.
 * Keeping the wiring here means adding the screen to the app is a single call
 * from AppBuilder, so the shared app can include it without knowing its internals.
 */
public final class MealPlanUseCaseFactory {

    private MealPlanUseCaseFactory() {
    }

    /**
     * Creates the meal plan screen and the presenter, interactor and controller behind it.
     *
     * @param viewModel      the meal plan view model, shared by the presenter and the view
     * @param recipeLookup   where saved recipes are found
     * @param planDataAccess where the weekly plan is read and stored
     * @return the meal plan screen
     */
    public static MealPlanView create(MealPlanViewModel viewModel,
                                      SavedRecipeLookup recipeLookup,
                                      AddMealToPlanDataAccessInterface planDataAccess) {
        AddMealToPlanPresenter presenter = new AddMealToPlanPresenter(viewModel);
        AddMealToPlanInteractor interactor =
                new AddMealToPlanInteractor(recipeLookup, planDataAccess, presenter);
        AddMealToPlanController controller = new AddMealToPlanController(interactor);
        return new MealPlanView(viewModel, controller);
    }
}

