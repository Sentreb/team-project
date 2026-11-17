package use_case.add_meal_to_plan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The result of a successful add: the recipe that was added and the updated weekly plan.
 */
public class AddMealToPlanOutputData {
    private final String addedRecipeName;
    private final List<PlannedMeal> plannedMeals;

    /**
     * Creates the output data.
     *
     * @param addedRecipeName the name of the recipe that was just added
     * @param plannedMeals    every filled slot of the updated plan
     */
    public AddMealToPlanOutputData(String addedRecipeName, List<PlannedMeal> plannedMeals) {
        this.addedRecipeName = addedRecipeName;
        this.plannedMeals = new ArrayList<>(plannedMeals);
    }

    public String getAddedRecipeName() {
        return addedRecipeName;
    }

    /**
     * Returns a read-only list of every filled slot in the updated plan.
     */
    public List<PlannedMeal> getPlannedMeals() {
        return Collections.unmodifiableList(plannedMeals);
    }
}
