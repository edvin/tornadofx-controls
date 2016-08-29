package tornadofx.control;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import tornadofx.control.skin.ExpandableTableRowSkin;

public class TableRowExpander<S> {
    private final ExpanderTableColumn<S> expanderColumn;

    public ExpanderTableColumn<S> getExpanderColumn() {
        return this.expanderColumn;
    }

    public TableRowExpander(TableView<S> tableView, Callback<TableRowDataFeatures<S>, Node> expandedNodeBuilder) {
        expanderColumn = new ExpanderTableColumn<>();
        tableView.getColumns().add(0, expanderColumn);
        tableView.setRowFactory(param -> new TableRow<S>() {
            @Override
            protected Skin<?> createDefaultSkin() {
                return new ExpandableTableRowSkin<>(this, expandedNodeBuilder, expanderColumn);
            }
        });
    }

    public static <S> TableRowExpander<S> install(TableView<S> tableView, Callback<TableRowDataFeatures<S>, Node> expandedNodeBuilder) {
        return new TableRowExpander<>(tableView, expandedNodeBuilder);
    }

    public static class TableRowDataFeatures<S> {
        private TableRow<S> tableRow;
        private ExpanderTableColumn<S> tableColumn;
        private S value;

        public TableRowDataFeatures(TableRow<S> tableRow, ExpanderTableColumn<S> tableColumn, S value) {
            this.tableRow = tableRow;
            this.tableColumn = tableColumn;
            this.value = value;
        }

        public S getValue() {
            return value;
        }

        public ExpanderTableColumn<S> getTableColumn() {
            return tableColumn;
        }

        public TableRow<S> getTableRow() {
            return tableRow;
        }

        public SimpleBooleanProperty expandedProperty() {
            return (SimpleBooleanProperty) tableColumn.getCellObservableValue(tableRow.getIndex());
        }

        public void toggleExpanded() {
            tableColumn.toggleExpanded(tableRow.getIndex());
        }

        public Boolean isExpanded() {
            return expandedProperty().getValue();
        }

        public void setExpanded(Boolean expanded) {
            expandedProperty().setValue(expanded);
        }

    }
}
