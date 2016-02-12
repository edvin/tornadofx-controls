package tornadofx.control;

import javafx.scene.layout.VBox;
import tornadofx.Fieldset;

import java.util.stream.Stream;

public class Form extends VBox {

    public double getLabelContainerWidth() {
        return getFieldsets().flatMap(Fieldset::getFields)
                .map(Field::getInputContainer)
                .mapToDouble(Field.InputContainer::getPrefWidth)
                .max()
                .orElse(0);
    }

    public Stream<Fieldset> getFieldsets() {
        return getChildren().stream()
                .filter(c -> c instanceof Fieldset)
                .map(c -> (Fieldset) c);
    }
}
