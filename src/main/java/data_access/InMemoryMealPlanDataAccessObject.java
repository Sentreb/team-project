package data_access;

import entity.MealPlan;
import entity.MealPlanEntry;
import use_case.add_meal_to_plan.AddMealToPlanDataAccessInterface;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Keeps the weekly plan in memory. Used for tests and until the SQLite version
 * exists. Nothing is kept after the app closes.
 */
public class InMemoryMealPlanDataAccessObject implements AddMealToPlanDataAccessInterface {
    private final MealPlan plan;

    /**
     * Creates an empty plan that starts on the Monday of the current week.
     */
    public InMemoryMealPlanDataAccessObject() {
        this(LocalDate.now().with(DayOfWeek.MONDAY));
    }

    /**
     * Creates an empty plan that starts on the given date.
     *
     * @param weekStartDate the first day of the planned week
     */
    public InMemoryMealPlanDataAccessObject(LocalDate weekStartDate) {
        this.plan = new MealPlan(weekStartDate);
    }

    /**
     * Returns a copy of the plan, so callers cannot change the stored plan
     * without going through {@link #saveEntry(MealPlanEntry)}.
     */
    @Override
    public MealPlan getMealPlan() {
        MealPlan copy = new MealPlan(plan.getWeekStartDate());
        for (MealPlanEntry entry : plan.getEntries()) {
            copy.addEntry(entry);
        }
        return copy;
    }

    @Override
    public void saveEntry(MealPlanEntry entry) {
        plan.addEntry(entry);
    }
}


