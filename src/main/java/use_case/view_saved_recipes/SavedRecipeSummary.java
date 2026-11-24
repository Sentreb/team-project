package use_case.view_saved_recipes;

/**
 * One saved recipe as plain values, so no Recipe entity leaves the use case.
 * Immutable.
 */
public class SavedRecipeSummary {

  private final String apiId;
  private final String name;

  /**
   * Creates a summary of one saved recipe.
   *
   * @param apiId the TheMealDB id of the recipe
   * @param name  the name of the recipe
   */
  public SavedRecipeSummary(String apiId, String name) {
    this.apiId = apiId;
    this.name = name;
  }

  /**
   * Returns the TheMealDB id of the recipe.
   *
   * @return the recipe's apiId
   */
  public String getApiId() {
    return apiId;
  }

  /**
   * Returns the name of the recipe.
   *
   * @return the recipe's name
   */
  public String getName() {
    return name;
  }
}
