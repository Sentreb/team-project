package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One line of the grocery list: an ingredient with every measure needed for it
 * across the planned meals, such as "Onion" with measures ["1", "2 slices"].
 * Blank measures are never stored, because TheMealDB leaves some measures empty.
 */
public final class GroceryItem {

    private final String ingredientName;
    private final List<String> measures;
    private boolean checked;

    /**
     * Creates an unchecked item.
     *
     * @param ingredientName the ingredient name; must not be null or blank
     * @param measures       the measures to start with; blank or null measures are skipped
     * @throws IllegalArgumentException if ingredientName is null or blank, or measures is null
     */
    public GroceryItem(String ingredientName, List<String> measures) {
        this(ingredientName, measures, false);
    }

    /**
     * Creates an item with its checked state set, for example when loading a
     * saved list from the database.
     *
     * @param ingredientName the ingredient name; must not be null or blank
     * @param measures       the measures to start with; blank or null measures are skipped
     * @param checked        whether the item has been checked off
     * @throws IllegalArgumentException if ingredientName is null or blank, or measures is null
     */
    public GroceryItem(String ingredientName, List<String> measures, boolean checked) {
        if (ingredientName == null || ingredientName.isBlank()) {
            throw new IllegalArgumentException("Ingredient name can't be empty");
        }
        if (measures == null) {
            throw new IllegalArgumentException("Measures list can't be null");
        }

        this.ingredientName = ingredientName;
        this.measures = new ArrayList<>();
        for (String measure: measures) {
            addMeasure(measure);
        }
        this.checked = checked;
    }

    /**
     * Adds a measure to the end of this item's measures. Does nothing if the
     * measure is null or blank.
     *
     * @param measure the amount to add, such as "2 tbsp"
     */
    public void addMeasure(String measure) {
        if (measure == null || measure.isBlank()) {
            return;
        }
        this.measures.add(measure);
    }

    /**
     * Marks the item as bought if it was not, or as not bought if it was.
     */
    public void toggleChecked() {
        this.checked = !this.checked;
    }

    public String getIngredientName() {
        return this.ingredientName;
    }

    /**
     * Returns a read-only view of the measures, in the order they were added.
     * Use {@link #addMeasure(String)} to add one.
     */
    public List<String> getMeasures() {
        return Collections.unmodifiableList(this.measures);
    }

    public boolean isChecked() {
        return this.checked;
    }

}
