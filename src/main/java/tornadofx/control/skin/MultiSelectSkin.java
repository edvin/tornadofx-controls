package tornadofx.control.skin;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import tornadofx.control.MultiSelect;
import tornadofx.control.MultiSelectCell;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MultiSelectSkin<E> extends SkinBase<MultiSelect<E>> {

	public MultiSelectSkin(MultiSelect<E> control) {
		super(control);
		control.getItems().addListener((ListChangeListener<E>) c -> updateChildren());
		updateChildren();
	}

	public void updateChildren() {
		MultiSelect<E> control = getSkinnable();

		List<Node> newChildren = new ArrayList<>();

		for (int i = 0; i < control.getItems().size(); i++) {
			E item = control.getItems().get(i);
			MultiSelectCell<E> cell = control.getCellFactory().call(control);
			cell.setItem(item);
			cell.updateItem(item, false);
			cell.updateIndex(i);
			newChildren.add(cell);
		}

		newChildren.add(control.getEditor());

		getChildren().setAll(newChildren);

		control.requestLayout();
	}

	private double getPrefRowHeight() {
		return getSkinnable().getEditor().prefHeight(-1);
	}

	private double acc(Function<Node, Double> fn) {
		return getChildren().stream().mapToDouble(fn::apply).sum();
	}

	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		return getSkinnable().getEditor().minWidth(height) + leftInset + rightInset;
	}

	protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		return getSkinnable().getEditor().minHeight(width) + topInset + bottomInset;
	}

	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		return 100;
	}

	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		return 200;
	}

	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
		double usedLineWidth = 0;
		double hgap = getSkinnable().getHgap().doubleValue();
		double vgap = getSkinnable().getVgap().doubleValue();
		double prefHeight = getPrefRowHeight();

		for (Node node : getChildren()) {
			double prefWidth = node.prefWidth(-1);
			if (usedLineWidth + prefWidth > contentWidth && usedLineWidth > 0) {
				usedLineWidth = 0;
				contentY += prefHeight + vgap;
			}
			double x = usedLineWidth + contentX;
			node.resizeRelocate(x, contentY, prefWidth, prefHeight);
			usedLineWidth += prefWidth + hgap;
		}
	}

}
