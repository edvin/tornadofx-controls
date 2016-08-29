package tornadofx.control;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

import java.util.HashMap;
import java.util.Map;

public class ExpanderTableColumn<S> extends TableColumn<S, Boolean> {
    private static final String STYLE_CLASS = "expander-column";
    private static final String EXPANDER_BUTTON_STYLE_CLASS = "expander-button";

    private final Map<S, SimpleBooleanProperty> expansionState = new HashMap<>();

    public ExpanderTableColumn() {
        getStyleClass().add(STYLE_CLASS);

        // The value of this column is the expanded state of the current row
        setCellValueFactory(param -> {
            if (param.getValue() == null) return null;
            return expansionState.putIfAbsent(param.getValue(), new SimpleBooleanProperty(false));
        });

        setCellFactory(param -> new ToggleCell());
    }

    private class ToggleCell extends TableCell<S, Boolean> {
        private Button button = new Button();

        public ToggleCell() {
            button.getStyleClass().add(EXPANDER_BUTTON_STYLE_CLASS);
            button.setPrefSize(16, 16);
            button.setPadding(new Insets(0));
            button.setOnAction(event -> toggleExpanded(getIndex()));
        }

        @Override
        protected void updateItem(Boolean expanded, boolean empty) {
            super.updateItem(expanded, empty);
            if (expanded == null || empty) {
                setGraphic(null);
            } else {
                button.setText(expanded ? "-" : "+");
                setGraphic(button);
            }
        }
    }

    public void toggleExpanded(int index) {
        SimpleBooleanProperty expanded = (SimpleBooleanProperty) getCellObservableValue(index);
        expanded.setValue(!expanded.getValue());
        getTableView().refresh();
    }

}
