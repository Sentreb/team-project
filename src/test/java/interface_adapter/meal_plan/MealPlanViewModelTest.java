package interface_adapter.meal_plan;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MealPlanViewModelTest {

    private int notifications;

    @Test
    void daysAndSlotsAreInPlanOrder() {
        MealPlanViewModel viewModel = new MealPlanViewModel();

        assertEquals(List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY",
                "SATURDAY", "SUNDAY"), viewModel.getDays());
        assertEquals(List.of("BREAKFAST", "LUNCH", "DINNER"), viewModel.getMealSlots());
    }

    @Test
    void everyListenerIsNotified() {
        MealPlanViewModel viewModel = new MealPlanViewModel();
        viewModel.addListener(() -> notifications++);
        viewModel.addListener(() -> notifications++);

        viewModel.notifyStateChanged();

        assertEquals(2, notifications);
    }

    @Test
    void emptySlotHasEmptyName() {
        MealPlanState state = new MealPlanState();
        state.setRecipeName("MONDAY", "DINNER", "Pancakes");

        assertEquals("Pancakes", state.getRecipeName("MONDAY", "DINNER"));
        assertEquals("", state.getRecipeName("MONDAY", "LUNCH"));
    }
}
