package tornadofx.control;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.stream.Stream;

import static javafx.scene.layout.Priority.SOMETIMES;

@SuppressWarnings("unused")
@DefaultProperty("children")
public class Fieldset extends VBox {
	private SimpleStringProperty text = new SimpleStringProperty();
	private ObjectProperty<Priority> inputGrow = new SimpleObjectProperty<>(SOMETIMES);

	public Fieldset(String text) {
		this();
		setText(text);
	}

	public Field field(String text, Node... inputs) {
		Field field = new Field(text, inputs);
		getChildren().add(field);
		return field;
	}

	public Field field() {
		Field field = new Field();
		getChildren().add(field);
		return field;
	}

	public Field field(Node... inputs) {
		Field field = new Field(null, inputs);
		getChildren().add(field);
		return field;
	}

	public Fieldset() {
		getStyleClass().add("fieldset");

		// Add legend label when text is populated
		textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) addLegend();
		});

		// Make sure input children gets the configured HBox.hgrow property
		getChildren().addListener((ListChangeListener<Node>) c -> {
			while (c.next()) {
				if (c.wasAdded()) {
					c.getAddedSubList().stream().filter(added -> added instanceof Field).forEach(added -> {
						Field field = (Field) added;

						// Configure hgrow for current children
						for (Node input : field.getInputContainer().getChildren())
							configureHgrow(input);

						// Add listener to support inputs added later
						field.getInputContainer().getChildren().addListener((ListChangeListener<Node>) c1 -> {
							while (c1.next()) if (c1.wasAdded()) c1.getAddedSubList().forEach(this::configureHgrow);
						});
					});
				}
			}
		});

		// Change HGrow for unconfigured children when inputGrow changes
		inputGrowProperty().addListener((observable, oldValue, newValue) -> {
			getChildren().stream().filter(c -> c instanceof Field).forEach(c -> {
				Field field = (Field) c;
				field.getInputContainer().getChildren().forEach(this::configureHgrow);
			});
		});
	}

	private void addLegend() {
		Label legend = new Label();
		legend.textProperty().bind(textProperty());
		legend.getStyleClass().add("legend");
		getChildren().add(0, legend);
	}

	private void configureHgrow(Node input) {
		HBox.setHgrow(input, getInputGrow());
	}

	public Form getForm() {
		return (Form) getParent();
	}

	Stream<Field> getFields() {
		return getChildren().stream()
			.filter(c -> c instanceof Field)
			.map(c -> (Field) c);
	}

	public Priority getInputGrow() {
		return inputGrow.get();
	}

	public ObjectProperty<Priority> inputGrowProperty() {
		return inputGrow;
	}

	public void setInputGrow(Priority inputGrow) {
		this.inputGrow.set(inputGrow);
	}

	public String getText() {
		return text.get();
	}

	public SimpleStringProperty textProperty() {
		return text;
	}

	public void setText(String text) {
		this.text.set(text);
	}

}