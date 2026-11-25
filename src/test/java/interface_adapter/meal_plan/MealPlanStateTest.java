package interface_adapter.meal_plan;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MealPlanStateTest {

    @Test
    void replaceQuestionIsAskedOnlyOnce() {
        MealPlanState state = new MealPlanState();
        state.setRecipeToReplace("Pancakes");

        assertEquals("Pancakes", state.takeRecipeToReplace());
        assertNull(state.takeRecipeToReplace());
        assertNull(state.getRecipeToReplace());
    }

    @Test
    void errorIsShownOnlyOnce() {
        MealPlanState state = new MealPlanState();
        state.setErrorMessage("The meal was not added. Please try again.");

        assertEquals("The meal was not added. Please try again.", state.takeErrorMessage());
        assertNull(state.takeErrorMessage());
    }

    @Test
    void takingMessagesKeepsThePlanAndOptions() {
        MealPlanState state = new MealPlanState();
        state.setRecipeName("MONDAY", "DINNER", "Pancakes");
        state.setStatusMessage("Added Pancakes to your plan.");
        state.setRecipeToReplace("Pancakes");

        state.takeRecipeToReplace();

        assertEquals("Pancakes", state.getRecipeName("MONDAY", "DINNER"));
        assertEquals("Added Pancakes to your plan.", state.getStatusMessage());
    }
}

