package use_case.add_meal_to_plan;

import entity.MealPlan;
import entity.MealPlanEntry;

/**
 * The weekly plan storage the Add Meal to Plan use case needs to read and write.
 */
public interface AddMealToPlanDataAccessInterface {

    /**
     * Returns the user's current weekly plan. Never returns null: if nothing
     * has been planned yet, the plan is empty.
     *
     * @return the weekly plan
     */
    MealPlan getMealPlan();

    /**
     * Stores an entry in its slot, replacing any entry already there.
     *
     * @param entry the entry to store
     * @throws MealPlanStorageException if the entry could not be stored
     */
    void saveEntry(MealPlanEntry entry) throws MealPlanStorageException;
}


