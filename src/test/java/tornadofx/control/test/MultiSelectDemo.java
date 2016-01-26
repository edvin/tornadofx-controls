package tornadofx.control.test;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tornadofx.control.MultiSelect;
import tornadofx.control.MultiSelectCell;

public class MultiSelectDemo extends Application {

	public void start(Stage stage) throws Exception {
		MultiSelect<String> control = new MultiSelect<>();

		// Default editor
		TextField input = new TextField();
		input.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			// Submit value on ENTER or TAB
			if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
				if (!input.getText().isEmpty()) {
					control.getItems().add(input.getText());
					input.setText("");
				}
			}

			// Tab to previous field on BACKSPACE in blank field
			if (event.getCode() == KeyCode.BACK_SPACE && input.getText().isEmpty())
				focusPrevious(control, input);
		});
		control.setEditor(input);

		control.setCellFactory(param -> new MultiSelectCell<String>() {
			public void updateItem(String item, boolean empty) {
				SplitMenuButton button = new SplitMenuButton();
				button.setText(item);

				MenuItem remove = new MenuItem(String.format("Remove %s", item));
				remove.setOnAction(event -> {
					int index = control.getChildrenUnmodifiable().indexOf(this);
					control.getItems().remove(item);
					control.getChildrenUnmodifiable().get(index).requestFocus();

				});

				button.getItems().add(remove);

				button.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
					if (event.getCode() == KeyCode.BACK_SPACE)
						remove.getOnAction().handle(null);
				});
				setGraphic(button);
			}
		});

		control.getItems().addAll("es@syse.no", "bj@syse.no", "of@syse.no");
		VBox root = new VBox(control, new Label("I am under that thing.."));

		stage.setScene(new Scene(root));
		stage.show();
	}

	private void focusPrevious(MultiSelect<String> control, Node thisNode) {
		int index = control.getChildrenUnmodifiable().indexOf(thisNode);
		if (index > 0) {
			Node previous = control.getChildrenUnmodifiable().get(index - 1);
			previous.requestFocus();
		}
	}
}
