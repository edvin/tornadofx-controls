package tornadofx.control;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import tornadofx.control.skin.ExpandableTableRowSkin;

import java.util.HashMap;
import java.util.Map;

/**
 * The TableRowExpander adds the ability to expand a TableRow by presenting a custom node that can be used to
 * further edit or present the data for the row.
 *
 * Example:
 *
 * <pre>
 * TableRowExpander.install(tableView, param -> {
 *     CustomEditor editor = new CustomEditor(param.getValue());
 *     Button save = new Button("Save");
 *     save.setOnAction(event -> param.toggleExpanded());
 *     editor.children.add(save);
 *     return editor;
 * });
 * </pre>
 *
 * If the node you return doesn't provide a reasonable prefHeight, you might want to configure that explicitly.
 * For example, if you embed another TableView as the row expander node, you will most likely want to constrain
 * the maximum size of the secondary table.
 *
 * You can customize the toggle button by assigning the result of the *install* call to a variable and calling
 * {@link TableRowExpander#getExpanderColumn()} to access the toggle button column. You can provide a custom cellFactory
 * and call {@link ExpanderTableColumn#toggleExpanded(int) toggleExpanded(index)} to open and close the row expander for the current table row.
 *
 * @param <S> The type of items in the TableView
 */
public class TableRowExpander<S> {
    private final ExpanderTableColumn<S> expanderColumn;
    private final Map<S, Node> expandedNodeCache = new HashMap<>();
    private final Map<S, SimpleBooleanProperty> expansionState = new HashMap<>();
    private TableView<S> tableView;
    private Callback<TableRowDataFeatures<S>, Node> expandedNodeBuilder;

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
                    tableView.refresh();
                    if (!getValue()) expandedNodeCache.remove(getBean());
                }
            };
            expansionState.put(item, value);
        }
        return value;
    }

    /**
     * Construct a new TableRowExpander and insert the expander table column at column position 0 in the TableView.
     *
     * @param tableView The TableView to install the row expander on
     * @param expandedNodeBuilder The callback used to create the custom editor node when a given row is expanded
     */
    private TableRowExpander(TableView<S> tableView, Callback<TableRowDataFeatures<S>, Node> expandedNodeBuilder) {
        this.tableView = tableView;
        this.expandedNodeBuilder = expandedNodeBuilder;
        expanderColumn = new ExpanderTableColumn<>(this);
        tableView.getColumns().add(0, expanderColumn);
        tableView.setRowFactory(param -> new TableRow<S>() {
            @Override
            protected Skin<?> createDefaultSkin() {
                return new ExpandableTableRowSkin<>(TableRowExpander.this, this, expanderColumn);
            }
        });
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

    /**
     * Return the ExpanderTableColumn. You can install a custom cellFactory to override the default
     * +/- button or further customize the column from here.
     *
     * @return The table column containing the expander button, already inserted at column position 0.
     */
    public ExpanderTableColumn<S> getExpanderColumn() {
        return this.expanderColumn;
    }

    /**
     * Get or create and cache the expanded node for a given item.
     *
     * @param tableRow The table row, used to find the item index
     * @return The expanded node for the given item
     */
    public Node getOrCreateExpandedNode(TableRow<S> tableRow) {
        int index = tableRow.getIndex();
        if (index > -1 && index < tableView.getItems().size()) {
            S item = tableView.getItems().get(index);
            Node node = expandedNodeCache.get(item);
            if (node == null) {
                node = expandedNodeBuilder.call(new TableRowDataFeatures<>(tableRow, expanderColumn, item));
                expandedNodeCache.put(item, node);
            }
            return node;
        }
        return null;
    }

    /**
     * Install a row epander on the given TableView. A column contaning a button to toggle the
     * expanded state of a row is inserted at position 0 in the TableView's columns list.
     *
     * @param tableView The TableView to add a row epxander to
     * @param expandedNodeBuilder A callback used to create the expanded node for the given item/table row
     * @param <S> The type of items in the TableView
     * @return A TableRowExpander which can be used to further customize the expander, for example by adding a custom
     * cell factory to the expanderColumn.
     */
    public static <S> TableRowExpander<S> install(TableView<S> tableView, Callback<TableRowDataFeatures<S>, Node> expandedNodeBuilder) {
        return new TableRowExpander<>(tableView, expandedNodeBuilder);
    }

    public static class TableRowDataFeatures<S> {
        private TableRow<S> tableRow;
        private ExpanderTableColumn<S> tableColumn;
        private SimpleBooleanProperty expandedProperty;
        private S value;

        public TableRowDataFeatures(TableRow<S> tableRow, ExpanderTableColumn<S> tableColumn, S value) {
            this.tableRow = tableRow;
            this.tableColumn = tableColumn;
            this.expandedProperty = (SimpleBooleanProperty) tableColumn.getCellObservableValue(tableRow.getIndex());
            this.value = value;
        }

        public TableRow<S> getTableRow() {
            return tableRow;
        }

        public ExpanderTableColumn<S> getTableColumn() {
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
