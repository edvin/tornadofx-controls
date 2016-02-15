package tornadofx.control;

import javafx.scene.control.SkinBase;

import static javafx.geometry.Orientation.HORIZONTAL;

public class FieldSkin extends SkinBase<Field> {
    public FieldSkin(Field control) {
        super(control);
    }

    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        Field field = getSkinnable();
        Fieldset fieldset = field.getFieldset();

        double labelWidth = field.getFieldset().getForm().getLabelContainerWidth();
        double inputWidth = field.getInputContainer().prefWidth(height);

        if (fieldset.getOrientation() == HORIZONTAL)
            return Math.max(labelWidth, inputWidth) + leftInset + rightInset;

        return labelWidth + inputWidth + leftInset + rightInset;
    }

    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        Field field = getSkinnable();
        Fieldset fieldset = field.getFieldset();

        double labelHeight = field.getLabelContainer().prefHeight(-1);
        double inputHeight = field.getInputContainer().prefHeight(-1);

        if (fieldset.getOrientation() == HORIZONTAL)
            return Math.max(labelHeight, inputHeight) + topInset + bottomInset;

        return labelHeight + inputHeight + topInset + bottomInset;

    }

    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        Field field = getSkinnable();
        Fieldset fieldset = field.getFieldset();

        double labelWidth = field.getFieldset().getForm().getLabelContainerWidth();
	    if (fieldset.getOrientation() == HORIZONTAL) {
            field.getLabelContainer().resizeRelocate(contentX, contentY, Math.min(labelWidth, contentWidth), contentHeight);

            double inputX = contentX + labelWidth;
            double inputWidth = contentWidth - labelWidth;

	        field.getInputContainer().resizeRelocate(inputX, contentY, inputWidth, contentHeight);
        } else {
            double labelPrefHeight = field.getLabelContainer().prefHeight(-1);
            double labelHeight = Math.min(labelPrefHeight, contentHeight);

            field.getLabelContainer().resizeRelocate(contentX, contentY, Math.min(labelWidth, contentWidth), labelHeight);

            double restHeight = labelHeight - contentHeight;

            field.getInputContainer().resizeRelocate(contentX, contentY + labelHeight, contentWidth, restHeight);
        }
    }
}