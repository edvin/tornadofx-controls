package tornadofx.control.test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tornadofx.control.DateTimePicker;
import tornadofx.control.Fieldset;
import tornadofx.control.Form;

import java.time.LocalDateTime;

public class DateTimeDemo extends Application {
	public void start(Stage stage) throws Exception {
		Form form = new Form();
		form.setPadding(new Insets(50));
		Fieldset fieldset = form.fieldset("DateTimePicker Demo");

		DateTimePicker picker = new DateTimePicker();
		picker.setDateTimeValue(LocalDateTime.now());
		fieldset.field("Choose date", picker);

		TextField label = new TextField();
		label.textProperty().bind(picker.dateTimeValueProperty().asString());
		fieldset.field("Selected value:", label);

		Button setNow = new Button("Set now");
		setNow.setOnAction(event -> picker.setDateTimeValue(LocalDateTime.now()));
		fieldset.field("Reset", setNow);

		stage.setScene(new Scene(form, 400, 200));
		stage.show();
	}
}
