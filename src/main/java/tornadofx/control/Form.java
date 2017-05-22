package tornadofx.control;

import javafx.geometry.Orientation;
import javafx.scene.layout.VBox;

import java.util.stream.Stream;

public class Form extends VBox {

	public Form() {
		getStyleClass().add("form");
	}

	public double getLabelContainerWidth(Double height) {
        return getFieldsets().flatMap(fs->fs.getFields().stream())
                .map(Field::getLabelContainer)
                .mapToDouble(f -> f.prefWidth(-height))
                .max()
                .orElse(0);
    }

    public Stream<Fieldset> getFieldsets() {
        return getChildren().stream()
                .filter(c -> c instanceof Fieldset)
                .map(c -> (Fieldset) c);
    }

	public String getUserAgentStylesheet() {
		return Form.class.getResource("form.css").toExternalForm();
	}

	public Fieldset fieldset(String text) {
		Fieldset fieldset = new Fieldset(text);
		getChildren().add(fieldset);
		return fieldset;
	}

	public Fieldset fieldset(String text, Orientation labelPosition ){
		Fieldset fieldset = new Fieldset( text, labelPosition );
		getChildren().add(fieldset);
		return fieldset;
	}

	public Fieldset fieldset() {
		Fieldset fieldset = new Fieldset();
		getChildren().add(fieldset);
		return fieldset;
	}

	public void add( Fieldset fieldset ){
		getChildren().add(fieldset);
	}
}
