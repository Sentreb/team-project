package interface_adapter.view_saved_recipes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import use_case.view_saved_recipes.ViewSavedRecipesInputBoundary;

class ViewSavedRecipesControllerTest {

  @Test
  void executeCallsUseCaseOnce() {
    // Arrange
    RecordingInputBoundary inputBoundary = new RecordingInputBoundary();
    ViewSavedRecipesController controller = new ViewSavedRecipesController(inputBoundary);

    // Act
    controller.execute();

    // Assert
    assertEquals(1, inputBoundary.callCount);
  }

  private static class RecordingInputBoundary implements ViewSavedRecipesInputBoundary {
    int callCount = 0;

    @Override
    public void execute() {
      callCount++;
    }
  }
}
