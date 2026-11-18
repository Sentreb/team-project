package use_case.common;

/**
 * Thrown when the external recipe service (TheMealDB) cannot give a use case
 * what it asked for, for example when the service cannot be reached, returns
 * an HTTP error or an unexpected response, or has no recipe with the requested
 * id. Shared by the Search Recipes and Save Recipe use cases, so it lives in
 * {@code use_case.common}.
 *
 * <p>Checked, so interactors must handle it and tell the user through their
 * presenter. The message is meant for developers and logs; interactors should
 * not show it to the user as is.</p>
 */
public class RecipeServiceException extends Exception {

    /**
     * Creates the exception with a message.
     *
     * @param message what went wrong
     */
    public RecipeServiceException(String message) {
        super(message);
    }

    /**
     * Creates the exception with a message and the underlying cause.
     *
     * @param message what went wrong
     * @param cause   the original exception, such as an {@code IOException}
     */
    public RecipeServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates the exception from the underlying cause alone. The message is
     * the cause's string form.
     *
     * @param cause the original exception
     */
    public RecipeServiceException(Throwable cause) {
        super(cause);
    }
}
