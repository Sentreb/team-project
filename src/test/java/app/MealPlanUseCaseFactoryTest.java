package app;

import data_access.InMemoryRecipeDataAccessObject;
import entity.Recipe;
import interface_adapter.meal_plan.MealPlanViewModel;
import interface_adapter.view_saved_recipes.ViewSavedRecipesController;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MealPlanUseCaseFactoryTest {

    @Test
    void recipeLoaderListsWhatIsSavedNow() {
        InMemoryRecipeDataAccessObject recipes = new InMemoryRecipeDataAccessObject();
        MealPlanViewModel viewModel = new MealPlanViewModel();
        ViewSavedRecipesController loader =
                MealPlanUseCaseFactory.createRecipeOptionsLoader(viewModel, recipes);

        recipes.save(new Recipe("52854", "Pancakes", "", List.of(), "", ""));
        loader.execute();
        assertEquals(Map.of("52854", "Pancakes"), viewModel.getState().getRecipeOptions());

        recipes.save(new Recipe("52772", "Teriyaki Chicken Casserole", "", List.of(), "", ""));
        loader.execute();
        assertEquals(List.of("52854", "52772"),
                List.copyOf(viewModel.getState().getRecipeOptions().keySet()));
    }
}

