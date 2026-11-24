package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.Recipe;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link InMemoryRecipeDataAccessObject}.
 */
class InMemoryRecipeDataAccessObjectTest {

  @Test
  void unknownApiIdDoesNotExistAndLookupReturnsNull() {
    // Arrange: an empty DAO
    InMemoryRecipeDataAccessObject dataAccess = new InMemoryRecipeDataAccessObject();

    // Act: check and look up an apiId that was never saved
    boolean exists = dataAccess.existsByApiId("52772");
    Recipe found = dataAccess.getSavedRecipe("52772");

    // Assert: exists is false, lookup is null
    assertFalse(exists);
    assertNull(found);
  }

  @Test
  void savedRecipeExistsAndLookupReturnsIt() {
    // Arrange: an empty DAO and one recipe
    InMemoryRecipeDataAccessObject dataAccess = new InMemoryRecipeDataAccessObject();
    Recipe recipe = recipe("52772", "Chicken Teriyaki");

    // Act: save the recipe, then check and look up its apiId
    dataAccess.save(recipe);
    boolean exists = dataAccess.existsByApiId("52772");
    Recipe found = dataAccess.getSavedRecipe("52772");

    // Assert: exists is true, lookup has the saved recipe's apiId and name
    assertTrue(exists);
    assertNotNull(found);
    assertEquals("52772", found.getApiId());
    assertEquals("Chicken Teriyaki", found.getName());
  }

  @Test
  void eachLookupReturnsItsOwnRecipe() {
    // Arrange: an empty DAO and two recipes with different apiIds
    InMemoryRecipeDataAccessObject dataAccess = new InMemoryRecipeDataAccessObject();
    Recipe teriyaki = recipe("52772", "Chicken Teriyaki");
    Recipe barbecue = recipe("52662", "Chicken Barbeque");

    // Act: save both, then look up each apiId
    dataAccess.save(teriyaki);
    dataAccess.save(barbecue);
    Recipe foundTeriyaki = dataAccess.getSavedRecipe("52772");
    Recipe foundBarbecue = dataAccess.getSavedRecipe("52662");

    // Assert: each lookup has its own recipe's apiId and name
    assertNotNull(foundTeriyaki);
    assertNotNull(foundBarbecue);
    assertEquals("52772", foundTeriyaki.getApiId());
    assertEquals("Chicken Teriyaki", foundTeriyaki.getName());
    assertEquals("52662", foundBarbecue.getApiId());
    assertEquals("Chicken Barbeque", foundBarbecue.getName());
  }

  @Test
  void nothingSavedReturnsEmptyList() {
    // Arrange: an empty DAO
    InMemoryRecipeDataAccessObject dataAccess = new InMemoryRecipeDataAccessObject();

    // Act: get the saved recipes
    List<Recipe> saved = dataAccess.getSavedRecipes();

    // Assert: the list is empty
    assertTrue(saved.isEmpty());
  }

  @Test
  void savedRecipesComeBackInSavedOrder() {
    // Arrange: an empty DAO and many apiIds in non-sorted order
    InMemoryRecipeDataAccessObject dataAccess = new InMemoryRecipeDataAccessObject();
    List<String> apiIds = List.of("52900", "52772", "53010", "52662", "52999",
        "52800", "53065", "52701", "52955", "52834");

    // Act: save each one, then get the saved recipes
    for (String apiId : apiIds) {
      dataAccess.save(recipe(apiId, "Recipe " + apiId));
    }
    List<String> savedApiIds = new ArrayList<>();
    for (Recipe saved : dataAccess.getSavedRecipes()) {
      savedApiIds.add(saved.getApiId());
    }

    // Assert: the apiIds match the order they were saved in
    assertEquals(apiIds, savedApiIds);
  }

  @Test
  void changingReturnedListDoesNotChangeStoredRecipes() {
    // Arrange: a DAO with one saved recipe
    InMemoryRecipeDataAccessObject dataAccess = new InMemoryRecipeDataAccessObject();
    dataAccess.save(recipe("52772", "Chicken Teriyaki"));

    // Act: clear the returned list
    dataAccess.getSavedRecipes().clear();

    // Assert: the recipe is still stored
    List<Recipe> saved = dataAccess.getSavedRecipes();
    assertEquals(1, saved.size());
    assertEquals("52772", saved.get(0).getApiId());
  }

  /**
   * Builds a recipe where only the apiId and name matter to the test.
   */
  private static Recipe recipe(String apiId, String name) {
    return new Recipe(apiId, name, "", List.of(), "", "");
  }
}
