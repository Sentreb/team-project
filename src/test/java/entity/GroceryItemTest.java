package entity;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GroceryItemTest {

    @Test
    void newItemIsUnchecked() {
        GroceryItem item = new GroceryItem("Onion", List.of("1"));

        boolean checked = item.isChecked();

        assertFalse(checked);
    }

    @Test
    void addMeasureAppendsInOrder() {
        GroceryItem item = new GroceryItem("Rice", List.of());

        item.addMeasure("1 cup");
        item.addMeasure("2 cup");

        List<String> measures = item.getMeasures();

        assertEquals(List.of("1 cup", "2 cup"), measures);
    }

    @Test
    void toggleTwiceReturnsToUnchecked() {
        GroceryItem item = new GroceryItem("Butter", List.of("1 cup"));

        item.toggleChecked();

        assertTrue(item.isChecked());

        item.toggleChecked();

        assertFalse(item.isChecked());
    }

    @Test
    void threeArgConstructorKeepsChecked() {
        GroceryItem item = new GroceryItem("Onion", List.of("1 piece"), true);

        boolean checked = item.isChecked();

        assertTrue(checked);
    }

    @Test
    void blankMeasuresAreSkipped() {
        GroceryItem item = new GroceryItem("Onion", List.of("", "  ", "1 piece"));

        item.addMeasure(null);

        List<String> measures = item.getMeasures();

        assertEquals(List.of("1 piece"), measures);
    }

    @Test
    void changingOriginalListDoesNotAffectItem() {
        List<String> modifiableMeasures = new ArrayList<>(List.of("1"));

        GroceryItem item = new GroceryItem("Onion", modifiableMeasures);

        modifiableMeasures.add("2");

        List<String> measures = item.getMeasures();

        assertEquals(List.of("1"), measures);
    }

    @Test
    void getMeasuresCannotBeModified() {
        GroceryItem item = new GroceryItem("Onion", List.of("1 piece"));

        assertThrows(UnsupportedOperationException.class, () -> item.getMeasures().add("x"));
    }

    @Test
    void blankNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new GroceryItem("  ", List.of()));
    }

    @Test
    void nullNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new GroceryItem(null, List.of()));
    }

    @Test
    void nullMeasureListThrows() {
        assertThrows(IllegalArgumentException.class, () -> new GroceryItem("Onion", null));
    }

}
