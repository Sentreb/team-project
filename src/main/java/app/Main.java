package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Starts the SOLID Meals app.
 */
public class Main {

    /**
     * Builds the app on the Swing thread and shows its window.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppBuilder appBuilder = new AppBuilder();
            JFrame application = appBuilder
                    .addHomeView()
                    .addSearchView()
                    .addMealPlanView()
                    .addGroceryListView()
                    .build();
            application.setVisible(true);
        });
    }
}