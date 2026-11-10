package use_case.generate_grocery_list;

public interface GenerateGroceryListOutputBoundary {

    void prepareSuccessView(GenerateGroceryListOutputData outputData);

    void prepareFailView(String error);

}
