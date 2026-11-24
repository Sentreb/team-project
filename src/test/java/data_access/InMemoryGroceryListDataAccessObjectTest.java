package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.MealPlanEntry;
import entity.MealType;
import entity.Recipe;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class InMemoryGroceryListDataAccessObjectTest {

  @Test
  void mealAddedAfterCreationIsInThePlan() throws Exception {
    // Arrange: grocery storage made while the meal plan storage is still empty,
    //          the way AppBuilder makes it at startup
    InMemoryMealPlanDataAccessObject mealPlanDataAccess =
        new InMemoryMealPlanDataAccessObject(LocalDate.of(2026, 11, 23));
    InMemoryGroceryListDataAccessObject groceryDataAccess =
        new InMemoryGroceryListDataAccessObject(mealPlanDataAccess::getMealPlan);

    // Act: plan a meal afterwards
    Recipe recipe = new Recipe("52772", "Teriyaki Chicken Casserole", "", List.of(), "", "");
    mealPlanDataAccess.saveEntry(new MealPlanEntry(DayOfWeek.MONDAY, MealType.DINNER, recipe));

    // Assert: the grocery storage sees the meal, not the empty plan from startup
    assertEquals(1, groceryDataAccess.getMealPlan().getAllRecipes().size());
    assertEquals("52772", groceryDataAccess.getMealPlan().getAllRecipes().get(0).getApiId());
  }

  @Test
  void emptyMealPlanStorageGivesEmptyPlan() {
    // Arrange: grocery storage reading an empty meal plan storage
    InMemoryMealPlanDataAccessObject mealPlanDataAccess = new InMemoryMealPlanDataAccessObject();
    InMemoryGroceryListDataAccessObject groceryDataAccess =
        new InMemoryGroceryListDataAccessObject(mealPlanDataAccess::getMealPlan);

    // Act and Assert: the plan has no recipes
    assertTrue(groceryDataAccess.getMealPlan().getAllRecipes().isEmpty());
  }
}
