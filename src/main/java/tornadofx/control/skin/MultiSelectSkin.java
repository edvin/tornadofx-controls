package tornadofx.control.skin;

import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import tornadofx.control.MultiSelect;

public class MultiSelectSkin<E> extends SkinBase<MultiSelect<E>> {

	public MultiSelectSkin(MultiSelect<E> control) {
		super(control);
	}

	private double getPrefRowHeight() {
		double editorHeight = getSkinnable().getEditor().prefHeight(-1);
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

		return widestRow + leftInset + rightInset - hgap;
	}

	protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
	}

	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		MultiSelect<E> control = getSkinnable();

		double usedLineWidth = 0;
		double hgap = control.getHgap().doubleValue();
		double vgap = control.getVgap().doubleValue();
		double prefHeight = getPrefRowHeight();

		double y = prefHeight;
		if (width == -1 && control.getWidth() > 0)
			width = control.getWidth();

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
