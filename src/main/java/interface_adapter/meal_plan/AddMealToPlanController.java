package interface_adapter.meal_plan;

import entity.MealType;
import use_case.add_meal_to_plan.AddMealToPlanInputBoundary;
import use_case.add_meal_to_plan.AddMealToPlanInputData;

import java.time.DayOfWeek;

/**
 * Turns the meal plan screen's choices into an Add Meal to Plan request.
 */
public class AddMealToPlanController {
    private final AddMealToPlanInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the Add Meal to Plan use case
     */
    public AddMealToPlanController(AddMealToPlanInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Asks to put a saved recipe in a slot of the weekly plan.
     *
     * @param recipeApiId     the TheMealDB id of the chosen saved recipe
     * @param day             a day name from the view model, such as "MONDAY"
     * @param mealSlot        a meal slot name from the view model, such as "DINNER"
     * @param replaceExisting true if the user has confirmed replacing the meal in that slot
     */
    public void execute(String recipeApiId, String day, String mealSlot, boolean replaceExisting) {
        interactor.execute(new AddMealToPlanInputData(recipeApiId, DayOfWeek.valueOf(day),
                MealType.valueOf(mealSlot), replaceExisting));
    }
}

