package tornadofx.control.test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import tornadofx.control.Fieldset;
import tornadofx.control.Form;
import tornadofx.property.DirtyState;

public class FormDemo extends Application {
	public void start(Stage stage) throws Exception {
		Customer customer = Customer.createSample(555);
		DirtyState dirtyState = new DirtyState(customer);

		Form form = new Form();
		form.setPadding(new Insets(20));

		Fieldset contactInfo = form.fieldset("Contact Information");

		TextField idInput = new TextField();
		idInput.textProperty().bindBidirectional(customer.idProperty(), new NumberStringConverter());
		contactInfo.field("Id", idInput);

		TextField usernameInput = new TextField();
		usernameInput.textProperty().bindBidirectional(customer.usernameProperty());
		contactInfo.field("Username", usernameInput);

		TextField zipInput = new TextField();
		zipInput.textProperty().bindBidirectional(customer.zipProperty());
		zipInput.setMinWidth(80);
		zipInput.setMaxWidth(80);
		TextField cityInput = new TextField();
		cityInput.textProperty().bindBidirectional(customer.cityProperty());
		contactInfo.field("Zip/City", zipInput, cityInput);

		Button saveButton = new Button("Save");
		saveButton.disableProperty().bind(dirtyState.not());
		saveButton.setOnAction(event -> dirtyState.reset());

		Button undoButton = new Button("Undo");
		undoButton.setOnAction(event -> dirtyState.undo());
		undoButton.visibleProperty().bind(dirtyState);
		contactInfo.field(saveButton, undoButton);

		stage.setScene(new Scene(form, 400,-1));

		stage.show();
	}
}
