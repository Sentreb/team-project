package interface_adapter.save_recipe;

/**
 * Something that wants to know when the Save Recipe state changes, such as
 * the view that shows the save result.
 */
public interface SaveRecipeViewModelListener {

    /**
     * The Save Recipe state was updated.
     */
    void stateChanged();
}
