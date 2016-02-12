package tornadofx;

import javafx.scene.layout.VBox;
import tornadofx.control.Field;
import tornadofx.control.Form;

import java.util.stream.Stream;

public class Fieldset extends VBox {

    public Form getForm() {
        return (Form) getParent();
    }

    public Stream<Field> getFields() {
        return getChildren().stream()
                .filter(c -> c instanceof Field)
                .map(c -> (Field) c);
    }
}
