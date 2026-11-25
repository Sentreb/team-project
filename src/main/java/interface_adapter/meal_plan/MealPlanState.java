package interface_adapter.meal_plan;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Everything the meal plan screen shows, as plain strings, so the view never
 * depends on entities. Days and meal slots are identified by their enum names,
 * such as "MONDAY" and "DINNER".
 */
public class MealPlanState {
    private final Map<String, String> recipeOptions = new LinkedHashMap<>();
    private final Map<String, String> recipeNamesBySlot = new LinkedHashMap<>();
    private String statusMessage = "";
    private String errorMessage;
    private String recipeToReplace;

    /**
     * Returns the recipes the user can choose from: TheMealDB id mapped to recipe name.
     *
     * @return a read-only map, in display order
     */
    public Map<String, String> getRecipeOptions() {
        return Collections.unmodifiableMap(recipeOptions);
    }

    /**
     * Replaces the recipes the user can choose from.
     *
     * @param options TheMealDB id mapped to recipe name, in display order
     */
    public void setRecipeOptions(Map<String, String> options) {
        recipeOptions.clear();
        recipeOptions.putAll(options);
    }

    /**
     * Returns the name of the recipe planned in a slot.
     *
     * @param day      a day name, such as "MONDAY"
     * @param mealSlot a meal slot name, such as "DINNER"
     * @return the recipe name, or an empty string if the slot is empty
     */
    public String getRecipeName(String day, String mealSlot) {
        return recipeNamesBySlot.getOrDefault(slotKey(day, mealSlot), "");
    }

    /**
     * Removes every planned meal from the state.
     */
    public void clearPlannedMeals() {
        recipeNamesBySlot.clear();
    }

    /**
     * Records the recipe planned in a slot.
     *
     * @param day        a day name, such as "MONDAY"
     * @param mealSlot   a meal slot name, such as "DINNER"
     * @param recipeName the name of the planned recipe
     */
    public void setRecipeName(String day, String mealSlot, String recipeName) {
        recipeNamesBySlot.put(slotKey(day, mealSlot), recipeName);
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * Returns the error to show the user, or null if the last action succeeded.
     *
     * @return the error message, or null
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Returns the error to show the user and clears it, so the view shows each
     * error only once even if the state is refreshed again later.
     *
     * @return the error message, or null if there is none to show
     */
    public String takeErrorMessage() {
        String message = errorMessage;
        errorMessage = null;
        return message;
    }

    /**
     * Returns the name of the recipe the user is being asked to replace, or
     * null if no confirmation is needed.
     *
     * @return the recipe currently in the chosen slot, or null
     */
    public String getRecipeToReplace() {
        return recipeToReplace;
    }

    public void setRecipeToReplace(String recipeToReplace) {
        this.recipeToReplace = recipeToReplace;
    }

    /**
     * Returns the recipe the user should be asked about replacing and clears
     * it, so the question is asked only once whether the user answers Yes or No.
     *
     * @return the recipe currently in the chosen slot, or null if there is no question
     */
    public String takeRecipeToReplace() {
        String recipe = recipeToReplace;
        recipeToReplace = null;
        return recipe;
    }

    private static String slotKey(String day, String mealSlot) {
        return day + "|" + mealSlot;
    }
}
