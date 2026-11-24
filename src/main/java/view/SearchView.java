package view;

import interface_adapter.search.SearchController;
import interface_adapter.search.SearchMode;
import interface_adapter.search.SearchResultItem;
import interface_adapter.search.SearchState;
import interface_adapter.search.SearchViewModel;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Consumer;

/**
 * The search window for the "Search Recipes" use case.
 *
 * Layout:
 *   [ < Back ]            Search Recipes
 *   [ Name v ] [ search field.................. ] [ Search ]
 *   +-----------------------------------------------------+
 *   |  Recipe name 1                                      |
 *   |  Recipe name 2   (click one to open its details)    |
 *   +-----------------------------------------------------+
 *   status message
 */
public class SearchView extends JPanel implements PropertyChangeListener {

    private final SearchViewModel viewModel;
    private SearchController searchController;

    // Navigation hooks. Replace with ViewManagerModel calls once the main menu and
    // recipe details views exist.
    private Runnable onBack = () -> { };
    private Consumer<SearchResultItem> onRecipeSelected = item -> { };

    private final JButton backButton = new JButton("\u2190 Back");
    private final JComboBox<SearchMode> modeComboBox = new JComboBox<>(SearchMode.values());
    private final JTextField searchField = new JTextField(25);
    private final JButton searchButton = new JButton("Search");
    private final DefaultListModel<SearchResultItem> listModel = new DefaultListModel<>();
    private final JList<SearchResultItem> resultList = new JList<>(listModel);
    private final JLabel statusLabel = new JLabel(" ");

    public SearchView(SearchViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildResultsPanel(), BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        wireListeners();
    }

    // ---------- layout ----------

    private JPanel buildTopPanel() {
        // Row 1: back button on the left, title centred
        JLabel title = new JLabel(SearchViewModel.TITLE_LABEL, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.add(backButton, BorderLayout.WEST);
        titleRow.add(title, BorderLayout.CENTER);
        // invisible spacer the same width as the back button keeps the title truly centred
        Component spacer = javax.swing.Box.createRigidArea(backButton.getPreferredSize());
        titleRow.add(spacer, BorderLayout.EAST);

        // Row 2: dropdown + search field + button
        JPanel searchRow = new JPanel(new BorderLayout(8, 0));
        JPanel left = new JPanel(new BorderLayout(6, 0));
        left.add(new JLabel("Search by:"), BorderLayout.WEST);
        left.add(modeComboBox, BorderLayout.CENTER);
        searchRow.add(left, BorderLayout.WEST);
        searchRow.add(searchField, BorderLayout.CENTER);
        searchRow.add(searchButton, BorderLayout.EAST);

        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.add(titleRow, BorderLayout.NORTH);
        top.add(searchRow, BorderLayout.SOUTH);
        return top;
    }

    private JScrollPane buildResultsPanel() {
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultList.setFixedCellHeight(36);
        resultList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SearchResultItem) {
                    setText(((SearchResultItem) value).getName());
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(resultList);
        scrollPane.setPreferredSize(new Dimension(520, 360));
        return scrollPane;
    }

    // ---------- behaviour ----------

    private void wireListeners() {
        backButton.addActionListener(e -> onBack.run());

        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch()); // Enter key in the field

        // Click a recipe to open it. We check the click landed on a row, not on empty
        // space below the last row (locationToIndex alone would return the last row).
        resultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = resultList.locationToIndex(e.getPoint());
                if (index >= 0 && resultList.getCellBounds(index, index).contains(e.getPoint())) {
                    openRecipe(listModel.getElementAt(index));
                }
            }
        });
        // Keyboard: Enter opens the selected recipe
        resultList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && resultList.getSelectedValue() != null) {
                    openRecipe(resultList.getSelectedValue());
                }
            }
        });
        // Hand cursor over rows to hint they are clickable
        resultList.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = resultList.locationToIndex(e.getPoint());
                boolean overRow = index >= 0 && resultList.getCellBounds(index, index).contains(e.getPoint());
                resultList.setCursor(Cursor.getPredefinedCursor(overRow ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void performSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            statusLabel.setText("Please enter something to search for.");
            return;
        }
        if (searchController == null) {
            statusLabel.setText("Search is not available yet.");
            return;
        }
        statusLabel.setText("Searching...");
        searchController.execute(query, (SearchMode) modeComboBox.getSelectedItem());
    }

    private void openRecipe(SearchResultItem item) {
        onRecipeSelected.accept(item);
    }

    /** Called by the view model whenever the presenter publishes a new state. */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (SearchViewModel.STATE_PROPERTY.equals(evt.getPropertyName())) {
            render(viewModel.getState());
        }
    }

    private void render(SearchState state) {
        listModel.clear();
        for (SearchResultItem item : state.getResults()) {
            listModel.addElement(item);
        }
        String message = state.getMessage();
        statusLabel.setText(message == null || message.isEmpty() ? " " : message);
    }

    // ---------- wiring (called from AppBuilder) ----------

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    /** What to do when Back is pressed (e.g. switch to the main menu view). */
    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    /** What to do when a recipe is clicked (e.g. open the recipe details view). */
    public void setOnRecipeSelected(Consumer<SearchResultItem> onRecipeSelected) {
        this.onRecipeSelected = onRecipeSelected;
    }

    public String getViewName() {
        return SearchViewModel.VIEW_NAME;
    }
}
