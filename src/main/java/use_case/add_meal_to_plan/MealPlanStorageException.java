package use_case.add_meal_to_plan;

/**
 * Thrown when the meal plan cannot be stored, for example if the database is unavailable.
 */
public class MealPlanStorageException extends Exception {

    /**
     * Creates the exception with a message.
     *
     * @param message what went wrong
     */
    public MealPlanStorageException(String message) {
        super(message);
    }

    /**
     * Creates the exception with a message and the underlying cause.
     *
     * @param message what went wrong
     * @param cause   the original exception
     */
    public MealPlanStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

