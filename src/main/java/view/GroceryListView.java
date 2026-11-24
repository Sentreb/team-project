package view;

import interface_adapter.grocery_list.GenerateGroceryListController;
import interface_adapter.grocery_list.GroceryListItem;
import interface_adapter.grocery_list.GroceryListState;
import interface_adapter.grocery_list.GroceryListViewModel;
import interface_adapter.grocery_list.GroceryListViewModelListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * The grocery list screen: a button that generates the list from the weekly
 * plan, and a table with each ingredient once alongside all of its measures.
 */
public class GroceryListView extends JPanel implements GroceryListViewModelListener {

  private static final String TITLE = "Grocery List";
  private static final String EMPTY_MESSAGE =
      "Press Generate to make a list from your weekly plan.";
  private static final Color ERROR_COLOR = new Color(176, 0, 32);

  private final GroceryListViewModel viewModel;
  private final GenerateGroceryListController controller;

  private final DefaultTableModel tableModel;
  private final JLabel messageLabel = new JLabel(EMPTY_MESSAGE, SwingConstants.CENTER);

  /**
   * Creates the grocery list screen. The table stays empty until the user
   * generates a list.
   *
   * @param viewModel  where the screen's state comes from
   * @param controller called when the user asks for a grocery list
   */
  public GroceryListView(GroceryListViewModel viewModel,
                         GenerateGroceryListController controller) {
    this.viewModel = viewModel;
    this.controller = controller;

    tableModel = new DefaultTableModel(new Object[] {"Ingredient", "Measures"}, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    layOutComponents();
    viewModel.addListener(this);
  }

  /**
   * Shows the current list and, if the last request failed, its error.
   */
  @Override
  public void stateChanged() {
    final GroceryListState state = viewModel.getState();
    tableModel.setRowCount(0);
    for (GroceryListItem item : state.getItems()) {
      tableModel.addRow(new Object[] {item.getIngredientName(), item.getMeasures()});
    }

    if (state.getErrorMessage() != null) {
      messageLabel.setForeground(ERROR_COLOR);
      messageLabel.setText(state.getErrorMessage());
    } else {
      messageLabel.setForeground(null);
      messageLabel.setText(state.getItems().isEmpty() ? EMPTY_MESSAGE : itemCount(state));
    }
  }

  /**
   * Returns the name of this view in the card layout.
   *
   * @return the view name
   */
  public String getViewName() {
    return GroceryListViewModel.VIEW_NAME;
  }

  private static String itemCount(GroceryListState state) {
    final int count = state.getItems().size();
    return count == 1 ? "1 item" : count + " items";
  }

  private void layOutComponents() {
    final JLabel titleLabel = new JLabel(TITLE, SwingConstants.CENTER);
    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

    final JButton generateButton = new JButton("Generate grocery list");
    generateButton.addActionListener(event -> controller.execute());
    final JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonRow.add(generateButton);

    final JPanel top = new JPanel(new BorderLayout());
    top.add(titleLabel, BorderLayout.NORTH);
    top.add(buttonRow, BorderLayout.SOUTH);

    final JTable table = new JTable(tableModel);
    table.setRowHeight(24);
    table.getTableHeader().setReorderingAllowed(false);

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    add(top, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);
    add(messageLabel, BorderLayout.SOUTH);
  }
}
