package interface_adapter.save_recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import use_case.save_recipe.SaveRecipeOutputData;

class SaveRecipePresenterTest {

    @Test
    void successSetsSavedMessageAndNotifiesOnce() {
        // Arrange: view model with a counting listener, presenter on that view model
        SaveRecipeViewModel viewModel = new SaveRecipeViewModel();
        CountingListener listener = new CountingListener();
        viewModel.addListener(listener);
        SaveRecipePresenter presenter = new SaveRecipePresenter(viewModel);

        // Act: prepareSuccessView with output data for "Teriyaki Chicken Casserole"
        presenter.prepareSuccessView(new SaveRecipeOutputData("Teriyaki Chicken Casserole"));

        // Assert: success message set, error message null, listener fired exactly once
        assertEquals("Saved \"Teriyaki Chicken Casserole\" to your collection.",
                viewModel.getState().getSuccessMessage());
        assertNull(viewModel.getState().getErrorMessage());
        assertEquals(1, listener.counter);
    }

    @Test
    void failSetsErrorMessageAndNotifiesOnce() {
        // Arrange: view model with a counting listener, presenter on that view model
        SaveRecipeViewModel viewModel = new SaveRecipeViewModel();
        CountingListener listener = new CountingListener();
        viewModel.addListener(listener);
        SaveRecipePresenter presenter = new SaveRecipePresenter(viewModel);

        // Act: prepareFailView with the duplicate-recipe error
        presenter.prepareFailView("This recipe is already in your collection.");

        // Assert: error message matches, success message null, listener fired exactly once
        assertEquals("This recipe is already in your collection.",
                viewModel.getState().getErrorMessage());
        assertNull(viewModel.getState().getSuccessMessage());
        assertEquals(1, listener.counter);
    }

    @Test
    void failAfterSuccessClearsSuccessMessage() {
        // Arrange: presenter that has already shown a success
        SaveRecipeViewModel viewModel = new SaveRecipeViewModel();
        CountingListener listener = new CountingListener();
        viewModel.addListener(listener);
        SaveRecipePresenter presenter = new SaveRecipePresenter(viewModel);
        presenter.prepareSuccessView(new SaveRecipeOutputData("Chicken Teriyaki"));

        // Act: prepareFailView with any error
        presenter.prepareFailView("Not found!");

        // Assert: success message is now null, so the view never shows both
        assertNull(viewModel.getState().getSuccessMessage());
        assertEquals("Not found!", viewModel.getState().getErrorMessage());
    }

    private static class CountingListener implements SaveRecipeViewModelListener {
        int counter = 0;

        @Override
        public void stateChanged() {
            counter++;
        }
    }
}
