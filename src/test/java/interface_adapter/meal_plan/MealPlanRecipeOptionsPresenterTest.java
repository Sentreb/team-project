package interface_adapter.meal_plan;

import org.junit.jupiter.api.Test;
import use_case.view_saved_recipes.SavedRecipeSummary;
import use_case.view_saved_recipes.ViewSavedRecipesOutputData;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MealPlanRecipeOptionsPresenterTest {

    private int notifications;

    @Test
    void savedRecipesBecomeDropDownOptionsInSavedOrder() {
        MealPlanViewModel viewModel = new MealPlanViewModel();
        viewModel.addListener(() -> notifications++);
        MealPlanRecipeOptionsPresenter presenter = new MealPlanRecipeOptionsPresenter(viewModel);

        presenter.prepareSuccessView(new ViewSavedRecipesOutputData(List.of(
                new SavedRecipeSummary("52854", "Pancakes"),
                new SavedRecipeSummary("52772", "Teriyaki Chicken Casserole"))));

        Map<String, String> options = viewModel.getState().getRecipeOptions();
        assertEquals(List.of("52854", "52772"), List.copyOf(options.keySet()));
        assertEquals("Pancakes", options.get("52854"));
        assertEquals(1, notifications);
    }

    @Test
    void emptyCollectionClearsOldOptions() {
        MealPlanViewModel viewModel = new MealPlanViewModel();
        viewModel.getState().setRecipeOptions(Map.of("52854", "Pancakes"));
        MealPlanRecipeOptionsPresenter presenter = new MealPlanRecipeOptionsPresenter(viewModel);

        presenter.prepareSuccessView(new ViewSavedRecipesOutputData(List.of()));

        assertTrue(viewModel.getState().getRecipeOptions().isEmpty());
    }
}

