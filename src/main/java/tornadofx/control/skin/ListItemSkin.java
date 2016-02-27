package tornadofx.control.skin;

import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import tornadofx.control.ListItem;
import tornadofx.control.ListMenu;

import java.util.function.Function;

public class ListItemSkin extends SkinBase<ListItem> {
	private final Text text;

	public ListItemSkin(ListItem control) {
		super(control);
		text = new Text();
		text.textProperty().bind(control.getTextProperty());

		getChildren().add(text);

		if (getSkinnable().getGraphic() != null)
			getChildren().add(getSkinnable().getGraphic());

		control.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			control.requestFocus();
			ListMenu menu = (ListMenu) control.getParent();
			menu.setActive(control);
		});
	}

	private double acc(Function<Node, Double> fn) {
		double val = fn.apply(text);
		if (getSkinnable().getGraphic() != null)
			val += fn.apply(getSkinnable().getGraphic());
		return val;
	}

	private double biggest(Function<Node, Double> fn) {
		double val = fn.apply(text);

		if (getSkinnable().getGraphic() != null) {
			double gval = fn.apply(getSkinnable().getGraphic());
			if (gval > val) return gval;
		}

		return val;
	}

	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		if (iconPosition().isHorizontal())
			return biggest(n -> n.minWidth(height));
		else
			return acc(n -> n.minWidth(height));
	}

	protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		if (iconPosition().isHorizontal())
			return acc(n -> n.minHeight(width));
		else
			return biggest(n -> n.minHeight(width));
	}

	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		double w = text.prefWidth(height);

		if (getSkinnable().getGraphic() != null && iconPosition().isVertical())
			w += Math.max(getSkinnable().getGraphic().prefWidth(-1), graphicFixedSize());

		return w + leftInset + rightInset;
	}

	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		double h = text.prefHeight(width);

		if (getSkinnable().getGraphic() != null && iconPosition().isHorizontal())
			h += Math.max(getSkinnable().getGraphic().prefHeight(-1), graphicFixedSize());

		return h + topInset + bottomInset;
	}

	protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
	}

	protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
	}

	private Side iconPosition() {
		ListMenu menu = (ListMenu) getSkinnable().getParent();
		return menu.getIconPosition();
	}

	protected void layoutChildren(double x, double y, double w, double h) {
		Node graphic = getSkinnable().getGraphic();

		switch (iconPosition()) {
			case TOP: {
				if (graphic != null) {
					double centeredX = x + (w / 2) - graphic.getLayoutBounds().getWidth() / 2;
					graphic.relocate(centeredX, y);
					y += Math.max(graphic.getLayoutBounds().getHeight(), graphicFixedSize());
				}

				double centeredX = x + (w / 2) - text.prefWidth(-1) / 2;
				text.resizeRelocate(centeredX, y, text.prefWidth(-1), text.prefHeight(-1));
				break;
			}
			case BOTTOM: {
				double centeredX = x + (w / 2) - text.prefWidth(-1) / 2;
				text.resizeRelocate(centeredX, y, text.prefWidth(-1), text.prefHeight(-1));

				if (graphic != null) {
					y += text.prefHeight(-1);
					double fixedSize = graphicFixedSize();
					if (fixedSize > graphic.getLayoutBounds().getHeight())
						y += fixedSize - graphic.getLayoutBounds().getHeight();

					centeredX = x + (w / 2) - graphic.getLayoutBounds().getWidth() / 2;
					graphic.relocate(centeredX, y);
				}

				break;
			}

			case LEFT: {
				if (graphic != null) {
					double centeredY = y + (h / 2) - graphic.getLayoutBounds().getHeight() / 2;
					graphic.relocate(x, centeredY);

					x += Math.max(graphic.getLayoutBounds().getWidth(), graphicFixedSize());
				}

				double centeredY = y + (h / 2) - text.prefHeight(-1) / 2;
				text.resizeRelocate(x, centeredY, text.prefWidth(-1), text.prefHeight(-1));

				break;
			}
			case RIGHT: {
				if (graphic != null) {
					double centeredY = y + (h / 2) - graphic.getLayoutBounds().getHeight() / 2;
					double graphicWidth = Math.max(graphic.getLayoutBounds().getWidth(), graphicFixedSize());

					graphic.resizeRelocate(w, centeredY, graphicWidth, graphic.prefHeight(-1));
				}

				double centeredY = y + (h / 2) - text.prefHeight(-1) / 2;
				text.resizeRelocate(x, centeredY, text.prefWidth(-1), text.prefHeight(-1));
				break;
			}
		}


	}

	private double graphicFixedSize() {
		ListMenu menu = (ListMenu) getSkinnable().getParent();
		return menu.graphicFixedSizeProperty().getValue().doubleValue();
	}

}
