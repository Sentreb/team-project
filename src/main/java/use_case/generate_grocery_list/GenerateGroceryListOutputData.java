package use_case.generate_grocery_list;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The generated grocery list as plain data for the presenter: each ingredient
 * name mapped to its measures, in the order the ingredients first appear in the
 * plan. Holds no entities, so the presenter cannot change the grocery items.
 * Immutable.
 */
public class GenerateGroceryListOutputData {

    private final Map<String, List<String>> items;

    /**
     * Creates the output data from a copy of the given items, so later changes
     * to the given map or its lists do not affect it.
     *
     * @param items each ingredient name mapped to its measures; the lists must
     *              not contain null
     */
    public GenerateGroceryListOutputData(Map<String, List<String>> items) {
        this.items = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry: items.entrySet()) {
            this.items.put(entry.getKey(), List.copyOf(entry.getValue()));
        }
    }

    /**
     * Returns a read-only view of the items, in the order the ingredients first
     * appear in the plan. The measure lists are read-only too.
     */
    public Map<String, List<String>> getItems() {
        return Collections.unmodifiableMap(items);
    }

}
