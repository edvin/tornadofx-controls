package tornadofx.control.skin;

import com.sun.javafx.scene.control.skin.CellSkinBase;
import javafx.scene.Node;
import tornadofx.control.MultiSelectCell;
import tornadofx.control.behavior.MultiSelectCellBehavor;

public class MultiSelectCellSkin<E> extends CellSkinBase<MultiSelectCell<E>, MultiSelectCellBehavor<E>> {
	public MultiSelectCellSkin(MultiSelectCell<E> control) {
		super(control, new MultiSelectCellBehavor<>(control));
	}

	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		Node graphic = getSkinnable().getGraphic();
		if (graphic != null)
			return graphic.minWidth(height);

		return super.computeMinWidth(height, topInset, rightInset, bottomInset, leftInset);
	}

	protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		Node graphic = getSkinnable().getGraphic();
		if (graphic != null)
			return graphic.minHeight(width);

		return super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
	}

	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		Node graphic = getSkinnable().getGraphic();
		if (graphic != null)
			return graphic.prefWidth(height);

		return super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
	}

	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		Node graphic = getSkinnable().getGraphic();
		if (graphic != null)
			return graphic.prefHeight(width);

		return super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
	}

}
