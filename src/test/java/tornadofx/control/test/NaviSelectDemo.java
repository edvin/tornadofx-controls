package tornadofx.control.test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tornadofx.control.NaviSelect;

import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class NaviSelectDemo extends Application {
	public void start(Stage stage) throws Exception {
		stage.setTitle("NaviSelect Demo");

		HBox root = new HBox(10);
		root.setAlignment(Pos.CENTER_LEFT);
		root.setPadding(new Insets(50));

		Label label = new Label("Choose customer:");
		label.setStyle("-fx-font-weight: bold");

		NaviSelect<Email> navi = new NaviSelect<>();
		navi.setValue(new Email("john@doe.com", "John Doe"));

		navi.setVisualConverter(Email::getName);
		navi.setOnEdit(event -> selectEmail(navi));

		navi.setOnGoto(event -> {
			Alert alert = new Alert(INFORMATION);
			alert.setContentText(String.format("Action to navigate to %s should be placed here.", navi.getValue()));
			alert.setHeaderText("Goto action triggered");
			alert.show();
		});

		root.getChildren().addAll(label, navi, new TextField());

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Select value example. Implement whatever technique you want to change value of the NaviSelect
	 */
	private void selectEmail(NaviSelect<Email> navi) {
		Stage dialog = new Stage(StageStyle.UTILITY);
		dialog.setTitle("Choose person");
		ListView<Email> listview = new ListView<>(FXCollections.observableArrayList(
			new Email("john@doe.com", "John Doe"),
			new Email("jane@doe.com", "Jane Doe"),
			new Email("some@dude.com", "Some Dude")
		));
		listview.setOnMouseClicked(event -> {
			Email item = listview.getSelectionModel().getSelectedItem();
			if (item != null) {
				navi.setValue(item);
				dialog.close();
			}
		});
		dialog.setScene(new Scene(listview));
		dialog.setWidth(navi.getWidth());
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setHeight(100);
		dialog.showAndWait();
	}
}
