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

public class TableRowExpander<S> {
    private final ExpanderTableColumn<S> expanderColumn;
    private final Map<S, Node> expandedNodeCache = new HashMap<>();
    private final Map<S, SimpleBooleanProperty> expansionState = new HashMap<>();
    private TableView<S> tableView;
    private Callback<TableRowDataFeatures<S>, Node> expandedNodeBuilder;

    public ExpanderTableColumn<S> getExpanderColumn() {
        return this.expanderColumn;
    }

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

    public TableRowExpander(TableView<S> tableView, Callback<TableRowDataFeatures<S>, Node> expandedNodeBuilder) {
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

    public Node getExpandedNode(S item) {
        return expandedNodeCache.get(item);
    }

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
