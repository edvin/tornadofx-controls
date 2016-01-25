package tornadofx.control.skin;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import tornadofx.control.ListMenu;

import java.util.function.Function;

public class ListMenuSkin extends SkinBase<ListMenu> {
	public ListMenuSkin(ListMenu control) {
		super(control);
	}

	private double acc(Function<Node, Double> fn) {
		double val = 0;

		for (Node node : getChildren())
			val += fn.apply(node);

		return val;
	}

	private double biggest(Function<Node, Double> fn) {
		double val = 0d;

		for (Node node : getChildren()) {
			double nval = fn.apply(node);
			if (nval > val)
				val = nval;
		}

		return val;
	}

	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		if (getSkinnable().getOrientation() == Orientation.VERTICAL)
			return biggest(n -> n.minWidth(height));
		else
			return acc(n -> n.minWidth(height));
	}

	protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		if (getSkinnable().getOrientation() == Orientation.VERTICAL)
			return acc(n -> n.minHeight(width));
		else
			return biggest(n -> n.minHeight(width));
	}

	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		double prefWidth;

		if (getSkinnable().getOrientation() == Orientation.VERTICAL)
			prefWidth = biggest(n -> n.prefWidth(height)) + leftInset + rightInset;
		else
			prefWidth = acc(n -> n.prefWidth(height)) + leftInset + rightInset;

		return Math.max(prefWidth, getSkinnable().getPrefWidth());
	}

	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		double prefHeight;

		if (getSkinnable().getOrientation() == Orientation.VERTICAL)
			prefHeight = acc(n -> n.prefHeight(width)) + topInset + bottomInset;
		else
			prefHeight = biggest(n -> n.prefHeight(width)) + topInset + bottomInset;

		return Math.max(prefHeight, getSkinnable().getPrefHeight());
	}

	protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
	}

	protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
	}

	protected void layoutChildren(double x, double y, double w, double h) {
		for (Node node : getChildren()) {
			if (getSkinnable().getOrientation() == Orientation.VERTICAL) {
				double prefHeight = node.prefHeight(-1);
				node.resizeRelocate(x, y, w, prefHeight);
				y += prefHeight;
			} else {
				double prefWidth = node.prefWidth(-1);
				node.resizeRelocate(x, y, prefWidth, h);
				x += prefWidth;
			}
		}
	}

}
