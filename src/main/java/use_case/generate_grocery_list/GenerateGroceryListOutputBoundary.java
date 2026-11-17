package use_case.generate_grocery_list;

/**
 * Output boundary for the Generate Grocery List use case. The presenter
 * implements this to turn the result into something the view can show.
 */
public interface GenerateGroceryListOutputBoundary {

    /**
     * Shows the newly generated grocery list.
     *
     * @param outputData the list, with each ingredient once alongside all of its measures
     */
    void prepareSuccessView(GenerateGroceryListOutputData outputData);

    /**
     * Shows why no grocery list was generated.
     *
     * @param error a message for the user, such as asking them to add meals first
     */
    void prepareFailView(String error);

}
