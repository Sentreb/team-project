package use_case.generate_grocery_list;

import entity.GroceryItem;
import entity.MealPlan;

import java.util.List;

/**
 * The storage the Generate Grocery List use case needs: it reads the weekly
 * plan and saves the grocery list made from it.
 */
public interface GenerateGroceryListDataAccessInterface {

    /**
     * Returns the user's weekly plan.
     *
     * @return the plan; an empty plan if the user has not planned any meals,
     *         never null
     */
    MealPlan getMealPlan();

    /**
     * Saves the grocery list, replacing any previously saved list.
     *
     * @param items the items of the new list, in display order
     */
    void saveGroceryList(List<GroceryItem> items);

}
