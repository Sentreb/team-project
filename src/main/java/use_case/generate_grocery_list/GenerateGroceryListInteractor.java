package use_case.generate_grocery_list;

import entity.GroceryItem;
import entity.Ingredient;
import entity.MealPlan;
import entity.Recipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates the grocery list for the weekly plan (Use Case 4).
 *
 * <p>Ingredients with the same name are combined into one item, ignoring case,
 * and the item keeps every measure. The item is named with the spelling first
 * seen in the plan. A recipe planned twice contributes its measures twice.</p>
 */
public class GenerateGroceryListInteractor implements GenerateGroceryListInputBoundary {
    private final GenerateGroceryListDataAccessInterface dataAccess;
    private final GenerateGroceryListOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param dataAccessInterface where the plan is read from and the list is saved to
     * @param presenter           where the result is sent
     */
    public GenerateGroceryListInteractor(GenerateGroceryListDataAccessInterface dataAccessInterface, GenerateGroceryListOutputBoundary presenter) {
        this.dataAccess = dataAccessInterface;
        this.presenter = presenter;
    }

    /**
     * Builds the grocery list from every recipe in the plan, saves it in place
     * of any previous list, and presents it. If the plan has no meals, presents
     * an error and saves nothing.
     */
    @Override
    public void execute() {
        MealPlan plan = dataAccess.getMealPlan();
        List<Recipe> recipes = plan.getAllRecipes();
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
