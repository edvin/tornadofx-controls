package tornadofx.control.skin;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import tornadofx.control.MultiSelect;
import tornadofx.control.MultiSelectCell;

import java.util.ArrayList;
import java.util.List;

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
	}

	private double getPrefRowHeight() {
		MultiSelect<E> control = getSkinnable();
		double editorHeight = control.getEditor().prefHeight(-1);
		if (getChildren().isEmpty())
			return editorHeight;
		else
			return Math.max(editorHeight, getChildren().get(0).prefHeight(-1));
	}

	/**
	 * Compute pref width by placing equal amount of childen on each line, with a line height equal to the editors preferred
	 * height plus the vgap. This will not be correct in every case, depending on the difference in the other childrens
	 * preferred width, but it seems to be adequate.
	 */
	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		MultiSelect<E> control = getSkinnable();

		double hgap = control.getHgap().doubleValue();
		double vgap = control.getVgap().doubleValue();
		double prefRowHeight = getPrefRowHeight();

		int childCount = getChildren().size();
		int rows = height <= 0 ? 1 : (int) Math.max(1, Math.floor((prefRowHeight + vgap) / height));
		int perRow = (int) Math.ceil(childCount / rows);

		double widestRow = 0;
		int childPos = 0;
		for (int rowCount = 0; rowCount < rows; rowCount++) {
			double rowWidth = 0;
			double childPosInRow = 0;

			while (childPosInRow < perRow && childPos < childCount) {
				Node child = getChildren().get(childPos);
				rowWidth += child.prefWidth(prefRowHeight) + hgap;
				childPos++;
				childPosInRow++;
			}

			if (rowWidth > widestRow)
				widestRow = rowWidth;
		}

		return widestRow + leftInset + rightInset;
	}

	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		double usedLineWidth = 0;
		double hgap = getSkinnable().getHgap().doubleValue();
		double vgap = getSkinnable().getVgap().doubleValue();
		double prefHeight = getPrefRowHeight();

		double y = prefHeight;

		for (Node node : getChildren()) {
			double prefWidth = node.prefWidth(prefHeight);
			if (width > 0 && usedLineWidth + prefWidth > width && usedLineWidth > 0) {
				usedLineWidth = 0;
				y += prefHeight + vgap;
			}
			usedLineWidth += prefWidth + hgap;
		}

		return y;
	}

	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
		double usedLineWidth = 0;
		double hgap = getSkinnable().getHgap().doubleValue();
		double vgap = getSkinnable().getVgap().doubleValue();
		double prefHeight = getPrefRowHeight();

		for (Node node : getChildren()) {
			double prefWidth = node.prefWidth(prefHeight);
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
