package tornadofx.control;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.function.Function;

import static javafx.beans.binding.Bindings.createStringBinding;

public class NaviSelect<T> extends HBox {
	private ComboBox<String> editButton = new ComboBox<>();
	private Button gotoButton = new Button();
    private Function<T, String> defaultVisualConverter = t -> { String value = t == null ? null : t.toString(); return value == null ? "" : value; };
    private ObjectProperty<Function<T, String>> visualConverter = new SimpleObjectProperty<Function<T, String>>(defaultVisualConverter) {
        public void set(Function<T, String> newValue) {
            super.set(newValue);
            visualBinding.invalidate();
        }
    };
    private ObjectProperty<T> value = new SimpleObjectProperty<>();
    private StringBinding visualBinding = createStringBinding(() -> getVisualConverter().apply(getValue()), valueProperty());

    public NaviSelect() {
		getStyleClass().add("navi-select");
		editButton.getStyleClass().add("edit-button");

        editButton.valueProperty().bind(visualBinding);
        HBox.setHgrow(editButton, Priority.ALWAYS);

		editButton.setTooltip(new Tooltip("Edit"));

		Pane gotoButtonGraphic = new Pane();
		gotoButtonGraphic.getStyleClass().add("icon");
		gotoButton.setGraphic(gotoButtonGraphic);
		gotoButton.setTooltip(new Tooltip("Goto"));

		gotoButton.getStyleClass().add("goto-button");

		getChildren().addAll(editButton, gotoButton);
	}

	public void setOnEdit(EventHandler<MouseEvent> editHandler) {
		editButton.setOnMouseClicked(editHandler);
	}

	public void setOnGoto(EventHandler<ActionEvent> gotoHandler) {
		gotoButton.setOnAction(gotoHandler);
	}

	public ObjectProperty<T> valueProperty() { return value; }
	public T getValue() { return value.get(); }
	public void setValue(T value) { this.value.set(value); }

	public ComboBox<String> getEditButton() {
		return editButton;
	}

	public void setEditButton(ComboBox<String> editButton) {
		this.editButton = editButton;
	}

	public Button getGotoButton() {
		return gotoButton;
	}

	public void setGotoButton(Button gotoButton) {
		this.gotoButton = gotoButton;
	}

	public String getUserAgentStylesheet() {
		return ListMenu.class.getResource("naviselect.css").toExternalForm();
	}

    public Function<T, String> getVisualConverter() {
        return visualConverter.get();
    }

    public ObjectProperty<Function<T, String>> visualConverterProperty() {
        return visualConverter;
    }

    public void setVisualConverter(Function<T, String> visualConverter) {
        this.visualConverter.set(visualConverter);
    }

}
