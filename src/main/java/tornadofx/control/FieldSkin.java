package tornadofx.control;

import javafx.scene.control.SkinBase;
import tornadofx.control.Field;

public class FieldSkin extends SkinBase<Field> {
    public FieldSkin(Field control) {
        super(control);
    }

    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
	    Field field = getSkinnable();

	    double labelContainerWidth = field.getFieldset().getForm().getLabelContainerWidth();
	    return labelContainerWidth + field.getInputContainer().getPrefWidth() + leftInset + rightInset;
    }

    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        Field field = getSkinnable();

        double labelContainerWidth = field.getFieldset().getForm().getLabelContainerWidth();
        field.getLabelContainer().resizeRelocate(contentX, contentY, Math.min(labelContainerWidth, contentWidth), contentHeight);

        double inputX = contentX + labelContainerWidth;
        double inputWidth = contentWidth - labelContainerWidth;
        field.getInputContainer().resizeRelocate(inputX, contentY, inputWidth, contentHeight);
    }
}
