package tornadofx.control;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TableRow;
import javafx.scene.layout.StackPane;

public class RowExpanderPane extends StackPane {
    private static final String STYLE_CLASS = "expander-pane";
    private final TableRow tableRow;
    private final ExpanderTableColumn expanderColumn;

    public RowExpanderPane(TableRow tableRow, ExpanderTableColumn expanderColumn) {
        getStyleClass().add(STYLE_CLASS);
        this.tableRow = tableRow;
        this.expanderColumn = expanderColumn;
    }

    public void toggleExpanded() {
        expanderColumn.toggleExpanded(tableRow.getIndex());
    }

    public SimpleBooleanProperty expandedProperty() {
        return (SimpleBooleanProperty) expanderColumn.getCellObservableValue(tableRow.getIndex());
    }

    public Boolean isExpanded() {
        return expandedProperty().getValue();
    }

    public void setExpanded(Boolean expanded) {
        expandedProperty().setValue(expanded);
    }
}
