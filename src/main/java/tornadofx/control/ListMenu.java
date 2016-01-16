package tornadofx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.*;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import tornadofx.control.skin.ListMenuSkin;

import java.util.Collections;
import java.util.List;

public class ListMenu extends Control {
	private static final PseudoClass ACTIVE_PSEUDOCLASS_STATE =
		PseudoClass.getPseudoClass("active");

	private static final StyleablePropertyFactory<ListMenu> FACTORY = new StyleablePropertyFactory<>(Collections.emptyList());

	public final StyleableProperty<Number> graphicFixedSizeProperty() {
		return graphicFixedSize;
	}

	private StyleableProperty<Number> graphicFixedSize = FACTORY.createStyleableNumberProperty(this, "graphicFixedSize", "-fx-graphic-fixed-size", ListMenu::graphicFixedSizeProperty);

	private Property<ListItem> active = new SimpleObjectProperty<>(this, "active");

	public void setActive(ListItem item) {
		ListItem previouslyActive = getActive();
		if (item == previouslyActive)
			return;

		if (previouslyActive != null)
			previouslyActive.pseudoClassStateChanged(ACTIVE_PSEUDOCLASS_STATE, false);

		item.pseudoClassStateChanged(ACTIVE_PSEUDOCLASS_STATE, true);
		active.setValue(item);
	}

	public ListItem getActive() {
		return active.getValue();
	}

	public Property<ListItem> activeProperty() {
		return active;
	}

	public Orientation getOrientation() {
		return orientation.get();
	}

	public ObjectProperty<Orientation> orientationProperty() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation.set(orientation);
	}

	private final ObjectProperty<Orientation> orientation = new SimpleObjectProperty<Orientation>(Orientation.VERTICAL) {
		protected void invalidated() {
			setNeedsLayout(true);
			requestLayout();
		}
	};

	private final ObjectProperty<Side> iconPosition = new SimpleObjectProperty<Side>(Side.LEFT) {
		protected void invalidated() {
			getChildren().forEach(child -> {
				if (child instanceof ListItem) {
					((ListItem) child).needsLayout();
				}
			});
		}
	};

	public Side getIconPosition() {
		return iconPosition.get();
	}

	public ObjectProperty<Side> iconPositionProperty() {
		return iconPosition;
	}

	public void setIconPosition(Side iconPosition) {
		this.iconPosition.set(iconPosition);
	}

	public ListMenu(ListItem... items) {
		getStyleClass().add("list-menu");
		setFocusTraversable(true);
		if (items != null)
			getChildren().addAll(items);
	}

	protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
		return FACTORY.getCssMetaData();
	}

	protected Skin<?> createDefaultSkin() {
		return new ListMenuSkin(this);
	}

	public ObservableList<Node> getChildren() {
		return super.getChildren();
	}

	public String getUserAgentStylesheet() {
		return ListMenu.class.getResource("listmenu.css").toExternalForm();
	}
}
