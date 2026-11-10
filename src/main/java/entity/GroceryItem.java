package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GroceryItem {

    private final String ingredientName;
    private final List<String> measures;
    private boolean checked;

    public GroceryItem(String ingredientName, List<String> measures) {
        this(ingredientName, measures, false);
    }

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

    public void addMeasure(String measure) {
        if (measure == null || measure.isBlank()) {
            return;
        }
        this.measures.add(measure);
    }

    public void toggleChecked() {
        this.checked = !this.checked;
    }

    public String getIngredientName() {
        return this.ingredientName;
    }

    public List<String> getMeasures() {
        // To prevent editing the internal list, we return an unmodifiable list version instead.
        return Collections.unmodifiableList(this.measures);
    }

    public boolean isChecked() {
        return this.checked;
    }

}
