package view;

import interface_adapter.meal_plan.AddMealToPlanController;
import interface_adapter.meal_plan.MealPlanState;
import interface_adapter.meal_plan.MealPlanViewModel;
import interface_adapter.meal_plan.MealPlanViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The weekly meal plan screen: pick a saved recipe, a day and a meal slot, then
 * add it to the plan. Shows the plan as a grid of meal slots by day.
 */
public class MealPlanView extends JPanel implements MealPlanViewModelListener {
    private final MealPlanViewModel viewModel;
    private final AddMealToPlanController controller;

    private final JComboBox<RecipeChoice> recipeChoices = new JComboBox<>();
    private final JComboBox<String> dayChoices = new JComboBox<>();
    private final JComboBox<String> slotChoices = new JComboBox<>();
    private final JLabel statusLabel = new JLabel(" ");
    private final DefaultTableModel planTableModel;

    /**
     * Creates the meal plan screen.
     *
     * @param viewModel  where the screen's state comes from
     * @param controller called when the user adds a meal
     */
    public MealPlanView(MealPlanViewModel viewModel, AddMealToPlanController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        viewModel.addListener(this);

        for (String day : viewModel.getDays()) {
            dayChoices.addItem(day);
        }
        for (String slot : viewModel.getMealSlots()) {
            slotChoices.addItem(slot);
        }

        JButton addButton = new JButton("Add to plan");
        addButton.addActionListener(event -> addSelectedMeal(false));

        JPanel choices = new JPanel(new FlowLayout(FlowLayout.LEFT));
        choices.add(new JLabel("Recipe:"));
        choices.add(recipeChoices);
        choices.add(new JLabel("Day:"));
        choices.add(dayChoices);
        choices.add(new JLabel("Meal:"));
        choices.add(slotChoices);
        choices.add(addButton);

        planTableModel = new DefaultTableModel(columnNames(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable planTable = new JTable(planTableModel);
        planTable.setRowHeight(48);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(new JLabel("Weekly meal plan"), BorderLayout.NORTH);
        JPanel centre = new JPanel(new BorderLayout());
        centre.add(choices, BorderLayout.NORTH);
        centre.add(new JScrollPane(planTable), BorderLayout.CENTER);
        add(centre, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        refresh(viewModel.getState());
    }

    /**
     * Returns the name the view manager uses for this screen.
     *
     * @return the view name
     */
    public String getViewName() {
        return MealPlanViewModel.VIEW_NAME;
    }

    @Override
    public void stateChanged() {
        MealPlanState state = viewModel.getState();
        // take...() clears each one-off message, so a later refresh never shows it again.
        String errorMessage = state.takeErrorMessage();
        String recipeToReplace = state.takeRecipeToReplace();
        if (errorMessage != null) {
            JOptionPane.showMessageDialog(this, errorMessage,
                    "Meal not added", JOptionPane.ERROR_MESSAGE);
        } else if (recipeToReplace != null) {
            int answer = JOptionPane.showConfirmDialog(this,
                    "This slot already has " + recipeToReplace + ". Replace it?",
                    "Replace meal?", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                addSelectedMeal(true);
                return;
            }
        }
        refresh(state);
    }


    private void addSelectedMeal(boolean replaceExisting) {
        RecipeChoice recipe = (RecipeChoice) recipeChoices.getSelectedItem();
        if (recipe == null) {
            JOptionPane.showMessageDialog(this, "Save a recipe first.");
            return;
        }
        controller.execute(recipe.apiId, (String) dayChoices.getSelectedItem(),
                (String) slotChoices.getSelectedItem(), replaceExisting);
    }

    private void refresh(MealPlanState state) {
        Object selected = recipeChoices.getSelectedItem();
        recipeChoices.removeAllItems();
        for (Map.Entry<String, String> option : state.getRecipeOptions().entrySet()) {
            RecipeChoice choice = new RecipeChoice(option.getKey(), option.getValue());
            recipeChoices.addItem(choice);
            if (choice.equals(selected)) {
                recipeChoices.setSelectedItem(choice);
            }
        }

        planTableModel.setRowCount(0);
        List<String> days = viewModel.getDays();
        for (String slot : viewModel.getMealSlots()) {
            Object[] row = new Object[days.size() + 1];
            row[0] = displayName(slot);
            for (int i = 0; i < days.size(); i++) {
                row[i + 1] = state.getRecipeName(days.get(i), slot);
            }
            planTableModel.addRow(row);
        }

        statusLabel.setText(state.getStatusMessage().isEmpty() ? " " : state.getStatusMessage());
    }

    private Object[] columnNames() {
        List<String> days = viewModel.getDays();
        Object[] names = new Object[days.size() + 1];
        names[0] = "";
        for (int i = 0; i < days.size(); i++) {
            names[i + 1] = displayName(days.get(i));
        }
        return names;
    }

    /** Turns "MONDAY" into "Monday". */
    private static String displayName(String enumName) {
        return enumName.charAt(0) + enumName.substring(1).toLowerCase(Locale.ROOT);
    }

    /** A recipe in the drop-down: shows the name, remembers the id. */
    private static final class RecipeChoice {
        private final String apiId;
        private final String name;

        private RecipeChoice(String apiId, String name) {
            this.apiId = apiId;
            this.name = name;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof RecipeChoice && apiId.equals(((RecipeChoice) other).apiId);
        }

        @Override
        public int hashCode() {
            return apiId.hashCode();
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

