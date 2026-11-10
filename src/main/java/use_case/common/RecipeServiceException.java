package use_case.common;


public class RecipeServiceException extends Exception {
    public RecipeServiceException(String message) {
        super(message);
    }
    public RecipeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public RecipeServiceException(Throwable cause) {
        super(cause);
    }
}
