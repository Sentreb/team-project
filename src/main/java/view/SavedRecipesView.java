package view;

import interface_adapter.view_saved_recipes.ViewSavedRecipesController;
import interface_adapter.view_saved_recipes.ViewSavedRecipesState;
import interface_adapter.view_saved_recipes.ViewSavedRecipesViewModel;
import interface_adapter.view_saved_recipes.ViewSavedRecipesViewModelListener;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * Lists the names of the saved recipes, or a message when there are none.
 * Reloads each time it is shown.
 */
public class SavedRecipesView extends JPanel implements ViewSavedRecipesViewModelListener {

  private static final String TITLE = "Saved Recipes";
  private static final String NO_MESSAGE = " ";

  private final ViewSavedRecipesViewModel viewSavedRecipesViewModel;
  private final ViewSavedRecipesController viewSavedRecipesController;

  private final DefaultListModel<String> nameListModel = new DefaultListModel<>();
  private final JLabel messageLabel = new JLabel(NO_MESSAGE, SwingConstants.CENTER);

  /**
   * Creates the view. It stays empty until it is shown.
   *
   * @param viewSavedRecipesViewModel the view model that holds the list
   * @param viewSavedRecipesController the controller called each time the view is shown
   */
  public SavedRecipesView(ViewSavedRecipesViewModel viewSavedRecipesViewModel,
                          ViewSavedRecipesController viewSavedRecipesController) {
    this.viewSavedRecipesViewModel = viewSavedRecipesViewModel;
    this.viewSavedRecipesController = viewSavedRecipesController;

    layOutComponents();
    reloadWhenShown();
    viewSavedRecipesViewModel.addListener(this);
  }

  /**
   * Shows the current names and message.
   */
  @Override
  public void stateChanged() {
    final ViewSavedRecipesState state = viewSavedRecipesViewModel.getState();
    nameListModel.clear();
    for (String name : state.getRecipeNames()) {
      nameListModel.addElement(name);
    }
    messageLabel.setText(state.getMessage() == null ? NO_MESSAGE : state.getMessage());
  }

  /**
   * Returns the name of this view in the card layout.
   *
   * @return the view name
   */
  public String getViewName() {
    return ViewSavedRecipesViewModel.VIEW_NAME;
  }

  private void reloadWhenShown() {
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent event) {
        viewSavedRecipesController.execute();
      }
    });
  }

  private void layOutComponents() {
    final JLabel titleLabel = new JLabel(TITLE, SwingConstants.CENTER);
    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    add(titleLabel, BorderLayout.NORTH);
    add(new JScrollPane(new JList<>(nameListModel)), BorderLayout.CENTER);
    add(messageLabel, BorderLayout.SOUTH);
  }
}
