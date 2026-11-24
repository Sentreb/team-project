package view;

import interface_adapter.save_recipe.SaveRecipeController;
import interface_adapter.save_recipe.SaveRecipeState;
import interface_adapter.save_recipe.SaveRecipeViewModel;
import interface_adapter.save_recipe.SaveRecipeViewModelListener;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Shows the recipe the user picked, a button to save it and the result of
 * the save.
 */
public class SaveRecipePanel extends JPanel implements SaveRecipeViewModelListener {

  private static final String NO_RECIPE_TEXT = "Pick a recipe to save.";
  private static final String EMPTY_MESSAGE = " ";

  private final SaveRecipeViewModel saveRecipeViewModel;
  private final SaveRecipeController saveRecipeController;

  private final JLabel nameLabel = new JLabel(NO_RECIPE_TEXT);
  private final JButton saveButton = new JButton("Save");
  private final JLabel messageLabel = new JLabel(EMPTY_MESSAGE);

  private String selectedApiId;

  /**
   * Creates the panel with no recipe picked.
   *
   * @param saveRecipeViewModel the view model that holds the save result
   * @param saveRecipeController the controller called when Save is clicked
   */
  public SaveRecipePanel(SaveRecipeViewModel saveRecipeViewModel,
                         SaveRecipeController saveRecipeController) {
    this.saveRecipeViewModel = saveRecipeViewModel;
    this.saveRecipeController = saveRecipeController;

    layOutComponents();
    saveButton.setEnabled(false);
    saveButton.addActionListener(event -> saveSelectedRecipe());
    saveRecipeViewModel.addListener(this);
  }

  /**
   * Shows the picked recipe, clears the old message and enables Save.
   *
   * @param apiId the TheMealDB id of the picked recipe
   * @param name the name of the picked recipe
   */
  public void setSelectedRecipe(String apiId, String name) {
    selectedApiId = apiId;
    nameLabel.setText(name);
    messageLabel.setText(EMPTY_MESSAGE);
    saveButton.setEnabled(true);
  }

  /**
   * Shows the success or error message of the last save.
   */
  @Override
  public void stateChanged() {
    final SaveRecipeState state = saveRecipeViewModel.getState();
    if (state.getErrorMessage() != null) {
      messageLabel.setText(state.getErrorMessage());
    } else if (state.getSuccessMessage() != null) {
      messageLabel.setText(state.getSuccessMessage());
    } else {
      messageLabel.setText(EMPTY_MESSAGE);
    }
  }

  private void saveSelectedRecipe() {
    saveRecipeController.execute(selectedApiId);
  }

  private void layOutComponents() {
    setLayout(new BorderLayout(10, 6));
    setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));
    add(nameLabel, BorderLayout.CENTER);
    add(saveButton, BorderLayout.EAST);
    add(messageLabel, BorderLayout.SOUTH);
  }
}
