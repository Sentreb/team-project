package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Tests for {@link Recipe}. */
class RecipeTest {

  // Group 1: constructors and null defaults

  @Test
  void fullConstructorStoresEveryField() {
    List<Ingredient> ingredients = List.of(
        new Ingredient("Chicken", "500 g"), new Ingredient("Soy Sauce", "3/4 cup"));

    Recipe recipe = new Recipe("52772", "Teriyaki Chicken", "Chicken", ingredients,
        "Bake for 30 minutes.", "https://example.com/teriyaki.jpg", 4, "Use less sugar.");

    assertEquals("52772", recipe.getApiId());
    assertEquals("Teriyaki Chicken", recipe.getName());
    assertEquals("Chicken", recipe.getCategory());
    assertEquals(
        List.of(new Ingredient("Chicken", "500 g"), new Ingredient("Soy Sauce", "3/4 cup")),
        recipe.getIngredients());
    assertEquals("Bake for 30 minutes.", recipe.getInstructions());
    assertEquals("https://example.com/teriyaki.jpg", recipe.getImageUrl());
    assertEquals(4, recipe.getServings());
    assertEquals("Use less sugar.", recipe.getUserNotes());
  }

  @Test
  void shortConstructorUsesOneServingAndNoNotes() {
    Recipe recipe = new Recipe("52772", "Teriyaki Chicken", "Chicken", List.of(),
        "Bake for 30 minutes.", "https://example.com/teriyaki.jpg");

    assertEquals(1, recipe.getServings());
    assertEquals("", recipe.getUserNotes());
  }

  @Test
  void nullOptionalTextBecomesEmpty() {
    Recipe recipe = new Recipe("52772", "Teriyaki Chicken", null, List.of(),
        null, null, 4, null);

    assertEquals("", recipe.getCategory());
    assertEquals("", recipe.getInstructions());
    assertEquals("", recipe.getImageUrl());
    assertEquals("", recipe.getUserNotes());
  }

  // Group 2: invalid arguments

  @Test
  void nullApiIdThrows() {
    assertThrows(IllegalArgumentException.class, () -> new Recipe(
        null, "Teriyaki Chicken", "Chicken", List.of(),
        "Bake for 30 minutes.", "https://example.com/teriyaki.jpg"));
  }

  @Test
  void blankApiIdThrows() {
    assertThrows(IllegalArgumentException.class, () -> new Recipe(
        "   ", "Teriyaki Chicken", "Chicken", List.of(),
        "Bake for 30 minutes.", "https://example.com/teriyaki.jpg"));
  }

  @Test
  void nullNameThrows() {
    assertThrows(IllegalArgumentException.class, () -> new Recipe(
        "52772", null, "Chicken", List.of(),
        "Bake for 30 minutes.", "https://example.com/teriyaki.jpg"));
  }

  @Test
  void blankNameThrows() {
    assertThrows(IllegalArgumentException.class, () -> new Recipe(
        "52772", "   ", "Chicken", List.of(),
        "Bake for 30 minutes.", "https://example.com/teriyaki.jpg"));
  }

  @Test
  void nullIngredientsThrows() {
    assertThrows(IllegalArgumentException.class, () -> new Recipe(
        "52772", "Teriyaki Chicken", "Chicken", null,
        "Bake for 30 minutes.", "https://example.com/teriyaki.jpg"));
  }

  @Test
  void ingredientsContainingNullThrows() {
    List<Ingredient> ingredients = Arrays.asList(new Ingredient("Salt", "1 tsp"), null);

    assertThrows(IllegalArgumentException.class, () -> new Recipe(
        "52772", "Teriyaki Chicken", "Chicken", ingredients,
        "Bake for 30 minutes.", "https://example.com/teriyaki.jpg"));
  }

  @Test
  void zeroServingsThrows() {
    assertThrows(IllegalArgumentException.class, () -> new Recipe(
        "52772", "Teriyaki Chicken", "Chicken", List.of(),
        "Bake for 30 minutes.", "https://example.com/teriyaki.jpg", 0, ""));
  }

