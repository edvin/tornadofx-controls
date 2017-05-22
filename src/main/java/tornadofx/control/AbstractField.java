package tornadofx.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import tornadofx.util.NodeHelper;


public abstract class AbstractField extends Pane {

    SimpleStringProperty textProperty = new SimpleStringProperty();

    private boolean forceLabelIndent = false;

    private Label label = new Label();
    private LabelContainer labelContainer = new LabelContainer(label);

    public AbstractField( String text, boolean forceLabelIndent ){
        setText(text);
        this.forceLabelIndent = forceLabelIndent;
        setFocusTraversable(false);
        getStyleClass().add("field");
        label.textProperty().bind(textProperty);
        getChildren().add( labelContainer );

    }

    public String getText(){
        return textProperty.get();
    }

    public void setText(String text ){
        textProperty.set(text);
    }

    public void setForceLabelIndent(boolean forceLabelIndent ){
        this.forceLabelIndent = forceLabelIndent;
    }

    public boolean getForceLabelIndent(){
        return this.forceLabelIndent;
    }

    public Fieldset getFieldSet(){
        return NodeHelper.findParentOfType(this,Fieldset.class);
    }

    public abstract Region getInputContainer();

    // FXML Default Target
    public abstract ObservableList<Node> getInputs();

    @Override
    protected double computePrefHeight( double width ){

        double labelHeight = labelHasContent() ?
                labelContainer.prefHeight(width) : 0.0;
        double inputHeight = getInputContainer().prefHeight(width);

        if( isHorizontalLabelPosition() ){
            return Math.max( labelHeight, inputHeight ) + getVerticalInsets();
        } else {
            return labelHeight + inputHeight + getVerticalInsets();
        }
    }

    @Override
    protected double computePrefWidth( double height ){

        double labelWidth = labelHasContent() ?
                getFieldSet().getForm().getLabelContainerWidth(height) : 0.0;
        double inputWidth = getInputContainer().prefWidth(height);

        if( isVerticalLabelPosition() ){
            return Math.max( labelWidth, inputWidth ) + getHorizontalInsets();
        } else {
            return labelWidth + inputWidth + getHorizontalInsets();
        }
    }

    private boolean labelHasContent(){
        return this.forceLabelIndent || !(getText() == null || getText().isEmpty() );
    }

    private double getVerticalInsets(){
        return getInsets().getTop() + getInsets().getBottom();
    }

    private double getHorizontalInsets(){
        return getInsets().getLeft() + getInsets().getRight();
    }

    private boolean isVerticalLabelPosition(){
        return getFieldSet().getLabelPosition().equals( Orientation.VERTICAL );
    }

    private boolean isHorizontalLabelPosition(){
        return !isVerticalLabelPosition();
    }

    @Override
    protected double computeMinHeight( double width ) {
        return computePrefHeight(width);
    }

    @Override
    protected void layoutChildren(){

        double contentX = getInsets().getLeft();
        double contentY = getInsets().getTop();
        double contentWidth = getWidth() - getHorizontalInsets();
        double contentHeight = getHeight() - getVerticalInsets();

        double labelWidth = Math.min( contentWidth,
                getFieldSet().getForm().getLabelContainerWidth( getHeight() ) );

        if( isHorizontalLabelPosition() ){
            if( labelHasContent() ){
                this.labelContainer.resizeRelocate(contentX,contentY,labelWidth,contentHeight);

                double inputX = contentX + labelWidth;
                double inputWidth = contentWidth - labelWidth;

                getInputContainer().resizeRelocate(inputX,contentY,inputWidth,contentHeight);

            } else {
                getInputContainer().resizeRelocate(contentX,contentY,contentWidth,contentHeight);
            }
        } else { // vertical label position
            if( labelHasContent() ){
                double labelPrefHeight = labelContainer.prefHeight( getWidth() );
                double labelHeight = Math.min(labelPrefHeight, contentHeight);

                labelContainer.resizeRelocate(contentX, contentY, Math.min( labelWidth, contentWidth), labelHeight );

                double restHeight = contentHeight - labelHeight;

                getInputContainer().resizeRelocate( contentX, contentY + labelHeight, contentWidth, restHeight );
            } else {
                getInputContainer().resizeRelocate( contentX, contentY, contentWidth, contentHeight );
            }
        }
    }

    public LabelContainer getLabelContainer(){
        return this.labelContainer;
    }

    public class LabelContainer extends HBox {
        public LabelContainer(Label label) {
            getChildren().add(label);
            getStyleClass().add("label-container");
        }
    }

    public Label getLabel(){
        return this.label;
    }

}
