package use_case.search_recipes;

import java.util.Collections;
import java.util.List;

/**
 * The recipes that matched a search, for the presenter to show. Never empty:
 * a search with no matches is reported through the fail view instead.
 * Immutable.
 */
public class SearchRecipesOutputData {
    private final List<RecipeSummary> results;

    /**
     * Creates the output data from a copy of the given results, so later
     * changes to the given list do not affect it.
     *
     * @param results the matching recipes, in the order the service returned them
     */
    public SearchRecipesOutputData(List<RecipeSummary> results) {
        this.results = List.copyOf(results);
    }

    /**
     * Returns a read-only list of the matching recipes, in service order.
     */
    public List<RecipeSummary> getResults() {
        return Collections.unmodifiableList(results);
    }
}