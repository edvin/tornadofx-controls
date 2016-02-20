package tornadofx.control;

import javafx.beans.DefaultProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

@DefaultProperty("content")
public class InlineHTML extends StackPane {
	private WebView view;
	private SimpleStringProperty content = new SimpleStringProperty();

	public InlineHTML() {
		getStyleClass().add("inline-html");
		view = new WebView();
		getChildren().add(view);

		content.addListener((observable, oldValue, newValue) ->
			view.getEngine().loadContent(newValue));
	}

	public String getUserAgentStylesheet() {
		return getClass().getResource("inline-html.css").toExternalForm();
	}

	public String getContent() {
		return content.get();
	}

	public SimpleStringProperty contentProperty() {
		return content;
	}

	public void setContent(String content) {
		this.content.set(content);
	}
}
