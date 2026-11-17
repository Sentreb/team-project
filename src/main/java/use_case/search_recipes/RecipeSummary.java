package use_case.search_recipes;

public class RecipeSummary {
    private final String apiId;
    private final String name;
    private final String category;   // may be "" when it came from filter.php
    private final String imageUrl;

    public RecipeSummary(String apiId, String name, String category, String imageUrl) {
        this.apiId = apiId;
        this.name = name;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public String getApiId()    { return apiId; }
    public String getName()     { return name; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
}
