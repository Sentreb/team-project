package use_case.save_recipe;

/**
 * The output data for the Save Recipe use case: what the presenter needs to
 * tell the user which recipe was saved. Holds plain values, never a Recipe.
 */
public class SaveRecipeOutputData {

    private final String recipeName;

    /**
     * Creates the output data for a saved recipe.
     *
     * @param recipeName the name of the recipe that was saved
     */
    public SaveRecipeOutputData(String recipeName) {
        this.recipeName = recipeName;
    }

    /**
     * Returns the name of the recipe that was saved.
     *
     * @return the recipe's name
     */
    public String getRecipeName() {
        return recipeName;
    }
}
