package tornadofx.control;

import javafx.beans.DefaultProperty;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import tornadofx.util.NodeHelper;

import java.util.List;

import static javafx.beans.binding.Bindings.createObjectBinding;
import static javafx.geometry.Orientation.HORIZONTAL;
import static javafx.geometry.Orientation.VERTICAL;
import static javafx.scene.layout.Priority.SOMETIMES;

@SuppressWarnings("unused")
@DefaultProperty("children")
public class Fieldset extends VBox {

    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");
    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");

    private SimpleStringProperty text = new SimpleStringProperty();
    private ObjectProperty<Priority> inputGrow = new SimpleObjectProperty<>(SOMETIMES);
	private ObjectProperty<Orientation> labelPosition = new SimpleObjectProperty<>();
	private ObjectProperty<Double> wrapWidth = new SimpleObjectProperty<>();
    private ObjectProperty<Node> icon = new SimpleObjectProperty<>();
    private Label legend;

    public Fieldset(String text) {
        this(text, Orientation.HORIZONTAL);
    }

    public Fieldset(String text, Orientation labelPosition){
        this();
        setText(text);
        setLabelPosition(labelPosition);
    }

    public void add( Node child ){
        getChildren().add( child );
    }

    public Field field(String text, Orientation orientation, Node... inputs){
        Field field = new Field( text, orientation, false, inputs );
        getChildren().add(field);
        return field;
    }

    public Field field(String text, Node... inputs) {
        Field field = new Field(text, inputs);
        getChildren().add(field);
        return field;
    }

    public Field field() {
        Field field = new Field();
        getChildren().add(field);
        return field;
    }

    public Field field(Node... inputs) {
        Field field = new Field(null, inputs);
        getChildren().add(field);
        return field;
    }

    public Fieldset() {

        getStyleClass().add("fieldset");

        syncOrientationState();

        // Add legend label when text is populated
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() ) addLegend();
        });

        // Add legend when icon is populated
        iconProperty().addListener(((observable1, oldValue1, newValue) -> {
            if (newValue != null) addLegend();
        }));

        // Make sure input children get the configured HBox.hgrow property
        syncHgrow();

        // Register/deregister with parent Form
	    parentProperty().addListener( (observable, oldParent, newParent) -> {
	        if( !(oldParent instanceof Form) ) {
                Form oldParentForm = NodeHelper.findParentOfType(oldParent, Form.class);
                if( oldParentForm != null ) {
                    oldParentForm.getChildren().remove(this);
                }
            }
            if( !(newParent instanceof Form ) ) {
                Form newParentForm = NodeHelper.findParentOfType(newParent, Form.class);
                if( newParentForm != null ) {
                    newParentForm.getChildren().remove(this);
                }
            }

        });
    }

    private void syncHgrow(){
        getChildren().addListener((ListChangeListener<Node>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach( this::configureHgrow );
                }
            }
        });

        // Change HGrow for unconfigured children when inputGrow changes
        inputGrowProperty().addListener((observable, oldValue, newValue) -> {
            getChildren().stream().forEach(this::configureHgrow);
        });
    }

    private void syncOrientationState() {
        // Apply pseudo classes when orientation changes
        labelPosition.addListener((observable, oldValue, newValue) -> {
            if (newValue == HORIZONTAL) {
                pseudoClassStateChanged(VERTICAL_PSEUDOCLASS_STATE, false);
                pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, true);
            } else {
                pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, false);
                pseudoClassStateChanged(VERTICAL_PSEUDOCLASS_STATE, true);
            }
        });

        // Setup listeneres for wrapping
        wrapWidth.addListener(((observable, oldValue, newValue) -> {
            ObjectBinding<Orientation> responsiveOrientation =
                    createObjectBinding(() -> getWidth() < newValue ? VERTICAL : HORIZONTAL, widthProperty());

            if (labelPositionProperty().isBound())
                labelPositionProperty().unbind();

            labelPositionProperty().bind(responsiveOrientation);
        }));
    }

    private void addLegend() {
        if (legend == null) {
            legend = new Label();
            legend.textProperty().bind(textProperty());
            legend.getStyleClass().add("legend");
            getChildren().add(0, legend);
        }

        legend.setGraphic(getIcon());
    }

    private void configureHgrow(Node node) {
        HBox.setHgrow(node, getInputGrow());
        if( node instanceof Field ){
            ((Field)node).configureHGrow( getInputGrow() );
        } else if ( node instanceof Pane){
            ((Pane)node).getChildren().forEach( child -> configureHgrow( child ));
        }
    }


    public Form getForm() {
        return NodeHelper.findParentOfType(this,Form.class);
    }

    protected List<Field> getFields(){
        return NodeHelper.findChildrenOfType(this,Field.class );
    }

    public Priority getInputGrow() {
        return inputGrow.get();
    }

    public ObjectProperty<Priority> inputGrowProperty() {
        return inputGrow;
    }

    public void setInputGrow(Priority inputGrow) {
        this.inputGrow.set(inputGrow);
    }

    public String getText() {
        return text.get();
    }

    public SimpleStringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    @Deprecated
    public Orientation getOrientation() {return getLabelPosition(); }

    public Orientation getLabelPosition() { return labelPosition.get(); }

    public ObjectProperty labelPositionProperty() { return labelPosition; }

    @Deprecated
    public ObjectProperty<Orientation> orientationProperty() {
        return labelPosition;
    }

    @Deprecated
    public void setOrientation(Orientation orientation) {
        setLabelPosition(orientation);
    }

    public void setLabelPosition(Orientation orientation) { this.labelPosition.set(orientation); }

    public Double getWrapWidth() {
        return wrapWidth.get();
    }

    public ObjectProperty<Double> wrapWidthProperty() {
        return wrapWidth;
    }

    public void setWrapWidth(Double wrapWidth) {
        this.wrapWidth.set(wrapWidth);
    }

    public Node getIcon() {
        return icon.get();
    }

    public ObjectProperty<Node> iconProperty() {
        return icon;
    }

    public void setIcon(Node icon) {
        this.icon.set(icon);
    }

}