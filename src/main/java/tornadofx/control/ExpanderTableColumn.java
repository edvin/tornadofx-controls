package tornadofx.control;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ExpanderTableColumn<S> extends TableColumn<S, Boolean> {
    private static final String STYLE_CLASS = "expander-column";
    private static final String EXPANDER_BUTTON_STYLE_CLASS = "expander-button";

    private final Map<S, SimpleBooleanProperty> expansionState = new HashMap<>();
    private BiFunction<TableCell<S, Boolean>, Boolean, Node> toggleNodeProvider;

    public ExpanderTableColumn() {
        getStyleClass().add(STYLE_CLASS);

        // The value of this column is the expanded state of the current row
        setCellValueFactory(param -> {
            if (param.getValue() == null) return null;
            return expansionState.putIfAbsent(param.getValue(), new SimpleBooleanProperty(false));
        });

        // Inject the toggleNode as the cell's graphic property
        setCellFactory(param -> new TableCell<S, Boolean>() {
            @Override
            protected void updateItem(Boolean expanded, boolean empty) {
                super.updateItem(expanded, empty);
                if (expanded == null || empty) {
                    setGraphic(null);
                } else {
                    Node toggleNode = getToggleNodeProvider().apply(this, expanded);
                    if (toggleNode instanceof ButtonBase) ((ButtonBase) toggleNode).setOnAction(event -> toggleExpanded(getIndex()));
                    else toggleNode.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> toggleExpanded(getIndex()));
                    setGraphic(toggleNode);
                }
            }
        });
    }

    public void toggleExpanded(int index) {
        SimpleBooleanProperty expanded = (SimpleBooleanProperty) getCellObservableValue(index);
        expanded.setValue(!expanded.getValue());
        getTableView().refresh();
    }

    public BiFunction<TableCell<S, Boolean>, Boolean, Node> getToggleNodeProvider() {
        if (toggleNodeProvider == null) toggleNodeProvider = defaultToggleNodeProvider();
        return toggleNodeProvider;
    }

    public void setToggleNodeProvider(BiFunction<TableCell<S, Boolean>, Boolean, Node> toggleNodeProvider) {
        this.toggleNodeProvider = toggleNodeProvider;
    }

    private BiFunction<TableCell<S, Boolean>, Boolean, Node> defaultToggleNodeProvider() {
        return (TableCell<S, Boolean> cell, Boolean expanded) -> {
            Button button = new Button(expanded ? "-" : "+");
            button.getStyleClass().add(EXPANDER_BUTTON_STYLE_CLASS);
            button.setPrefSize(16, 16);
            button.setPadding(new Insets(0));
            return button;
        };
    }

}
