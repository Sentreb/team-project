package interface_adapter.meal_plan;

import entity.MealType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.add_meal_to_plan.AddMealToPlanOutputData;
import use_case.add_meal_to_plan.PlannedMeal;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AddMealToPlanPresenterTest {

    private MealPlanViewModel viewModel;
    private AddMealToPlanPresenter presenter;
    private int notifications;

    @BeforeEach
    void setUp() {
        viewModel = new MealPlanViewModel();
        viewModel.addListener(() -> notifications++);
        presenter = new AddMealToPlanPresenter(viewModel);
    }

    @Test
    void successShowsEveryPlannedMealAsStrings() {
        presenter.prepareSuccessView(new AddMealToPlanOutputData("Pancakes", List.of(
                new PlannedMeal(DayOfWeek.MONDAY, MealType.BREAKFAST, "Pancakes"),
                new PlannedMeal(DayOfWeek.FRIDAY, MealType.DINNER, "Teriyaki Chicken Casserole"))));

        MealPlanState state = viewModel.getState();
        assertEquals("Pancakes", state.getRecipeName("MONDAY", "BREAKFAST"));
        assertEquals("Teriyaki Chicken Casserole", state.getRecipeName("FRIDAY", "DINNER"));
        assertEquals("", state.getRecipeName("MONDAY", "LUNCH"));
        assertEquals("Added Pancakes to your plan.", state.getStatusMessage());
        assertNull(state.getErrorMessage());
        assertNull(state.getRecipeToReplace());
        assertEquals(1, notifications);
    }

    @Test
    void successDropsMealsNoLongerInThePlan() {
        presenter.prepareSuccessView(new AddMealToPlanOutputData("Pancakes", List.of(
                new PlannedMeal(DayOfWeek.MONDAY, MealType.BREAKFAST, "Pancakes"))));

        presenter.prepareSuccessView(new AddMealToPlanOutputData("Penne", List.of(
                new PlannedMeal(DayOfWeek.TUESDAY, MealType.LUNCH, "Penne"))));

        assertEquals("", viewModel.getState().getRecipeName("MONDAY", "BREAKFAST"));
        assertEquals("Penne", viewModel.getState().getRecipeName("TUESDAY", "LUNCH"));
    }

    @Test
    void confirmReplaceAsksAboutCurrentRecipe() {
        presenter.prepareConfirmReplaceView("Pancakes");

        assertEquals("Pancakes", viewModel.getState().getRecipeToReplace());
        assertNull(viewModel.getState().getErrorMessage());
        assertEquals(1, notifications);
    }

    @Test
    void failShowsErrorAndClearsPendingReplace() {
        presenter.prepareConfirmReplaceView("Pancakes");

        presenter.prepareFailView("The meal was not added. Please try again.");

        assertEquals("The meal was not added. Please try again.",
                viewModel.getState().getErrorMessage());
        assertNull(viewModel.getState().getRecipeToReplace());
        assertEquals(2, notifications);
    }
}

