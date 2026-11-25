package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests for {@link Ingredient}. */
class IngredientTest {

  // Group 1: constructor

  @Test
  void storesNameAndMeasure() {
    Ingredient ingredient = new Ingredient("Sugar", "1 tablespoon");
    assertEquals("Sugar", ingredient.getName());
    assertEquals("1 tablespoon", ingredient.getMeasure());
  }

  @Test
  void trimsNameAndMeasure() {
    Ingredient ingredient = new Ingredient("  Soy Sauce  ", "  3/4 cup  ");

    assertEquals("Soy Sauce", ingredient.getName());
    assertEquals("3/4 cup", ingredient.getMeasure());
  }

  @Test
  void nullMeasureBecomesEmpty() {
    Ingredient ingredient = new Ingredient("Salt", null);

    assertEquals("", ingredient.getMeasure());
  }

  // Group 2: invalid name

  @Test
  void nullNameThrows() {
    assertThrows(IllegalArgumentException.class, () -> new Ingredient(null, "1 cup"));
  }

  @Test
  void blankNameThrows() {
    assertThrows(IllegalArgumentException.class, () -> new Ingredient("   ", "1 cup"));
  }

  // Group 3: equals

  @Test
  void equalsItself() {
    Ingredient ingredient = new Ingredient("Salt", "1 tsp");

    assertTrue(ingredient.equals(ingredient));
  }

  @Test
  void equalsSameNameAndMeasure() {
    Ingredient first = new Ingredient("Salt", "1 tsp");
    Ingredient second = new Ingredient("Salt", "1 tsp");

    assertTrue(first.equals(second));
  }

  @Test
  void notEqualToDifferentName() {
    Ingredient salt = new Ingredient("Salt", "1 tsp");
    Ingredient sugar = new Ingredient("Sugar", "1 tsp");

    assertFalse(salt.equals(sugar));
  }

  @Test
  void notEqualToDifferentMeasure() {
    Ingredient oneTeaspoon = new Ingredient("Salt", "1 tsp");
    Ingredient twoTeaspoons = new Ingredient("Salt", "2 tsp");

    assertFalse(oneTeaspoon.equals(twoTeaspoons));
  }

  @Test
  void notEqualToAnotherType() {
    Ingredient ingredient = new Ingredient("Salt", "1 tsp");
    Object text = "1 tsp Salt";

    assertFalse(ingredient.equals(text));
  }

  // Group 4: hashCode

  @Test
  void equalIngredientsHaveEqualHashCodes() {
    Ingredient first = new Ingredient("Salt", "1 tsp");
    Ingredient second = new Ingredient("Salt", "1 tsp");

    assertEquals(first.hashCode(), second.hashCode());
  }

  // Group 5: toString

  @Test
  void toStringWithMeasure() {
    Ingredient ingredient = new Ingredient("Salt", "1 tsp");

    String text = ingredient.toString();

    assertEquals("1 tsp Salt", text);
  }

  @Test
  void toStringWithoutMeasure() {
    Ingredient ingredient = new Ingredient("Salt", "");

    String text = ingredient.toString();

    assertEquals("Salt", text);
  }
}
