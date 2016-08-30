package tornadofx.control.skin;

import com.sun.javafx.scene.control.skin.TableRowSkin;
import javafx.scene.Node;
import javafx.scene.control.TableRow;
import tornadofx.control.TableRowExpanderColumn;

public class ExpandableTableRowSkin<S> extends TableRowSkin<S> {
    private final TableRow<S> tableRow;
    private TableRowExpanderColumn<S> expander;
    private Double tableRowPrefHeight = -1D;

    public ExpandableTableRowSkin(TableRow<S> tableRow, TableRowExpanderColumn<S> expander) {
        super(tableRow);
        this.tableRow = tableRow;
        this.expander = expander;
        tableRow.itemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                Node expandedNode = this.expander.getExpandedNode(oldValue);
                if (expandedNode != null) getChildren().remove(expandedNode);
            }
        });
    }

    private Node getContent() {
        Node node = expander.getOrCreateExpandedNode(tableRow);
        if (!getChildren().contains(node)) getChildren().add(node);
        return node;
    }

    public Boolean isExpanded() {
        return getSkinnable().getItem() != null && expander.getCellData(getSkinnable().getIndex());
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
