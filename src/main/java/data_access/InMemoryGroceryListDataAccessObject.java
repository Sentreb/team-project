package data_access;

import entity.GroceryItem;
import entity.MealPlan;
import use_case.generate_grocery_list.GenerateGroceryListDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Grocery list storage kept in memory, for tests and for running the app
 * before the SQLite storage exists. Nothing is kept after the app closes.
 *
 * <p>The plan is not stored here. Each {@link #getMealPlan()} asks the given
 * source again, so meals added after this object was made are included.</p>
 */
public class InMemoryGroceryListDataAccessObject implements GenerateGroceryListDataAccessInterface {
    private final Supplier<MealPlan> mealPlanSource;
    private List<GroceryItem> savedItems = null;

    /**
     * Creates storage that reads the plan from the given source and has no
     * saved list. In the app, pass the meal plan storage's
     * {@code getMealPlan} method, such as {@code mealPlanDataAccess::getMealPlan}.
     *
     * @param mealPlanSource gives the current plan each time it is called
     */
    public InMemoryGroceryListDataAccessObject(Supplier<MealPlan> mealPlanSource) {
        this.mealPlanSource = mealPlanSource;
    }

    /**
     * Returns the current plan from the source given to the constructor.
     *
     * @return the current plan
     */
    @Override
    public MealPlan getMealPlan() {
        return mealPlanSource.get();
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
