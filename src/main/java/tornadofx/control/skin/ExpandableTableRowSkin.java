package tornadofx.control.skin;

import com.sun.javafx.scene.control.skin.TableRowSkin;
import javafx.scene.Node;
import javafx.scene.control.TableRow;
import tornadofx.control.ExpanderTableColumn;
import tornadofx.control.RowExpanderPane;

import java.util.function.BiFunction;

public class ExpandableTableRowSkin<S> extends TableRowSkin<S> {
    private final TableRow<S> tableRow;
    private final BiFunction<RowExpanderPane, S, Node> expandedNodeBuilder;
    private final ExpanderTableColumn<S> expanderColumn;
    private RowExpanderPane rowExpanderPane;
    private Double tableRowPrefHeight = -1D;

    public ExpandableTableRowSkin(TableRow<S> tableRow, BiFunction<RowExpanderPane, S, Node> expandedNodeBuilder, ExpanderTableColumn<S> expanderColumn) {
        super(tableRow);
        this.tableRow = tableRow;
        this.expandedNodeBuilder = expandedNodeBuilder;
        this.expanderColumn = expanderColumn;
    }

    private RowExpanderPane getRowExpanderPane() {
        if (rowExpanderPane == null) {
            rowExpanderPane = new RowExpanderPane(tableRow, expanderColumn);
            Node content = expandedNodeBuilder.apply(rowExpanderPane, getSkinnable().getItem());
            rowExpanderPane.getChildren().add(content);
            getChildren().add(rowExpanderPane);
        }
        return rowExpanderPane;
    }

    public Boolean isExpanded() {
        return getSkinnable().getItem() != null && expanderColumn.getCellData(getSkinnable().getIndex());
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        tableRowPrefHeight = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        return isExpanded() ? tableRowPrefHeight + getRowExpanderPane().prefHeight(width) : tableRowPrefHeight;
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        if (isExpanded()) getRowExpanderPane().resizeRelocate(0.0, tableRowPrefHeight, w, h - tableRowPrefHeight);
    }
}
