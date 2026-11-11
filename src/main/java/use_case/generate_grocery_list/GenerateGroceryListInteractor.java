package use_case.generate_grocery_list;

import entity.GroceryItem;
import entity.Ingredient;
import entity.MealPlan;
import entity.Recipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenerateGroceryListInteractor implements GenerateGroceryListInputBoundary {
    private final GenerateGroceryListDataAccessInterface dataAccess;
    private final GenerateGroceryListOutputBoundary presenter;

    public GenerateGroceryListInteractor(GenerateGroceryListDataAccessInterface dataAccessInterface, GenerateGroceryListOutputBoundary presenter) {
        this.dataAccess = dataAccessInterface;
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
        MealPlan plan = dataAccess.getMealPlan();
        List<Recipe> recipes = plan.getAllRecipes(); // Wait for C.
        if (recipes.isEmpty()) {
            presenter.prepareFailView("Add meals to your plan first.");
            return;
        }

        Map<String, GroceryItem> map = new LinkedHashMap<>();

        for (Recipe recipe : recipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                String key = ingredient.getName().toLowerCase();
                GroceryItem groceryItem = map.get(key);
                if (groceryItem == null) {
                    groceryItem = new GroceryItem(ingredient.getName(), List.of());
                    map.put(key, groceryItem);
                }
                groceryItem.addMeasure(ingredient.getMeasure());
            }
        }

        List<GroceryItem> items = new ArrayList<>(map.values());
        dataAccess.saveGroceryList(items);
        Map<String, List<String>> itemsByName = new LinkedHashMap<>();

        for (GroceryItem item : items) {
            itemsByName.put(item.getIngredientName(), item.getMeasures());
        }

        GenerateGroceryListOutputData outputData = new GenerateGroceryListOutputData(itemsByName);

        presenter.prepareSuccessView(outputData);
    }
}
