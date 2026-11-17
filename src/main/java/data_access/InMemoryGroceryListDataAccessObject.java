package data_access;

import entity.GroceryItem;
import entity.MealPlan;
import use_case.generate_grocery_list.GenerateGroceryListDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class InMemoryGroceryListDataAccessObject implements GenerateGroceryListDataAccessInterface {
    private final MealPlan mealPlan;
    private List<GroceryItem> savedItems = null;

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

    public List<GroceryItem> getSavedItems() {
        return savedItems;
    }
}
