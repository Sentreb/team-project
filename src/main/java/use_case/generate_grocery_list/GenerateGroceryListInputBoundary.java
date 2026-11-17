package use_case.generate_grocery_list;

/**
 * Input boundary for the Generate Grocery List use case. The controller calls
 * this when the user asks for a grocery list.
 */
public interface GenerateGroceryListInputBoundary {

    /**
     * Builds a grocery list from the current weekly plan, saves it in place of
     * any previous list, and presents it. If the plan has no meals, presents an
     * error instead and saves nothing.
     */
    void execute();

}
