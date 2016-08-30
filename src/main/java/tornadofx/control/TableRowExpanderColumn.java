package tornadofx.control;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import tornadofx.control.skin.ExpandableTableRowSkin;

import java.util.HashMap;
import java.util.Map;

/**
 * The TableColumn can be addded to a TableView to provide an expansion toggle button. Clicking the button
 * will expand the area below the given row and display a custom editor for that row. Example:
 *
 * <pre>
 * new TableRowExpanderColumn&lt;>(tableView, param -> {
 *     HBox editor = new HBox(10);
 *     TextField text = new TextField(param.getValue().getUsername());
 *     Button save = new Button("Save");
 *     save.setOnAction(event -> {
 *         save();
 *         param.toggleExpanded();
 *     });
 *     editor.getChildren().addAll(text, save);
 *     return editor;
 * });
 * </pre>
 *
 * You can insert the expander column manually at any position as well:
 *
 * <pre>
 *     TableRowExpanderColumn expander = new TableRowExpanderColumn&lt;Customer>(param -> {
 *          // Create and return editor here
 *     });
 *
 *     tableView.getColumns().add(expander);
 * </pre>
 *
 * You can provide a custom cellFactory to customize the toggle button. A typical custom toggle cell implementation
 * would look like this:
 *
 * <pre>
 * public class MyCustomToggleCell&lt;S> extends TableCell&lt;S, Boolean> {
 *     private Button button = new Button();
 *
 *     public MyCustomToggleCell(TableRowExpanderColumn&lt;S> column) {
 *         button.setOnAction(event -> column.toggleExpanded(getIndex()));
 *     }
 *
 *     protected void updateItem(Boolean expanded, boolean empty) {
 *         super.updateItem(expanded, empty);
 *         if (expanded == null || empty) {
 *             setGraphic(null);
 *         } else {
 *             button.setText(expanded ? "Collapse" : "Expand");
 *             setGraphic(button);
 *         }
 *     }
 * }
 * </pre>
 *
 * The custom toggle cell utilizes the {@link TableRowExpanderColumn#toggleExpanded(int)} method to toggle
 * the row expander.
 *
 * @param <S> The item type of the TableView
 */
public class TableRowExpanderColumn<S> extends TableColumn<S, Boolean> {
    private static final String STYLE_CLASS = "expander-column";
    private static final String EXPANDER_BUTTON_STYLE_CLASS = "expander-button";

    private final Map<S, Node> expandedNodeCache = new HashMap<>();
    private final Map<S, SimpleBooleanProperty> expansionState = new HashMap<>();
    private Callback<TableRowDataFeatures<S>, Node> expandedNodeCallback;

    /**
     * Returns a Boolean property that can be used to manipulate the expanded state for a row
     * corresponding to the given item value.
     *
     * @param item The item corresponding to a table row
     * @return The boolean property
     */
    public SimpleBooleanProperty getExpandedProperty(S item) {
        SimpleBooleanProperty value = expansionState.get(item);
        if (value == null) {
            value = new SimpleBooleanProperty(item, "expanded", false) {
                /**
                 * When the expanded state change we refresh the tableview.
                 * If the expanded state changes to false we remove the cached expanded node.
                 */
                @Override
                protected void invalidated() {
                    getTableView().refresh();
                    if (!getValue()) expandedNodeCache.remove(getBean());
                }
            };
            expansionState.put(item, value);
        }
        return value;
    }

    /**
     * Get or create and cache the expanded node for a given item.
     *
     * @param tableRow The table row, used to find the item index
     * @return The expanded node for the given item
     */
    public Node getOrCreateExpandedNode(TableRow<S> tableRow) {
        int index = tableRow.getIndex();
        if (index > -1 && index < getTableView().getItems().size()) {
            S item = getTableView().getItems().get(index);
            Node node = expandedNodeCache.get(item);
            if (node == null) {
                node = expandedNodeCallback.call(new TableRowDataFeatures<>(tableRow, this, item));
                expandedNodeCache.put(item, node);
            }
            return node;
        }
        return null;
    }

    /**
     * Return the expanded node for the given item, if it exists.
     *
     * @param item The item corresponding to a table row
     * @return The expanded node, if it exists.
     */
    public Node getExpandedNode(S item) {
        return expandedNodeCache.get(item);
    }

    public TableRowExpanderColumn(TableView<S> tableView, Callback<TableRowDataFeatures<S>, Node> expandedNodeCallback) {
        this(expandedNodeCallback);
        tableView.getColumns().add(this);
    }

    public TableRowExpanderColumn(Callback<TableRowDataFeatures<S>, Node> expandedNodeCallback) {
        this.expandedNodeCallback = expandedNodeCallback;

        getStyleClass().add(STYLE_CLASS);
        setCellValueFactory(param -> getExpandedProperty(param.getValue()));
        setCellFactory(param -> new ToggleCell());
        installRowFactoryOnTableViewAssignment();
    }

    /**
     * Install the row factory on the TableView when this column is assigned to a TableView
     */
    private void installRowFactoryOnTableViewAssignment() {
        tableViewProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                getTableView().setRowFactory(param -> new TableRow<S>() {
                    @Override
                    protected Skin<?> createDefaultSkin() {
                        return new ExpandableTableRowSkin<>(this, TableRowExpanderColumn.this);
                    }
                });
            }
        });
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

    public static class TableRowDataFeatures<S> {
        private TableRow<S> tableRow;
        private TableRowExpanderColumn<S> tableColumn;
        private SimpleBooleanProperty expandedProperty;
        private S value;

        public TableRowDataFeatures(TableRow<S> tableRow, TableRowExpanderColumn<S> tableColumn, S value) {
            this.tableRow = tableRow;
            this.tableColumn = tableColumn;
            this.expandedProperty = (SimpleBooleanProperty) tableColumn.getCellObservableValue(tableRow.getIndex());
            this.value = value;
        }

        public TableRow<S> getTableRow() {
            return tableRow;
        }

        public TableRowExpanderColumn<S> getTableColumn() {
            return tableColumn;
        }

        public SimpleBooleanProperty expandedProperty() {
            return expandedProperty;
        }

        public void toggleExpanded() {
            SimpleBooleanProperty expanded = expandedProperty();
            expanded.setValue(!expanded.getValue());
        }

        public Boolean isExpanded() {
            return expandedProperty().getValue();
        }

        public void setExpanded(Boolean expanded) {
            expandedProperty().setValue(expanded);
        }

        public S getValue() {
            return value;
        }

    }

}
