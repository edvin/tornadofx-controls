package tornadofx.control.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tornadofx.control.Form;

public class FormFxmlDemo extends Application {
	public void start(Stage stage) throws Exception {
		Form form = FXMLLoader.load(FormFxmlDemo.class.getResource("/FormDemo.fxml"));
		Scene scene = new Scene(form, 400, -1);
		stage.setScene(scene);
		stage.show();
	}
}
