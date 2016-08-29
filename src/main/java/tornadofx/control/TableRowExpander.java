package tornadofx.control;

import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import tornadofx.control.skin.ExpandableTableRowSkin;

import java.util.function.BiFunction;

public class TableRowExpander<S> {
    private final ExpanderTableColumn<S> expanderColumn;
    private TableView<S> tableView;

    public TableRowExpander(TableView<S> tableView, BiFunction<RowExpanderPane, S, Node> expandedNodeBuilder) {
        this.tableView = tableView;
        expanderColumn = new ExpanderTableColumn<>();
        tableView.getColumns().add(0, expanderColumn);
        tableView.setRowFactory(param -> new TableRow<S>() {
            @Override
            protected Skin<?> createDefaultSkin() {
                return new ExpandableTableRowSkin<>(this, expandedNodeBuilder, expanderColumn);
            }
        });
    }

    public ExpanderTableColumn<S> getExpanderColumn() {
        return this.expanderColumn;
    }
}
