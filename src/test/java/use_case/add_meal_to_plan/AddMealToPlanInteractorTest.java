package use_case.add_meal_to_plan;

import data_access.InMemoryMealPlanDataAccessObject;
import entity.Ingredient;
import entity.MealPlan;
import entity.MealPlanEntry;
import entity.MealType;
import entity.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddMealToPlanInteractorTest {

    private static final LocalDate WEEK_START = LocalDate.of(2026, 11, 16);

    private final Recipe teriyaki = new Recipe("52772", "Teriyaki Chicken Casserole", "Chicken",
            List.of(new Ingredient("soy sauce", "3/4 cup")), "Cook it.", "");
    private final Recipe pancakes = new Recipe("52854", "Pancakes", "Dessert",
            List.of(new Ingredient("flour", "100g")), "Fry them.", "");

    private InMemoryMealPlanDataAccessObject dataAccess;
    private RecordingPresenter presenter;
    private AddMealToPlanInteractor interactor;

    @BeforeEach
    void setUp() {
        dataAccess = new InMemoryMealPlanDataAccessObject(WEEK_START);
        dataAccess.addSavedRecipe(teriyaki);
        dataAccess.addSavedRecipe(pancakes);
        presenter = new RecordingPresenter();
        interactor = new AddMealToPlanInteractor(dataAccess, presenter);
    }

    @Test
    void addToEmptySlotSucceeds() {
        interactor.execute(new AddMealToPlanInputData("52772", DayOfWeek.MONDAY, MealType.DINNER, false));

        assertNotNull(presenter.successData);
        assertEquals("Teriyaki Chicken Casserole", presenter.successData.getAddedRecipeName());
        List<PlannedMeal> meals = presenter.successData.getPlannedMeals();
        assertEquals(1, meals.size());
        assertEquals(DayOfWeek.MONDAY, meals.get(0).getDay());
        assertEquals(MealType.DINNER, meals.get(0).getMealType());
        assertSame(teriyaki, dataAccess.getMealPlan().getEntry(DayOfWeek.MONDAY, MealType.DINNER).getRecipe());
    }

    @Test
    void filledSlotAsksBeforeReplacing() {
        interactor.execute(new AddMealToPlanInputData("52772", DayOfWeek.MONDAY, MealType.DINNER, false));
        presenter = new RecordingPresenter();
        interactor = new AddMealToPlanInteractor(dataAccess, presenter);

        interactor.execute(new AddMealToPlanInputData("52854", DayOfWeek.MONDAY, MealType.DINNER, false));

        assertEquals("Teriyaki Chicken Casserole", presenter.replaceRecipeName);
        assertNull(presenter.successData);
        assertSame(teriyaki, dataAccess.getMealPlan().getEntry(DayOfWeek.MONDAY, MealType.DINNER).getRecipe());
    }

    @Test
    void filledSlotIsReplacedWhenConfirmed() {
        interactor.execute(new AddMealToPlanInputData("52772", DayOfWeek.MONDAY, MealType.DINNER, false));

        interactor.execute(new AddMealToPlanInputData("52854", DayOfWeek.MONDAY, MealType.DINNER, true));

        MealPlan plan = dataAccess.getMealPlan();
        assertSame(pancakes, plan.getEntry(DayOfWeek.MONDAY, MealType.DINNER).getRecipe());
        assertEquals(1, plan.getEntries().size());
    }

    @Test
    void unsavedRecipeFails() {
        interactor.execute(new AddMealToPlanInputData("99999", DayOfWeek.MONDAY, MealType.DINNER, false));

        assertNotNull(presenter.errorMessage);
        assertTrue(dataAccess.getMealPlan().getEntries().isEmpty());
    }

    @Test
    void storageFailureLeavesPlanUnchanged() {
        AddMealToPlanDataAccessInterface failingDataAccess = new AddMealToPlanDataAccessInterface() {
            @Override
            public Recipe getSavedRecipe(String apiId) {
                return dataAccess.getSavedRecipe(apiId);
            }

            @Override
            public MealPlan getMealPlan() {
                return dataAccess.getMealPlan();
            }

            @Override
            public void saveEntry(MealPlanEntry entry) throws MealPlanStorageException {
                throw new MealPlanStorageException("Database unavailable.");
            }
        };
        interactor = new AddMealToPlanInteractor(failingDataAccess, presenter);

        interactor.execute(new AddMealToPlanInputData("52772", DayOfWeek.MONDAY, MealType.DINNER, false));

        assertEquals("The meal was not added. Please try again.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertTrue(dataAccess.getMealPlan().getEntries().isEmpty());
    }

    /**
     * Remembers which output method the interactor called, so tests can check it.
     */
    private static class RecordingPresenter implements AddMealToPlanOutputBoundary {
        private AddMealToPlanOutputData successData;
        private String replaceRecipeName;
        private String errorMessage;

        @Override
        public void prepareSuccessView(AddMealToPlanOutputData outputData) {
            successData = outputData;
        }

        @Override
        public void prepareConfirmReplaceView(String currentRecipeName) {
            replaceRecipeName = currentRecipeName;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}

