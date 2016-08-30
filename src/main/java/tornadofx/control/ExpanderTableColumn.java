package tornadofx.control;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * The expander table column will be inserted as the first column in a TableView when you call
 * {@link TableRowExpander#install(TableView, Callback)} to configure a row expander for the given
 * TableView. The expander column contains the default button used to toggle expansion state for the row.
 *
 * You can provide a custom cellFactory to customize the toggle button.
 *
 * @param <S> The item type of the TableView
 */
public class ExpanderTableColumn<S> extends TableColumn<S, Boolean> {
    private static final String STYLE_CLASS = "expander-column";
    private static final String EXPANDER_BUTTON_STYLE_CLASS = "expander-button";

    public ExpanderTableColumn(TableRowExpander<S> expander) {
        getStyleClass().add(STYLE_CLASS);
        setCellValueFactory(param -> expander.getExpandedProperty(param.getValue()));
        setCellFactory(param -> new ToggleCell());
    }

    private class ToggleCell extends TableCell<S, Boolean> {
        private Button button = new Button();

        public ToggleCell() {
            button.setFocusTraversable(false);
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

    /**
     * Toggle the expanded state of the row at the given index.
     *
     * @param index The index of the row you want to toggle expansion for.
     */
    public void toggleExpanded(int index) {
        SimpleBooleanProperty expanded = (SimpleBooleanProperty) getCellObservableValue(index);
        expanded.setValue(!expanded.getValue());
    }

}
