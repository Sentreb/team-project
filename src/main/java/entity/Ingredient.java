package entity;

import java.util.Objects;

/**
 * One ingredient line in a recipe, such as "Soy Sauce" with measure "3/4 cup".
 * Immutable: to change an ingredient, replace it in its Recipe.
 */
public class Ingredient {
    private final String name;
    private final String measure;

    /**
     * Creates an ingredient.
     *
     * @param name    the ingredient name; must not be null or blank
     * @param measure the amount, such as "2 tbsp"; null is stored as an empty
     *                string because TheMealDB leaves some measures blank
     * @throws IllegalArgumentException if name is null or blank
     */
    public Ingredient(String name, String measure) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ingredient name must not be blank.");
        }
        this.name = name.trim();
        this.measure = measure == null ? "" : measure.trim();
    }

    public String getName() {
        return name;
    }

    public String getMeasure() {
        return measure;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Ingredient)) {
            return false;
        }
        Ingredient that = (Ingredient) other;
        return name.equals(that.name) && measure.equals(that.measure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, measure);
    }

    @Override
    public String toString() {
        return measure.isEmpty() ? name : measure + " " + name;
    }
}