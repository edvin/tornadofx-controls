package tornadofx.control;

import javafx.beans.DefaultProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.Region;

@DefaultProperty("inputs")
public class ButtonBarField extends AbstractField {

    private ButtonBar inputContainer;

    public ButtonBarField(){
        this( null, false );
    }

    public ButtonBarField(String buttonOrder, boolean forceLabelIndent ){
        super( null, forceLabelIndent );
        inputContainer = new ButtonBar( buttonOrder );
        inputContainer.getStyleClass().add("input-container");
        getChildren().add(inputContainer);
    }

    @Override
    public Region getInputContainer() {
        return inputContainer;
    }

    @Override
    public ObservableList<Node> getInputs() {
        return inputContainer.getButtons();
    }
}
