package tornadofx.control.skin;

import com.sun.javafx.scene.control.skin.TableRowSkin;
import javafx.scene.Node;
import javafx.scene.control.TableRow;
import tornadofx.control.TableRowExpanderColumn;

/**
 * This skin is installed when you assign a {@link TableRowExpanderColumn} to a TableView.
 * The skin will render the expanded node produced by the {@link TableRowExpanderColumn#expandedNodeCallback} whenever
 * the expanded state is changed to true for a certain row.
 *
 * @param <S> The type of items in the TableRow
 */
public class ExpandableTableRowSkin<S> extends TableRowSkin<S> {
    private final TableRow<S> tableRow;
    private TableRowExpanderColumn<S> expander;
    private Double tableRowPrefHeight = -1D;

    /**
     * Create the ExpandableTableRowSkin and listen to changes for the item this table row represents. When the
     * item is changed, the old expanded node, if any, is removed from the children list of the TableRow.
     *
     * @param tableRow The table row to apply this skin for
     * @param expander The expander column, used to retrieve the expanded node when this row is expanded
     */
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

    /**
     * Create the expanded content node that should represent the current table row.
     *
     * If the expanded content node is not currently in the children list of the TableRow it is automatically added.
     *
     * @return The expanded content Node
     */
    private Node getContent() {
        Node node = expander.getOrCreateExpandedNode(tableRow);
        if (!getChildren().contains(node)) getChildren().add(node);
        return node;
    }

    /**
     * Check if the current node is expanded. This is done by checking that there is an item for the current row,
     * and that the expanded property for the row is true.
     *
     * @return A boolean indicating the expanded state of this row
     */
    private Boolean isExpanded() {
        return getSkinnable().getItem() != null && expander.getCellData(getSkinnable().getIndex());
    }

    /**
     * Add the preferred height of the expanded Node whenever the expanded flag is true.
     *
     * @return The preferred height of the TableRow, appended with the preferred height of the expanded node
     * if this row is currently expanded.
     */
    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        tableRowPrefHeight = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        return isExpanded() ? tableRowPrefHeight + getContent().prefHeight(width) : tableRowPrefHeight;
    }

    /**
     * Lay out the columns of the TableRow, then add the expanded content node below if this row is currently expanded.
     */
    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        if (isExpanded()) getContent().resizeRelocate(0.0, tableRowPrefHeight, w, h - tableRowPrefHeight);
    }
}