  // Group 3: protected ingredient list

  @Test
  void ingredientsCannotBeChangedThroughGetter() {
    Recipe recipe = createRecipe(List.of(new Ingredient("Salt", "1 tsp")));
    List<Ingredient> returned = recipe.getIngredients();
    Ingredient sugar = new Ingredient("Sugar", "1 tbsp");

    assertThrows(UnsupportedOperationException.class, () -> returned.add(sugar));
    assertEquals(List.of(new Ingredient("Salt", "1 tsp")), recipe.getIngredients());
  }

  @Test
  void changingOriginalListDoesNotChangeRecipe() {
    List<Ingredient> original = new ArrayList<>();
    original.add(new Ingredient("Salt", "1 tsp"));
    Recipe recipe = createRecipe(original);

    original.add(new Ingredient("Sugar", "1 tbsp"));

    assertEquals(List.of(new Ingredient("Salt", "1 tsp")), recipe.getIngredients());
  }

  // Group 4: updateNotes and setServings

  @Test
  void updateNotesSetsNotes() {
    Recipe recipe = createRecipe(List.of());

    recipe.updateNotes("Add more garlic.");

    assertEquals("Add more garlic.", recipe.getUserNotes());
  }

  @Test
  void updateNotesWithNullClearsNotes() {
    Recipe recipe = createRecipe(List.of());

    recipe.updateNotes(null);

    assertEquals("", recipe.getUserNotes());
  }

  @Test
  void setServingsSetsServings() {
    Recipe recipe = createRecipe(List.of());

    recipe.setServings(6);

    assertEquals(6, recipe.getServings());
  }

  @Test
  void setServingsToZeroThrowsAndKeepsOldValue() {
    Recipe recipe = createRecipe(List.of());

    assertThrows(IllegalArgumentException.class, () -> recipe.setServings(0));
    assertEquals(4, recipe.getServings());
  }

  // Group 5: replaceIngredient

  @Test
  void replaceIngredientKeepsPosition() {
    Recipe recipe = createRecipe(List.of(
        new Ingredient("Chicken", "500 g"),
        new Ingredient("Salt", "1 tsp"),
        new Ingredient("Soy Sauce", "3/4 cup")));

    recipe.replaceIngredient(new Ingredient("Salt", "1 tsp"), new Ingredient("Sugar", "1 tbsp"));

    assertEquals(
        List.of(
            new Ingredient("Chicken", "500 g"),
            new Ingredient("Sugar", "1 tbsp"),
            new Ingredient("Soy Sauce", "3/4 cup")),
        recipe.getIngredients());
  }

  @Test
  void replaceIngredientWithNullThrows() {
    Ingredient salt = new Ingredient("Salt", "1 tsp");
    Recipe recipe = createRecipe(List.of(salt));

    assertThrows(IllegalArgumentException.class, () -> recipe.replaceIngredient(salt, null));
    assertEquals(List.of(salt), recipe.getIngredients());
  }

  @Test
  void replaceUnknownIngredientThrows() {
    Ingredient salt = new Ingredient("Salt", "1 tsp");
    Ingredient unknown = new Ingredient("Pepper", "1 tsp");
    Ingredient sugar = new Ingredient("Sugar", "1 tbsp");
    Recipe recipe = createRecipe(List.of(salt));

    assertThrows(IllegalArgumentException.class, () -> recipe.replaceIngredient(unknown, sugar));
    assertEquals(List.of(salt), recipe.getIngredients());
  }

  /** Creates a valid recipe with 4 servings, notes and the given ingredients. */
  private static Recipe createRecipe(List<Ingredient> ingredients) {
    return new Recipe("52772", "Teriyaki Chicken", "Chicken", ingredients,
        "Bake for 30 minutes.", "https://example.com/teriyaki.jpg", 4, "Use less sugar.");
  }
}
