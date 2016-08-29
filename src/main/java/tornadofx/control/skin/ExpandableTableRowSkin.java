package tornadofx.control.skin;

import com.sun.javafx.scene.control.skin.TableRowSkin;
import javafx.scene.Node;
import javafx.scene.control.TableRow;
import javafx.util.Callback;
import tornadofx.control.ExpanderTableColumn;
import tornadofx.control.TableRowExpander.TableRowDataFeatures;

public class ExpandableTableRowSkin<S> extends TableRowSkin<S> {
    private final TableRow<S> tableRow;
    private final Callback<TableRowDataFeatures<S>, Node> expandedNodeBuilder;
    private final ExpanderTableColumn<S> expanderColumn;
    private Node content;
    private Double tableRowPrefHeight = -1D;

    public ExpandableTableRowSkin(TableRow<S> tableRow, Callback<TableRowDataFeatures<S>, Node> expandedNodeBuilder, ExpanderTableColumn<S> expanderColumn) {
        super(tableRow);
        this.tableRow = tableRow;
        this.expandedNodeBuilder = expandedNodeBuilder;
        this.expanderColumn = expanderColumn;
    }

    private Node getContent() {
        if (content == null) {
            TableRowDataFeatures<S> features = new TableRowDataFeatures<>(expanderColumn, tableRow, getSkinnable().getItem());
            content = expandedNodeBuilder.call(features);
            getChildren().add(content);
        }
        return content;
    }

    public Boolean isExpanded() {
        return getSkinnable().getItem() != null && expanderColumn.getCellData(getSkinnable().getIndex());
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        tableRowPrefHeight = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        return isExpanded() ? tableRowPrefHeight + getContent().prefHeight(width) : tableRowPrefHeight;
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        if (isExpanded()) getContent().resizeRelocate(0.0, tableRowPrefHeight, w, h - tableRowPrefHeight);
    }
}
