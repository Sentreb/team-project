package interface_adapter.grocery_list;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import use_case.generate_grocery_list.GenerateGroceryListInputBoundary;

class GenerateGroceryListControllerTest {

  @Test
  void executeCallsUseCaseOnce() {
    // Arrange: fake input boundary that counts its calls, controller on that fake
    CountingInputBoundary inputBoundary = new CountingInputBoundary();
    GenerateGroceryListController controller = new GenerateGroceryListController(inputBoundary);

    // Act
    controller.execute();

    // Assert: fake was called exactly once
    assertEquals(1, inputBoundary.callCount);
  }

  private static class CountingInputBoundary implements GenerateGroceryListInputBoundary {
    int callCount = 0;

    @Override
    public void execute() {
      callCount++;
    }
  }
}
