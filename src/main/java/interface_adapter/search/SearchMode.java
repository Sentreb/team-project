package interface_adapter.search;

/**
 * The two ways a user can search TheMealDB: by recipe name or by a single ingredient.
 * The label is what the dropdown in the search view displays.
 */
public enum SearchMode {
    BY_NAME("Name"),
    BY_INGREDIENT("Ingredient");

    private final String label;

    SearchMode(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
