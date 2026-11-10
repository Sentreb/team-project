package use_case.generate_grocery_list;

import entity.MealPlan;
import entity.Recipe;

import java.util.List;

public class GenerateGroceryListInteractor implements GenerateGroceryListInputBoundary {
    private final GenerateGroceryListDataAccessInterface dataAccessInterface;
    private final GenerateGroceryListOutputBoundary presenter;

    public GenerateGroceryListInteractor(GenerateGroceryListDataAccessInterface dataAccessInterface, GenerateGroceryListOutputBoundary presenter) {
        this.dataAccessInterface = dataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        // 1. get meal plan from the DAO
        // 2. if it has no recipes -> prepareFailView("Add meals to your plan first"), return
        // 3. for each recipe in meal plan
        //      for each ingredient in recipe
        //          find GroceryItem(), if not found, create one
        //          addMeasure(ingredient's measure)
        // 4. save the list through the DAO (replaces the old one)
        // 5. prepareSuccessView
        MealPlan plan = dataAccessInterface.getMealPlan();
        List<Recipe> recipes = plan.getAllReciepes();
        if (recipes.isEmpty()) {
            presenter.prepareFailView("Add meals to your plan first.");
            return;
        }

        for (Recipe recipe: recipes) {
            for()
        }

    };
}
