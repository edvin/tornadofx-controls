package tornadofx.control;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.function.Function;

import static javafx.beans.binding.Bindings.createStringBinding;

public class NaviSelect<T> extends HBox {
	private Button editButton = new Button();
	private TextField visual = new TextField();
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
		visual.getStyleClass().add("visual");

        visual.textProperty().bind(visualBinding);
        HBox.setHgrow(visual, Priority.ALWAYS);

		Pane editButtonGraphic = new Pane();
		editButtonGraphic.getStyleClass().add("icon");
		editButton.setGraphic(editButtonGraphic);
		editButton.setTooltip(new Tooltip("Edit"));

		Pane gotoButtonGraphic = new Pane();
		gotoButtonGraphic.getStyleClass().add("icon");
		gotoButton.setGraphic(gotoButtonGraphic);
		gotoButton.setTooltip(new Tooltip("Goto"));

		editButton.getStyleClass().add("edit-button");
		gotoButton.getStyleClass().add("goto-button");

		getChildren().addAll(visual, editButton, gotoButton);
	}

	public void setOnEdit(EventHandler<ActionEvent> editHandler) {
		editButton.setOnAction(editHandler);
	}

	public void setOnGoto(EventHandler<ActionEvent> gotoHandler) {
		gotoButton.setOnAction(gotoHandler);
	}

	public ObjectProperty<T> valueProperty() { return value; }
	public T getValue() { return value.get(); }
	public void setValue(T value) { this.value.set(value); }

	public TextField getVisual() {
		return visual;
	}

	public void setVisual(TextField visual) {
		this.visual = visual;
	}


	public Button getEditButton() {
		return editButton;
	}

	public void setEditButton(Button editButton) {
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
