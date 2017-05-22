package tornadofx.control.test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tornadofx.control.ButtonBarField;
import tornadofx.control.Field;
import tornadofx.control.Fieldset;
import tornadofx.control.Form;

public class FormDemo2 extends Application {

	private Fieldset fieldset;
	private ComboBox comboBoxLabelPosition;

	public void start(Stage stage) throws Exception {

		Form form = new Form(){{

			setPadding(new Insets(20));

			add( fieldset = new Fieldset("Fieldset"){{

				add( new Field( "Orientation",
						comboBoxLabelPosition = new ComboBox<Orientation>() {{
							getItems().addAll( Orientation.HORIZONTAL, Orientation.VERTICAL );
							this.setValue( Orientation.VERTICAL );
					}})
				);

                add( new HBox( 15.0 ) {{
                    getChildren().addAll(
                            new Field ( "First", Orientation.HORIZONTAL, new TextField(){{
                                HBox.setHgrow( this, Priority.ALWAYS );
                            }} ),
                            new Field ( "Last", Orientation.HORIZONTAL, new TextField(){{
                                HBox.setHgrow( this, Priority.ALWAYS );
                            }} )
                    );
                }});

				add( new Field("_TextField", new TextField(){{
					Field.setMnemonicTarget(this);
				}}));

				add( new Field(){{
					getInputs().add(new TextField("forceLabelIndent = true"));
					setForceLabelIndent(true);
				}});

				add( new Field( "TextArea", Orientation.VERTICAL,
						new TextArea("VGrow = Priority.ALWAYS"){{
							setPrefRowCount(2);
							VBox.setVgrow(this,Priority.ALWAYS);
						}})
				);

				add( new Field( "TextArea", Orientation.VERTICAL,
						new TextArea("VGrow = Priority.ALWAYS"){{
							setPrefRowCount(2);
							VBox.setVgrow(this,Priority.ALWAYS);
						}})
				);

				add( new Field( "TextArea",
						new TextArea("No VGrow"){{
							setPrefRowCount(2);
						}})
				);

				add( new ButtonBarField(){{
					getInputs().addAll(
							new Button("OK"){{
								ButtonBar.setButtonData(this, ButtonBar.ButtonData.OK_DONE);
							}},
							new Button( "Cancel"){{
								ButtonBar.setButtonData(this, ButtonBar.ButtonData.CANCEL_CLOSE);
							}}
					);
				}});

			}});

			fieldset.labelPositionProperty().bind(
					comboBoxLabelPosition.getSelectionModel().selectedItemProperty()  );

			comboBoxLabelPosition.valueProperty().addListener( (obs, o, n) -> stage.sizeToScene() );

		}};

		stage.setScene(new Scene(form, 600,-1));

		stage.show();
	}
}
