package tornadofx.control.test;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tornadofx.control.ListItem;
import tornadofx.control.ListMenu;

public class ListMenuDemo extends Application {
	private Scene scene;

	public void start(Stage stage) throws Exception {
		stage.setTitle("ListMenu Demo Application");

		ListItem item1 = new ListItem("Contacts", icon(FontAwesomeIcon.USER));
		ListItem item2 = new ListItem("Projects", icon(FontAwesomeIcon.SUITCASE));
		ListItem item3 = new ListItem("Settings", icon(FontAwesomeIcon.COG));

		ListMenu menu = new ListMenu(item1, item2, item3);
		menu.setActive(item1);

		Label titleLabel = new Label(stage.getTitle());
		titleLabel.setStyle("-fx-font-size: 3em");

		Label selectedDescription = new Label();
		selectedDescription.setStyle("-fx-text-fill: red");
		StringBinding description = Bindings.createStringBinding(() -> String.format("Currently selected: %s", menu.getActive().getText()), menu.activeProperty());
		selectedDescription.textProperty().bind(description);

		ComboBox<Orientation> orientation = new ComboBox<>(FXCollections.observableArrayList(Orientation.values()));
		orientation.valueProperty().bindBidirectional(menu.orientationProperty());

		ComboBox<Side> iconPos = new ComboBox<>(FXCollections.observableArrayList(Side.values()));
		iconPos.valueProperty().bindBidirectional(menu.iconPositionProperty());

		CheckBox customCss = new CheckBox("Custom CSS");
		customCss.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue)
				scene.getStylesheets().add(getCustomStylesheet());
			else
				scene.getStylesheets().remove(getCustomStylesheet());
		});

		HBox options = new HBox(10, new Label("Orientation"), orientation, new Label("Icon Position"), iconPos, customCss);
		options.setAlignment(Pos.CENTER);

		BorderPane borderpane = new BorderPane(menu);
		borderpane.setStyle("-fx-background-color: #ffffff");
		borderpane.setTop(new VBox(10.0, titleLabel, options, selectedDescription));
		borderpane.getTop().setStyle("-fx-alignment: center");
		borderpane.setPadding(new Insets(20));

		scene = new Scene(borderpane, 600, 500);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.show();
	}

	private String getCustomStylesheet() {
		return getClass().getResource("/custom.css").toExternalForm();
	}

	private FontAwesomeIconView icon(FontAwesomeIcon user) {
		FontAwesomeIconView iconview = new FontAwesomeIconView(user);
		iconview.setGlyphSize(20);
		return iconview;
	}
}
