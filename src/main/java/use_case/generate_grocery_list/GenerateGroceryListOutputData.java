package use_case.generate_grocery_list;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenerateGroceryListOutputData {

    private final Map<String, List<String>> items;

    public GenerateGroceryListOutputData(Map<String, List<String>> items) {
        this.items = new LinkedHashMap<>();
        // Copy the list using List.copyOf() to ensure it is unmodifiable
        for (Map.Entry<String, List<String>> entry: items.entrySet()) {
            this.items.put(entry.getKey(), List.copyOf(entry.getValue()));
        }
    }

    public Map<String, List<String>> getItems() {
        return Collections.unmodifiableMap(items);
    }

}
