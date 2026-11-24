package use_case.generate_grocery_list;

import data_access.InMemoryGroceryListDataAccessObject;
import entity.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tests for {@link GenerateGroceryListInteractor}, using in-memory storage and
 * a presenter that records what it receives.
 */
public class GenerateGroceryListInteractorTest {

    /**
     * Records what the interactor presents; a field stays null if its method
     * was not called.
     */
    private static class RecordingPresenter implements GenerateGroceryListOutputBoundary {

        GenerateGroceryListOutputData outputData;
        String error;

        @Override
        public void prepareSuccessView(GenerateGroceryListOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            this.error = error;
        }
    }

    /**
     * Creates a recipe with the given ingredients and placeholder values for
     * the fields these tests do not use.
     */
    private static Recipe recipe(String id, Ingredient... ingredients) {
        return new Recipe(id, "Recipe " + id, "", List.of(ingredients), "", "");
    }

    /**
     * Creates a plan with each recipe as dinner on its own day, starting on
     * Monday, so no recipe replaces another. Takes at most seven recipes.
     */
    private static MealPlan planWith(Recipe... recipes) {
        MealPlan mealPlan = new MealPlan(LocalDate.of(2023, 3, 13));

        for (int i = 0; i < recipes.length; i++) {
            mealPlan.addEntry(new MealPlanEntry(DayOfWeek.values()[i], MealType.DINNER, recipes
            [i]));
        }

        return mealPlan;


    }

    @Test
    void emptyPlanCallsFailView() {
        InMemoryGroceryListDataAccessObject dao = new InMemoryGroceryListDataAccessObject(() -> planWith());
        RecordingPresenter presenter = new RecordingPresenter();
        GenerateGroceryListInteractor interactor = new GenerateGroceryListInteractor(dao, presenter);

        interactor.execute();

        assertEquals("Add meals to your plan first.", presenter.error);
        assertNull(presenter.outputData);
        assertNull(dao.getSavedItems());
    }

    @Test
    void sharedIngredientIsMerged() {
        Recipe a = recipe("1", new Ingredient("Onion", "1"));
        Recipe b = recipe("2", new Ingredient("Onion", "3"));

        InMemoryGroceryListDataAccessObject dao = new InMemoryGroceryListDataAccessObject(() -> planWith(a, b));
        RecordingPresenter presenter = new RecordingPresenter();
        GenerateGroceryListInteractor interactor = new GenerateGroceryListInteractor(dao, presenter);

        interactor.execute();

        assertEquals(Map.of("Onion", List.of("1", "3")), presenter.outputData.getItems());
    }

    @Test
    void differentCaseIsOneItem() {
        Recipe a = recipe("1", new Ingredient("Onion", "1"));
        Recipe b = recipe("2", new Ingredient("onion", "3"));

        InMemoryGroceryListDataAccessObject dao = new InMemoryGroceryListDataAccessObject(() -> planWith(a, b));
        RecordingPresenter presenter = new RecordingPresenter();
        GenerateGroceryListInteractor interactor = new GenerateGroceryListInteractor(dao, presenter);

        interactor.execute();

        assertEquals(Set.of("Onion"), presenter.outputData.getItems().keySet());
    }

    @Test
    void blankMeasureGivesEmptyMeasure() {
        Recipe a = recipe("1", new Ingredient("Salt", ""));

        InMemoryGroceryListDataAccessObject dao = new InMemoryGroceryListDataAccessObject(() -> planWith(a));
        RecordingPresenter presenter = new RecordingPresenter();
        GenerateGroceryListInteractor interactor = new GenerateGroceryListInteractor(dao, presenter);

        interactor.execute();

        assertEquals(Map.of("Salt", List.of()), presenter.outputData.getItems());
    }

    @Test
    void itemsKeepFirstSeenOrder() {
        Recipe a = recipe("1", new Ingredient("Rice", "1"), new Ingredient("Onion", "1"));
        Recipe b = recipe("2", new Ingredient("Rice", "3"));

        InMemoryGroceryListDataAccessObject dao = new InMemoryGroceryListDataAccessObject(() -> planWith(a, b));
        RecordingPresenter presenter = new RecordingPresenter();
        GenerateGroceryListInteractor interactor = new GenerateGroceryListInteractor(dao, presenter);

        interactor.execute();

        assertEquals(List.of("Rice", "Onion"), new ArrayList<>(presenter.outputData.getItems().keySet()));
    }

    @Test
    void savedListMatchesPresented() {
        Recipe a = recipe("1", new Ingredient("Rice", "1 cup"), new Ingredient("Onion", "1"));
        Recipe b = recipe("2", new Ingredient("Garlic", "2 cloves"), new Ingredient("Rice", "2 cups"));

        InMemoryGroceryListDataAccessObject dao = new InMemoryGroceryListDataAccessObject(() -> planWith(a, b));
        RecordingPresenter presenter = new RecordingPresenter();
        GenerateGroceryListInteractor interactor = new GenerateGroceryListInteractor(dao, presenter);

        interactor.execute();

        assertNotNull(dao.getSavedItems(), "the list should have been saved");
        Map<String, List<String>> saved = new LinkedHashMap<>();
        for (GroceryItem item : dao.getSavedItems()) {
            saved.put(item.getIngredientName(), item.getMeasures());
        }
        Map<String, List<String>> presented = presenter.outputData.getItems();

        assertEquals(presented, saved);
        assertEquals(new ArrayList<>(presented.keySet()), new ArrayList<>(saved.keySet()));
    }

    @Test
    void recipePlannedTwiceDoublesMeasures() {
        Recipe a = recipe("1", new Ingredient("Onion", "1"));

        InMemoryGroceryListDataAccessObject dao = new InMemoryGroceryListDataAccessObject(() -> planWith(a, a));
        RecordingPresenter presenter = new RecordingPresenter();
        GenerateGroceryListInteractor interactor = new GenerateGroceryListInteractor(dao, presenter);

        interactor.execute();

        assertEquals(Map.of("Onion", List.of("1", "1")), presenter.outputData.getItems());
    }

}