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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.stream.Stream;

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
	private ObjectProperty<Orientation> orientation = new SimpleObjectProperty<>();
	private ObjectProperty<Double> wrapWidth = new SimpleObjectProperty<>();

    public Fieldset(String text) {
        this();
        setText(text);
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
            if (newValue != null) addLegend();
        });

        // Make sure input children gets the configured HBox.hgrow property
        getChildren().addListener((ListChangeListener<Node>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().stream().filter(added -> added instanceof Field).forEach(added -> {
                        Field field = (Field) added;

                        // Configure hgrow for current children
                        field.getInputContainer().getChildren().forEach(this::configureHgrow);

                        // Add listener to support inputs added later
                        field.getInputContainer().getChildren().addListener((ListChangeListener<Node>) c1 -> {
                            while (c1.next()) if (c1.wasAdded()) c1.getAddedSubList().forEach(this::configureHgrow);
                        });
                    });
                }
            }
        });

        // Change HGrow for unconfigured children when inputGrow changes
        inputGrowProperty().addListener((observable, oldValue, newValue) -> {
            getChildren().stream().filter(c -> c instanceof Field).forEach(c -> {
                Field field = (Field) c;
                field.getInputContainer().getChildren().forEach(this::configureHgrow);
            });
        });

	    // Default
	    setOrientation(HORIZONTAL);
    }

    private void syncOrientationState() {
        // Apply pseudo classes when orientation changes
        orientation.addListener((observable, oldValue, newValue) -> {
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
            ObjectBinding<Orientation> responsiveOrientation = createObjectBinding(() -> getWidth() < newValue ? VERTICAL : HORIZONTAL, widthProperty());

            if (orientationProperty().isBound())
                orientationProperty().unbind();

            orientationProperty().bind(responsiveOrientation);
        }));
    }

    private void addLegend() {
        Label legend = new Label();
        legend.textProperty().bind(textProperty());
        legend.getStyleClass().add("legend");
        getChildren().add(0, legend);
    }

    private void configureHgrow(Node input) {
        HBox.setHgrow(input, getInputGrow());
    }

    public Form getForm() {
        return (Form) getParent();
    }

    Stream<Field> getFields() {
        return getChildren().stream()
                .filter(c -> c instanceof Field)
                .map(c -> (Field) c);
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

    public Orientation getOrientation() {
        return orientation.get();
    }

    public ObjectProperty<Orientation> orientationProperty() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation.set(orientation);
    }

    public Double getWrapWidth() {
        return wrapWidth.get();
    }

    public ObjectProperty<Double> wrapWidthProperty() {
        return wrapWidth;
    }

    public void setWrapWidth(Double wrapWidth) {
        this.wrapWidth.set(wrapWidth);
    }
}