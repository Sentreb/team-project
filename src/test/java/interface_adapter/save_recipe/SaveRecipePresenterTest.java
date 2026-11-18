package interface_adapter.save_recipe;

import org.junit.jupiter.api.Test;

class SaveRecipePresenterTest {

    @Test
    void successSetsSavedMessageAndNotifiesOnce() {
        // Arrange: view model with a counting listener, presenter on that view model

        // Act: prepareSuccessView with output data for "Teriyaki Chicken Casserole"

        // Assert: success message is Saved "Teriyaki Chicken Casserole" to your collection.
        //         error message is null, listener fired exactly once
    }

    @Test
    void failSetsErrorMessageAndNotifiesOnce() {
        // Arrange: view model with a counting listener, presenter on that view model

        // Act: prepareFailView with "This recipe is already in your collection."

        // Assert: error message matches, success message is null,
        //         listener fired exactly once
    }

    @Test
    void failAfterSuccessClearsSuccessMessage() {
        // Arrange: presenter that has already shown a success

        // Act: prepareFailView with any error

        // Assert: success message is now null, so the view never shows both
    }

    // TODO(Clifford): add a CountingListener that implements
    //  SaveRecipeViewModelListener and counts stateChanged() calls
}
