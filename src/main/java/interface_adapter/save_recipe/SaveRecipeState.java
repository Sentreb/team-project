package interface_adapter.save_recipe;

/**
 * The state for the Save Recipe View Model: the message the user should see
 * after trying to save a recipe. At most one of the two messages is set.
 */
public class SaveRecipeState {

    private String successMessage = null;
    private String errorMessage = null;

    /**
     * Returns the message shown after a recipe was saved.
     *
     * @return the success message, or null if the last save failed
     */
    public String getSuccessMessage() {
        return successMessage;
    }

    /**
     * Sets the message shown after a recipe was saved.
     *
     * @param successMessage the success message, or null to clear it
     */
    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    /**
     * Returns the message shown after a recipe could not be saved.
     *
     * @return the error message, or null if the last save worked
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the message shown after a recipe could not be saved.
     *
     * @param errorMessage the error message, or null to clear it
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
