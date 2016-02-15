package tornadofx.control.test;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import tornadofx.control.Fieldset;
import tornadofx.control.Form;

import java.net.URL;
import java.util.ResourceBundle;

public class FormFxmlDemo extends Application {

	public void start(Stage stage) throws Exception {
		Form form = FXMLLoader.load(FormFxmlDemo.class.getResource("/FormDemo.fxml"));

		Scene scene = new Scene(form);
		stage.setScene(scene);
		stage.show();
	}

    public static class FormController implements Initializable {
        @FXML Fieldset responsiveFieldset;
        @FXML TextField wrapAt;

        public void initialize(URL location, ResourceBundle resources) {
            wrapAt.textProperty().bindBidirectional(responsiveFieldset.wrapWidthProperty(), new DoubleStringConverter());
        }
    }
}
