package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A recipe in the user's collection. Starts as a copy of a TheMealDB recipe,
 * then the user can edit its notes, ingredients and servings.
 *
 * <p>Identity is {@code apiId}: a recipe can be saved only once, so the
 * TheMealDB id is unique within the collection.</p>
 */
public class Recipe {
    /** TheMealDB gives no serving count, so new recipes start with this. */
    public static final int DEFAULT_SERVINGS = 1;

    private final String apiId;
    private final String name;
    private final String category;
    private final List<Ingredient> ingredients;
    private final String instructions;
    private final String imageUrl;
    private int servings;
    private String userNotes;

    /**
     * Creates a recipe fresh from the API, with default servings and no notes.
     */
    public Recipe(String apiId, String name, String category, List<Ingredient> ingredients,
                  String instructions, String imageUrl) {
        this(apiId, name, category, ingredients, instructions, imageUrl, DEFAULT_SERVINGS, "");
    }

    /**
     * Creates a recipe with every field set, for example when loading a saved,
     * edited recipe from the database.
     **/
    public Recipe(String apiId, String name, String category, List<Ingredient> ingredients,
                  String instructions, String imageUrl, int servings, String userNotes) {
        if (apiId == null || apiId.isBlank()) {
            throw new IllegalArgumentException("Recipe apiId must not be blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Recipe name must not be blank.");
        }
        if (ingredients == null) {
            throw new IllegalArgumentException("Ingredients must not be null.");
        }
        for (Ingredient ingredient : ingredients) {
            if (ingredient == null) {
                throw new IllegalArgumentException("Ingredients must not contain null.");
            }
        }

        checkServings(servings);
        this.apiId = apiId;
        this.name = name;
        this.category = category == null ? "" : category;
        this.ingredients = new ArrayList<>(ingredients);
        this.instructions = instructions == null ? "" : instructions;
        this.imageUrl = imageUrl == null ? "" : imageUrl;
        this.servings = servings;
        this.userNotes = userNotes == null ? "" : userNotes;
    }

    public String getApiId() {
        return apiId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    /**
     * Returns a read-only view of the ingredients. Use
     * {@link #replaceIngredient(Ingredient, Ingredient)} to change one.
     */
    public List<Ingredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    public String getInstructions() {
        return instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getServings() {
        return servings;
    }

    public String getUserNotes() {
        return userNotes;
    }

    /**
     * Replaces the user's notes. Null clears them.
     */
    public void updateNotes(String notes) {
        this.userNotes = notes == null ? "" : notes;
    }

    /**
     * Swaps one ingredient for another, keeping its position in the list.
     *
     * @throws IllegalArgumentException if replacement is null or oldIngredient
     *                                  is not in this recipe
     */
    public void replaceIngredient(Ingredient oldIngredient, Ingredient replacement) {
        if (replacement == null) {
            throw new IllegalArgumentException("Replacement ingredient must not be null.");
        }
        int index = ingredients.indexOf(oldIngredient);
        if (index == -1) {
            throw new IllegalArgumentException(
                    "Ingredient not in recipe: " + oldIngredient);
        }
        ingredients.set(index, replacement);
    }

    /**
     * @throws IllegalArgumentException if servings is below 1
     */
    public void setServings(int servings) {
        checkServings(servings);
        this.servings = servings;
    }

    private static void checkServings(int servings) {
        if (servings < 1) {
            throw new IllegalArgumentException("Servings must be at least 1.");
        }
    }
}