package tornadofx.control.test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tornadofx.control.Fieldset;
import tornadofx.control.Form;

public class FormDemo extends Application {
	public void start(Stage stage) throws Exception {
		Form form = new Form();
		form.setPadding(new Insets(20));

		Fieldset contactInfo = form.fieldset("Contact Information");

		contactInfo.field("Id", new TextField());
		contactInfo.field("Username", new TextField());

		TextField zipInput = new TextField();
		zipInput.setMinWidth(80);
		zipInput.setMaxWidth(80);
		contactInfo.field("Zip/City", zipInput, new TextField());

		contactInfo.field(new Button("Save"));

		stage.setScene(new Scene(form, 400,-1));
		stage.show();
	}
}
