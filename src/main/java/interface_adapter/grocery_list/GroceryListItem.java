package interface_adapter.grocery_list;

/**
 * One line of the grocery list as the view needs to see it: the ingredient
 * name and its measures as one string, such as "1, 2 slices". Immutable.
 */
public class GroceryListItem {

  private final String ingredientName;
  private final String measures;

  /**
   * Creates one line of the grocery list.
   *
   * @param ingredientName the ingredient name
   * @param measures       every measure for the ingredient, joined into one
   *                       string; empty when there are none
   */
  public GroceryListItem(String ingredientName, String measures) {
    this.ingredientName = ingredientName;
    this.measures = measures;
  }

  /**
   * Returns the ingredient name.
   *
   * @return the ingredient name
   */
  public String getIngredientName() {
    return ingredientName;
  }

  /**
   * Returns every measure for the ingredient as one string.
   *
   * @return the measures, such as "1, 2 slices", or an empty string
   */
  public String getMeasures() {
    return measures;
  }
}
