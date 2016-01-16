package tornadofx.control;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import tornadofx.control.skin.ListItemSkin;

public class ListItem extends Control {
	private Node graphic;

	public Node getGraphic() {
		return graphic;
	}

	public void setGraphic(Node graphic) {
		this.graphic = graphic;
	}

	private final StringProperty textProperty = new SimpleStringProperty(this, "text");

	public String getText() {
		return textProperty.get();
	}

	public StringProperty getTextProperty() {
		return textProperty;
	}

	public void setText(String text) {
		textProperty.set(text);
	}

	public ListItem() {
		getStyleClass().add("list-item");
		setFocusTraversable(true);
	}

	public ListItem(String text) {
		this();
		setText(text);
	}

	public ListItem(String text, Node graphic) {
		this(text);
		setGraphic(graphic);
	}

	protected Skin<?> createDefaultSkin() {
		return new ListItemSkin(this);
	}

	public void needsLayout() {
		setNeedsLayout(true);
		requestLayout();
	}

	public void setActive(boolean active) {
		Platform.runLater(() -> {
			ListMenu menu = getMenu();

			if (active) {
				menu.setActive(ListItem.this);
			} else {
				if (menu.getActive() == ListItem.this)
					menu.setActive(null);
			}
		});
	}

	public boolean getActive() {
		return getMenu().getActive() == this;
	}


	private ListMenu getMenu() {
		return (ListMenu) getParent();
	}
}
