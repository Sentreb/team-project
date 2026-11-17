package entity;

import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MealPlanTest {

    private static final LocalDate WEEK_START = LocalDate.of(2026, 11, 16);

    private final Recipe teriyaki = new Recipe("52772", "Teriyaki Chicken Casserole", "Chicken",
            List.of(new Ingredient("soy sauce", "3/4 cup")), "Cook it.", "");
    private final Recipe pancakes = new Recipe("52854", "Pancakes", "Dessert",
            List.of(new Ingredient("flour", "100g")), "Fry them.", "");

    @Test
    void newPlanIsEmpty() {
        MealPlan plan = new MealPlan(WEEK_START);

        assertTrue(plan.getEntries().isEmpty());
        assertTrue(plan.getAllRecipes().isEmpty());
    }

    @Test
    void addEntryFillsSlot() {
        MealPlan plan = new MealPlan(WEEK_START);

        plan.addEntry(new MealPlanEntry(DayOfWeek.MONDAY, MealType.DINNER, teriyaki));

        MealPlanEntry entry = plan.getEntry(DayOfWeek.MONDAY, MealType.DINNER);

        assertSame(teriyaki, entry.getRecipe());
    }

    @Test
    void emptySlotReturnsNull() {
        MealPlan plan = new MealPlan(WEEK_START);

        assertNull(plan.getEntry(DayOfWeek.MONDAY, MealType.DINNER));
    }

    @Test
    void addEntryReplacesMealInSameSlot() {
        MealPlan plan = new MealPlan(WEEK_START);
        plan.addEntry(new MealPlanEntry(DayOfWeek.MONDAY, MealType.DINNER, teriyaki));

        plan.addEntry(new MealPlanEntry(DayOfWeek.MONDAY, MealType.DINNER, pancakes));

        assertEquals(1, plan.getEntries().size());
        assertSame(pancakes, plan.getEntry(DayOfWeek.MONDAY, MealType.DINNER).getRecipe());
    }

    @Test
    void removeEntryClearsSlot() {
        MealPlan plan = new MealPlan(WEEK_START);
        plan.addEntry(new MealPlanEntry(DayOfWeek.MONDAY, MealType.DINNER, teriyaki));

        boolean removed = plan.removeEntry(DayOfWeek.MONDAY, MealType.DINNER);

        assertTrue(removed);
        assertNull(plan.getEntry(DayOfWeek.MONDAY, MealType.DINNER));
    }

    @Test
    void removeEntryOnEmptySlotReturnsFalse() {
        MealPlan plan = new MealPlan(WEEK_START);

        boolean removed = plan.removeEntry(DayOfWeek.MONDAY, MealType.DINNER);

        assertFalse(removed);
    }

    @Test
    void getEntriesForDayReturnsOnlyThatDay() {
        MealPlan plan = new MealPlan(WEEK_START);
        plan.addEntry(new MealPlanEntry(DayOfWeek.MONDAY, MealType.BREAKFAST, pancakes));
        plan.addEntry(new MealPlanEntry(DayOfWeek.MONDAY, MealType.DINNER, teriyaki));
        plan.addEntry(new MealPlanEntry(DayOfWeek.TUESDAY, MealType.DINNER, teriyaki));

        List<MealPlanEntry> monday = plan.getEntriesForDay(DayOfWeek.MONDAY);

        assertEquals(2, monday.size());
    }

    @Test
    void getAllRecipesKeepsRepeatedRecipes() {
        MealPlan plan = new MealPlan(WEEK_START);
        plan.addEntry(new MealPlanEntry(DayOfWeek.MONDAY, MealType.DINNER, teriyaki));
        plan.addEntry(new MealPlanEntry(DayOfWeek.TUESDAY, MealType.DINNER, teriyaki));

        List<Recipe> recipes = plan.getAllRecipes();

        assertEquals(List.of(teriyaki, teriyaki), recipes);
    }

    @Test
    void getEntriesCannotBeModified() {
        MealPlan plan = new MealPlan(WEEK_START);

        assertThrows(UnsupportedOperationException.class, () -> plan.getEntries()
                .add(new MealPlanEntry(DayOfWeek.MONDAY, MealType.DINNER, teriyaki)));
    }

    @Test
    void nullEntryThrows() {
        MealPlan plan = new MealPlan(WEEK_START);

        assertThrows(IllegalArgumentException.class, () -> plan.addEntry(null));
    }

    @Test
    void nullWeekStartDateThrows() {
        assertThrows(IllegalArgumentException.class, () -> new MealPlan(null));
    }

}
