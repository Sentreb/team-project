package interface_adapter.save_recipe;

import org.junit.jupiter.api.Test;

class SaveRecipeControllerTest {

    @Test
    void executePassesInputDataToUseCaseOnce() {
        // Arrange: fake input boundary that records its calls, controller on that fake

        // Act: controller.execute("52772")

        // Assert: fake was called exactly once, with non-null input data
    }

    // TODO(Clifford): add a RecordingInputBoundary that implements
    //  SaveRecipeInputBoundary and keeps a call count and the last input data
}
