package use_case.generate_grocery_list;

import entity.GroceryItem;
import entity.MealPlan;

import java.util.List;

public interface GenerateGroceryListDataAccessInterface {

    MealPlan getMealPlan();

    void saveGroceryList(List<GroceryItem> items);

}
