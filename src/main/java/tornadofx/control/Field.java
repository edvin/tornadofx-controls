package tornadofx.control;

import javafx.beans.DefaultProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.HBox;
import tornadofx.Fieldset;
import tornadofx.control.skin.FieldSkin;

@DefaultProperty("inputs")
public class Field extends Control {
    private LabelContainer labelContainer = new LabelContainer();
    private InputContainer inputContainer = new InputContainer();
    private ObservableList<Node> inputs;

    public Field() {
        getStyleClass().add("field");
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
        public LabelContainer() {
            getStyleClass().add("label-container");
        }
    }

    public class InputContainer extends HBox {
        public InputContainer() {
            getStyleClass().add("input-container");
        }
    }

    public LabelContainer getLabelContainer() {
        return labelContainer;
    }

    public InputContainer getInputContainer() {
        return inputContainer;
    }

    public ObservableList<Node> getInputs() {
        return inputs;
    }
}
