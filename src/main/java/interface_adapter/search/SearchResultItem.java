package interface_adapter.search;

/**
 * A single recipe in the search results, as the view needs to see it.
 * (Plain immutable class because the project targets Java 11, which has no records.)
 */
public class SearchResultItem {
    private final String apiId;
    private final String name;
    private final String category;
    private final String imageUrl;

    public SearchResultItem(String apiId, String name, String category, String imageUrl) {
        this.apiId = apiId;
        this.name = name;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public String getApiId() {
        return apiId;
    }

    public String getName() {
        return name;
    }

    /** May be null/empty: filter.php (search by ingredient) does not return a category. */
    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return name;
    }
}
