package entity;

import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MealPlanEntryTest {

    private final Recipe recipe = new Recipe("52772", "Teriyaki Chicken Casserole", "Chicken",
            List.of(new Ingredient("soy sauce", "3/4 cup")), "Cook it.", "");

    @Test
    void gettersReturnConstructorValues() {
        MealPlanEntry entry = new MealPlanEntry(DayOfWeek.MONDAY, MealType.DINNER, recipe);

        assertEquals(DayOfWeek.MONDAY, entry.getDay());
        assertEquals(MealType.DINNER, entry.getMealType());
        assertSame(recipe, entry.getRecipe());
    }

    @Test
    void isInSlotMatchesOnlyItsOwnSlot() {
        MealPlanEntry entry = new MealPlanEntry(DayOfWeek.MONDAY, MealType.DINNER, recipe);

        assertTrue(entry.isInSlot(DayOfWeek.MONDAY, MealType.DINNER));
        assertFalse(entry.isInSlot(DayOfWeek.MONDAY, MealType.LUNCH));
        assertFalse(entry.isInSlot(DayOfWeek.TUESDAY, MealType.DINNER));
    }

    @Test
    void nullDayThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new MealPlanEntry(null, MealType.DINNER, recipe));
    }

    @Test
    void nullMealTypeThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new MealPlanEntry(DayOfWeek.MONDAY, null, recipe));
    }

    @Test
    void nullRecipeThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new MealPlanEntry(DayOfWeek.MONDAY, MealType.DINNER, null));
    }

}
