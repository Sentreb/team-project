package interface_adapter.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Everything the search view needs to render itself.
 */
public class SearchState {
    private List<SearchResultItem> results = new ArrayList<>();
    private String message = "";

    public List<SearchResultItem> getResults() {
        return results;
    }

    public void setResults(List<SearchResultItem> results) {
        this.results = results;
    }

    /** A status or error message to show under the list (empty string = nothing to show). */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}