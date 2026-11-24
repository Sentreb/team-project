package app;

import data_access.InMemoryGroceryListDataAccessObject;
import data_access.InMemoryMealPlanDataAccessObject;
import data_access.InMemoryRecipeDataAccessObject;
import data_access.MealDbApiGateway;
import interface_adapter.ViewManagerModel;
import interface_adapter.grocery_list.GenerateGroceryListController;
import interface_adapter.grocery_list.GenerateGroceryListPresenter;
import interface_adapter.grocery_list.GroceryListViewModel;
import interface_adapter.meal_plan.MealPlanState;
import interface_adapter.meal_plan.MealPlanViewModel;
import interface_adapter.save_recipe.SaveRecipeController;
import interface_adapter.save_recipe.SaveRecipePresenter;
import interface_adapter.save_recipe.SaveRecipeState;
import interface_adapter.save_recipe.SaveRecipeViewModel;
import interface_adapter.search.SearchController;
import interface_adapter.search.SearchPresenter;
import interface_adapter.search.SearchResultItem;
import interface_adapter.search.SearchViewModel;
import interface_adapter.view_saved_recipes.ViewSavedRecipesController;
import interface_adapter.view_saved_recipes.ViewSavedRecipesPresenter;
import interface_adapter.view_saved_recipes.ViewSavedRecipesViewModel;
import use_case.generate_grocery_list.GenerateGroceryListInteractor;
import use_case.save_recipe.SaveRecipeInteractor;
import use_case.search_recipes.SearchRecipesInteractor;
import use_case.view_saved_recipes.ViewSavedRecipesInteractor;
import view.GroceryListView;
import view.MealPlanView;
import view.SaveRecipePanel;
import view.SearchView;
import view.ViewManager;
import view.SavedRecipesView;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builds the app: creates the data access objects, wires each use case
 * (interactor, presenter, controller, view model, view) and puts the views in
 * a card layout managed by the {@link ViewManager}.
 *
 * <p>Usage: call the {@code add...} methods, then {@link #build()}.</p>
 */
public class AppBuilder {
    private static final String HOME_VIEW_NAME = "home";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // Data access. In-memory for now; swap for the SQLite versions when they exist.
    private final MealDbApiGateway mealDbApiGateway = new MealDbApiGateway();
    private final InMemoryRecipeDataAccessObject recipeDataAccess = new InMemoryRecipeDataAccessObject();
    private final InMemoryMealPlanDataAccessObject mealPlanDataAccess = new InMemoryMealPlanDataAccessObject();
    // Reads the plan through mealPlanDataAccess on every call, so meals planned later are included.
    private final InMemoryGroceryListDataAccessObject groceryListDataAccess =
            new InMemoryGroceryListDataAccessObject(mealPlanDataAccess::getMealPlan);

    // Recipes saved this session (apiId -> name), used to fill the meal plan drop-down.
    // The recipe DAO has no "list all" method yet, so the app tracks them itself.
    private final Map<String, String> savedRecipeOptions = new LinkedHashMap<>();
    private SearchResultItem selectedSearchResult;

    private MealPlanViewModel mealPlanViewModel;
    private String searchViewName;
    private String mealPlanViewName;
    private String groceryListViewName;
    private String savedRecipesViewName;

    /**
     * Adds the home screen shown when the app starts.
     *
     * @return this builder
     */
    public AppBuilder addHomeView() {
        JLabel welcome = new JLabel("<html><div style='text-align:center'>Welcome to SOLID Meals<br>"
                + "Search for recipes, save them, and plan your week.</div></html>",
                SwingConstants.CENTER);
        welcome.setFont(welcome.getFont().deriveFont(Font.PLAIN, 16f));
        JPanel home = new JPanel(new BorderLayout());
        home.add(welcome, BorderLayout.CENTER);
        cardPanel.add(home, HOME_VIEW_NAME);
        return this;
    }

    /**
     * Adds the search screen with the Search Recipes and Save Recipe use cases.
     * Clicking a search result selects it for saving.
     *
     * @return this builder
     */
    public AppBuilder addSearchView() {
        // Search Recipes use case
        SearchViewModel searchViewModel = new SearchViewModel();
        SearchPresenter searchPresenter = new SearchPresenter(searchViewModel);
        SearchRecipesInteractor searchInteractor =
                new SearchRecipesInteractor(mealDbApiGateway, searchPresenter);
        SearchController searchController = new SearchController(searchInteractor);

        SearchView searchView = new SearchView(searchViewModel);
        searchView.setSearchController(searchController);
        searchView.setOnBack(() -> showView(HOME_VIEW_NAME));

        // Save Recipe use case
        SaveRecipeViewModel saveRecipeViewModel = new SaveRecipeViewModel();
        SaveRecipePresenter saveRecipePresenter = new SaveRecipePresenter(saveRecipeViewModel);
        SaveRecipeInteractor saveRecipeInteractor =
                new SaveRecipeInteractor(recipeDataAccess, mealDbApiGateway, saveRecipePresenter);
        SaveRecipeController saveRecipeController = new SaveRecipeController(saveRecipeInteractor);
        SaveRecipePanel saveRecipePanel = new SaveRecipePanel(saveRecipeViewModel, saveRecipeController);

        // Clicking a result picks it for saving.
        searchView.setOnRecipeSelected(item -> {
            selectedSearchResult = item;
            saveRecipePanel.setSelectedRecipe(item.getApiId(), item.getName());
        });

        // Remember what was saved so the meal plan can offer it.
        saveRecipeViewModel.addListener(() -> {
            SaveRecipeState state = saveRecipeViewModel.getState();
            if (state.getSuccessMessage() != null && selectedSearchResult != null) {
                savedRecipeOptions.put(selectedSearchResult.getApiId(), selectedSearchResult.getName());
            }
        });

        JPanel searchCard = new JPanel(new BorderLayout());
        searchCard.add(searchView, BorderLayout.CENTER);
        searchCard.add(saveRecipePanel, BorderLayout.SOUTH);

        searchViewName = searchView.getViewName();
        cardPanel.add(searchCard, searchViewName);
        return this;
    }

    /**
     * Adds the saved recipes screen with the View Saved Recipes use case.
     * The view is added straight to the card panel, because it reloads itself
     * on componentShown, which only fires on the card itself.
     *
     * @return this builder
     */
    public AppBuilder addSavedRecipesView() {
        ViewSavedRecipesViewModel viewModel = new ViewSavedRecipesViewModel();
        ViewSavedRecipesPresenter presenter = new ViewSavedRecipesPresenter(viewModel);
        ViewSavedRecipesInteractor interactor =
                new ViewSavedRecipesInteractor(recipeDataAccess, presenter);
        ViewSavedRecipesController controller = new ViewSavedRecipesController(interactor);
        SavedRecipesView savedRecipesView = new SavedRecipesView(viewModel, controller);

        savedRecipesViewName = savedRecipesView.getViewName();
        cardPanel.add(savedRecipesView, savedRecipesViewName);
        return this;
    }

    /**
     * Adds the weekly meal plan screen with the Add Meal to Plan use case.
     *
     * @return this builder
     */
    public AppBuilder addMealPlanView() {
        mealPlanViewModel = new MealPlanViewModel();
        MealPlanView mealPlanView =
                MealPlanUseCaseFactory.create(mealPlanViewModel, recipeDataAccess, mealPlanDataAccess);
        mealPlanViewName = mealPlanView.getViewName();
        cardPanel.add(mealPlanView, mealPlanViewName);
        return this;
    }

    /**
     * Adds the grocery list screen with the Generate Grocery List use case.
     *
     * @return this builder
     */
    public AppBuilder addGroceryListView() {
        GroceryListViewModel groceryListViewModel = new GroceryListViewModel();
        GenerateGroceryListPresenter groceryListPresenter =
                new GenerateGroceryListPresenter(groceryListViewModel);
        GenerateGroceryListInteractor groceryListInteractor =
                new GenerateGroceryListInteractor(groceryListDataAccess, groceryListPresenter);
        GenerateGroceryListController groceryListController =
                new GenerateGroceryListController(groceryListInteractor);
        GroceryListView groceryListView = new GroceryListView(groceryListViewModel, groceryListController);

        groceryListViewName = groceryListView.getViewName();
        cardPanel.add(groceryListView, groceryListViewName);
        return this;
    }

    /**
     * Creates the window: a navigation bar on top and the views below it.
     * Shows the home screen first.
     *
     * @return the application window, not yet visible
     */
    public JFrame build() {
        JFrame application = new JFrame("SOLID Meals");
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        application.setLayout(new BorderLayout());
        application.add(buildNavigationBar(), BorderLayout.NORTH);
        application.add(cardPanel, BorderLayout.CENTER);
        application.setMinimumSize(new Dimension(900, 520));
        application.pack();
        application.setLocationRelativeTo(null);

        showView(HOME_VIEW_NAME);
        return application;
    }

    private JPanel buildNavigationBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.LIGHT_GRAY));

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(event -> showView(HOME_VIEW_NAME));
        bar.add(homeButton);

        if (searchViewName != null) {
            JButton searchButton = new JButton("Search Recipes");
            searchButton.addActionListener(event -> showView(searchViewName));
            bar.add(searchButton);
        }
        if (savedRecipesViewName != null) {
            JButton savedButton = new JButton("Saved Recipes");
            savedButton.addActionListener(event -> showView(savedRecipesViewName));
            bar.add(savedButton);
        }
        if (mealPlanViewName != null) {
            JButton planButton = new JButton("Meal Plan");
            planButton.addActionListener(event -> showMealPlan());
            bar.add(planButton);
        }
        if (groceryListViewName != null) {
            JButton groceryButton = new JButton("Grocery List");
            groceryButton.addActionListener(event -> showView(groceryListViewName));
            bar.add(groceryButton);
        }
        return bar;
    }

    /** Refreshes the recipe drop-down from what has been saved, then shows the plan. */
    private void showMealPlan() {
        MealPlanState state = mealPlanViewModel.getState();
        state.setRecipeOptions(savedRecipeOptions);
        // Clear stale messages so the view does not pop up an old dialog on refresh.
        state.setErrorMessage(null);
        state.setRecipeToReplace(null);
        mealPlanViewModel.notifyStateChanged();
        showView(mealPlanViewName);
    }

    private void showView(String viewName) {
        viewManagerModel.setState(viewName);
        viewManagerModel.notifyViewChanged();
    }
}