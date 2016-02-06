package tornadofx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class NaviSelect<T> extends HBox {
	private Button editButton = new Button();
	private TextField visual = new TextField();
	private Button gotoButton = new Button();

	private ObjectProperty<T> valueProperty = new SimpleObjectProperty<>();

	public NaviSelect() {
		setFocusTraversable(true);

		getStyleClass().add("navi-select");
		visual.setEditable(false);
		visual.getStyleClass().add("visual");
		visual.textProperty().bind(valueProperty.asString());

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

	public ObjectProperty<T> valueProperty() { return valueProperty; }
	public T getValue() { return valueProperty.get(); }
	public void setValue(T value) { this.valueProperty.set(value); }

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

}
