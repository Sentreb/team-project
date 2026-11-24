package interface_adapter.meal_plan;

import entity.MealType;
import org.junit.jupiter.api.Test;
import use_case.add_meal_to_plan.AddMealToPlanInputData;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddMealToPlanControllerTest {

    private AddMealToPlanInputData received;

    @Test
    void passesTheChoicesToTheInteractor() {
        AddMealToPlanController controller = new AddMealToPlanController(input -> received = input);

        controller.execute("52772", "WEDNESDAY", "LUNCH", true);

        assertEquals("52772", received.getRecipeApiId());
        assertEquals(DayOfWeek.WEDNESDAY, received.getDay());
        assertEquals(MealType.LUNCH, received.getMealType());
        assertTrue(received.isReplaceExisting());
    }
}

