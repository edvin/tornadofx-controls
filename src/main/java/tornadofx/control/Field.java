package tornadofx.control;

import javafx.beans.DefaultProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.HBox;
import tornadofx.control.skin.FieldSkin;

@DefaultProperty("inputs")
public class Field extends Control {
	private SimpleStringProperty textProperty = new SimpleStringProperty();
	private Label label = new Label();
	private LabelContainer labelContainer = new LabelContainer(label);
    private InputContainer inputContainer = new InputContainer();
    private ObservableList<Node> inputs;

	public LabelContainer getLabelContainer() {
		return labelContainer;
	}

	public Field(String text, Node... inputs) {
		this();
		setText(text);
		if (inputs != null) getInputContainer().getChildren().addAll(inputs);
	}

    public Field() {
        setFocusTraversable(false);
        getStyleClass().add("field");
	    label.textProperty().bind(textProperty);
        inputs = inputContainer.getChildren();
        getChildren().addAll(labelContainer, inputContainer);
    }

    public Fieldset getFieldset() {
        return (Fieldset) getParent();
    }

    protected Skin<?> createDefaultSkin() {
        return new FieldSkin(this);
    }

    public class LabelContainer extends HBox {
        public LabelContainer(Label label) {
	        getChildren().add(label);
	        getStyleClass().add("label-container");
        }
    }

    public class InputContainer extends HBox {
        public InputContainer() {
            getStyleClass().add("input-container");
        }
    }

    public InputContainer getInputContainer() {
        return inputContainer;
    }

    public ObservableList<Node> getInputs() {
        return inputs;
    }

    public void setInputs(ObservableList<Node> inputs) {
        this.inputs = inputs;
    }

    public String getText() {
		return textProperty.get();
	}

	public SimpleStringProperty textProperty() {
		return textProperty;
	}

	public void setText(String text) {
		this.textProperty.set(text);
	}
}
