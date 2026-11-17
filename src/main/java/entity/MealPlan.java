package entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The user's weekly meal plan. Each day and meal slot holds at most one entry.
 */
public class MealPlan {
    private final LocalDate weekStartDate;
    private final List<MealPlanEntry> entries;
    /**
     * Creates an empty plan for the week starting on weekStartDate.
     *
     * @throws IllegalArgumentException if weekStartDate is null
     */
    public MealPlan(LocalDate weekStartDate) {
        if (weekStartDate == null) {
            throw new IllegalArgumentException("Week start date must not be null.");
        }
        this.weekStartDate = weekStartDate;
        this.entries = new ArrayList<>();
    }

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    /**
     * Returns a read-only view of every entry, in the order they were added.
     */
    public List<MealPlanEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Puts the entry in its slot, replacing any entry already there.
     *
     * @throws IllegalArgumentException if entry is null
     */
    public void addEntry(MealPlanEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Entry must not be null.");
        }
        removeEntry(entry.getDay(), entry.getMealType());
        entries.add(entry);
    }

    /**
     * Clears a slot.
     *
     * @return true if the slot held an entry
     */
    public boolean removeEntry(DayOfWeek day, MealType mealType) {
        return entries.removeIf(entry -> entry.isInSlot(day, mealType));
    }

    /**
     * Returns the entry in a slot, or null if the slot is empty.
     */
    public MealPlanEntry getEntry(DayOfWeek day, MealType mealType) {
        for (MealPlanEntry entry : entries) {
            if (entry.isInSlot(day, mealType)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Returns a read-only list of the entries planned for the given day.
     */
    public List<MealPlanEntry> getEntriesForDay(DayOfWeek day) {
        List<MealPlanEntry> result = new ArrayList<>();
        for (MealPlanEntry entry : entries) {
            if (entry.getDay() == day) {
                result.add(entry);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns the recipe of every entry. A recipe planned twice appears twice,
     * so the grocery list has enough for both meals.
     */
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        for (MealPlanEntry entry : entries) {
            recipes.add(entry.getRecipe());
        }
        return recipes;
    }
}

