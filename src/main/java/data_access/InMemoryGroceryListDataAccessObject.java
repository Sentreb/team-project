package data_access;

import entity.GroceryItem;
import entity.MealPlan;
import use_case.generate_grocery_list.GenerateGroceryListDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Grocery list storage kept in memory, for tests and for running the app
 * before the SQLite storage exists. Nothing is kept after the app closes.
 */
public class InMemoryGroceryListDataAccessObject implements GenerateGroceryListDataAccessInterface {
    private final MealPlan mealPlan;
    private List<GroceryItem> savedItems = null;

    /**
     * Creates storage that serves the given plan and has no saved list.
     *
     * @param mealPlan the plan {@link #getMealPlan()} returns
     */
    public InMemoryGroceryListDataAccessObject(MealPlan mealPlan) {
        this.mealPlan = mealPlan;
    }

    @Override
    public MealPlan getMealPlan() {
        return mealPlan;
    }

    @Override
    public void saveGroceryList(List<GroceryItem> items) {
        savedItems = new ArrayList<>(items);
    }

    /**
     * Returns the most recently saved list. Not part of the use case's
     * interface; tests use it to check what was saved.
     *
     * @return the saved items, or null if nothing has been saved
     */
    public List<GroceryItem> getSavedItems() {
        return savedItems;
    }
}
