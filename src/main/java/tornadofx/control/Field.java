package tornadofx.control;

import javafx.beans.DefaultProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.*;
import tornadofx.util.NodeHelper;

@DefaultProperty("inputs")
public class Field extends AbstractField {

    private Pane inputContainer = null;
    private Orientation orientation;

    public Field() {
        this(null);
    }

    public Field( String text ){
        this( text, Orientation.HORIZONTAL );
    }

    public Field( String text, Orientation orientation ){
        this( text, orientation, false,null );
    }

    public Field( String text, Orientation orientation, Node... inputs ){
        this( text, orientation, false,inputs );
    }

    public Field(String text, Node... inputs){
        this( text, Orientation.HORIZONTAL, false, inputs );
    }

	public Field(String text, Orientation orientation, boolean forceLabelIndent, Node... inputs) {
		super(text, forceLabelIndent);
		this.orientation = orientation;

        getStyleClass().add("field");

        parentProperty().addListener( (obs, oldParent, newParent) -> {
            if( !(oldParent instanceof Fieldset) ) {
                Fieldset oldFieldSet =
                        NodeHelper.findParentOfType(oldParent, Fieldset.class);
                if (oldFieldSet != null) {
                    oldFieldSet.getFields().remove(this);
                }
            }
            if( !(newParent instanceof Fieldset) ) {
                Fieldset newFieldSet =
                        NodeHelper.findParentOfType(newParent, Fieldset.class);
                if (newFieldSet != null ) {
                    newFieldSet.getFields().add(this);
                }
            }
            syncVgrowConstraints();
        });

        getChildren().add( getInputContainer() );

        if( inputs != null ) {
            getInputs().addAll(inputs);
        }

        for ( Node input : getInputs() ) {
            configureMnemonicTarget( input );
        }

        // Add listener to support inputs added later
        getInputContainer().getChildren().addListener((ListChangeListener<Node>) c1 -> {
            while (c1.next()) if (c1.wasAdded()) c1.getAddedSubList().forEach( node -> {
                if( VBox.getVgrow(node) != null ) {
                    syncVgrowConstraints(node);
                }
                configureMnemonicTarget(node);
            });
        });
    }

    public Fieldset getFieldset() {
        return NodeHelper.findParentOfType( this, Fieldset.class);
    }

    public Pane getInputContainer() {

	    if( inputContainer == null ){
            inputContainer = Orientation.HORIZONTAL.equals( orientation ) ? new HBox() : new VBox();
            inputContainer.getStyleClass().add("input-container");
            NodeHelper.addPseudoClass( inputContainer, this.orientation.name().toLowerCase() );
        }
        return inputContainer;
    }

    protected void syncVgrowConstraints(){
        getInputContainer().getChildren().stream()
            .filter( n -> VBox.getVgrow(n) != null )
            .findFirst( )
            .ifPresent( Field.this::syncVgrowConstraints);
    }

    private void syncVgrowConstraints(Node inputNode){
        Priority priority = VBox.getVgrow(inputNode);
        Field parentField = NodeHelper.findParentOfType(inputNode,Field.class);
        if( parentField != null ){
            VBox.setVgrow( parentField, priority );
            if( parentField.getFieldSet() != null ){
                VBox.setVgrow( parentField.getFieldset(), priority );
            }
        }
    }

    public void configureHGrow( Priority priority ){
        // Configure hgrow for current children
        getInputContainer().getChildren().forEach( node -> HBox.setHgrow(node, priority));

        // Add listener to support inputs added later
        getInputContainer().getChildren().addListener((ListChangeListener<Node>) c1 -> {
            while (c1.next()) if (c1.wasAdded()) c1.getAddedSubList().forEach( node -> {
                HBox.setHgrow(node, priority);
                syncVgrowConstraints( node );
            });

        });
    }

    private void configureMnemonicTarget( Node target ){
        if( target.getProperties().containsKey("mnemonicTarget") ){
            getLabel().setMnemonicParsing(true);
            getLabel().setLabelFor(target);
        }
    }

    public static void setMnemonicTarget( Node target ){
        Field parent = NodeHelper.findParentOfType( target, Field.class);
        if( parent != null ){
            parent.getLabel().setMnemonicParsing(true);
            parent.getLabel().setLabelFor(target);
        } else {
            // we will detect this when this target is added as an input
            target.getProperties().put("mnemonicTarget",Boolean.TRUE);
        }
    }

    public ObservableList<Node> getInputs() {
        return getInputContainer().getChildren();
    }

    public String getText() {
		return textProperty.get();
	}

	public SimpleStringProperty textProperty() {
		return textProperty;
	}

	public void setText(String text) {
		this.textProperty.set(text);
	}

	public void setOrientation( Orientation orientation ){ this.orientation = orientation; }
}
