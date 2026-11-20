package interface_adapter.save_recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import use_case.save_recipe.SaveRecipeInputBoundary;
import use_case.save_recipe.SaveRecipeInputData;

class SaveRecipeControllerTest {

    @Test
    void executePassesInputDataToUseCaseOnce() {
        // Arrange: fake input boundary that records its calls, controller on that fake
        RecordingInputBoundary inputBoundary = new RecordingInputBoundary();
        SaveRecipeController controller = new SaveRecipeController(inputBoundary);

        // Act
        controller.execute("52772");

        // Assert: fake was called exactly once, with the same apiId
        assertEquals(1, inputBoundary.callCount);
        assertEquals("52772", inputBoundary.lastInputData.getApiId());
    }

    private static class RecordingInputBoundary implements SaveRecipeInputBoundary {
        int callCount = 0;
        SaveRecipeInputData lastInputData = null;

        @Override
        public void execute(SaveRecipeInputData inputData) {
            callCount++;
            lastInputData = inputData;
        }
    }
}
