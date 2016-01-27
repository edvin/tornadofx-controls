package tornadofx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.util.StringConverter;
import tornadofx.control.skin.MultiSelectSkin;

import java.util.List;
import java.util.function.BiFunction;

public final class MultiSelect<E> extends Control {
	private static final StyleablePropertyFactory<MultiSelect> FACTORY = new StyleablePropertyFactory<>(Region.getClassCssMetaData());
	private StyleableProperty<Number> hgap = FACTORY.createStyleableNumberProperty(this, "hgap", "-fx-hgap", MultiSelect::hgapProperty);
	private StyleableProperty<Number> vgap = FACTORY.createStyleableNumberProperty(this, "vgap", "-fx-vgap", MultiSelect::vgapProperty);

	private ObservableList<E> items = FXCollections.observableArrayList();
	private ObjectProperty<BiFunction<MultiSelect<E>, E, Node>> cellFactory = new SimpleObjectProperty<>();
	private ObjectProperty<Node> editor = new SimpleObjectProperty<Node>() {
		public void set(Node newEditor) {
			Node old = get();

			if (old != null)
				getChildren().remove(old);

			getChildren().add(newEditor);

			super.set(newEditor);
		}
	};

	public ObservableList<E> getItems() {
		return items;
	}

	public Number getHgap() {
		return hgap.getValue();
	}

	public void setHgap(Number hgap) {
		this.hgap.setValue(hgap);
	}

	public StyleableProperty<Number> hgapProperty() {
		return hgap;
	}

	public Number getVgap() {
		return vgap.getValue();
	}

	public void setVgap(Number vgap) {
		this.vgap.setValue(vgap);
	}

	public StyleableProperty<Number> vgapProperty() {
		return vgap;
	}

	public Node getEditor() {
		return editor.get();
	}

	public ObjectProperty<Node> editorProperty() {
		return editor;
	}

	public void setEditor(Node editor) {
		this.editor.set(editor);
	}

	public BiFunction<MultiSelect<E>, E, Node> getCellFactory() {
		return cellFactory.get();
	}

	public void setCellFactory(BiFunction<MultiSelect<E>, E, Node> cellFactory) {
		this.cellFactory.set(cellFactory);
	}

	public ObjectProperty<BiFunction<MultiSelect<E>, E, Node>> cellFactoryProperty() {
		return cellFactory;
	}

	private ObjectProperty<StringConverter<E>> converter = new SimpleObjectProperty<>();

	public StringConverter<E> getConverter() {
		return converter.get();
	}

	public ObjectProperty<StringConverter<E>> converterProperty() {
		return converter;
	}

	public void setConverter(StringConverter<E> converter) {
		this.converter.set(converter);
	}

	public MultiSelect() {
		getStyleClass().add("multi-select");
		setFocusTraversable(true);
		setEditor(new DefaultEditor(this));
		setCellFactory(new DefaultCellFactory<>());
		configureItemChangeListener();
	}

	private void configureItemChangeListener() {
		items.addListener((ListChangeListener<E>) c -> {
			while (c.next()) {
				if (c.wasRemoved())
					getChildren().remove(c.getFrom(), c.getTo() + 1);

				if (c.wasAdded()) {
					for (int i = 0; i < c.getAddedSize(); i++) {
						E item = c.getAddedSubList().get(i);
						Node cell = getCellFactory().apply(this, item);
						getChildren().add(i + c.getFrom(), cell);
					}
				}
			}
		});
	}

	/**
	 * Convert the given string to an Item by using the configured converter.
	 *
	 * @param text The string that the converter knows how to convert to an item
	 */
	public void addItem(String text) {
		StringConverter<E> c = getConverter();
		if (c == null)
			throw new IllegalArgumentException("You must define a converter before you can add items as Strings");

		E item = c.fromString(text);

		if (item != null)
			getItems().add(item);
	}

	protected Skin<?> createDefaultSkin() {
		return new MultiSelectSkin<>(this);
	}

	public String getUserAgentStylesheet() {
		return MultiSelect.class.getResource("multiselect.css").toExternalForm();
	}

	public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
		return FACTORY.getCssMetaData();
	}

	public void focusPrevious(Node current) {
		int index = getChildren().indexOf(current);
		if (index > 0) {
			Node previous = getChildren().get(index - 1);
			previous.requestFocus();
		}
	}

	private static  class DefaultEditor extends TextField {
		public <E> DefaultEditor(MultiSelect<E> multiSelect) {
			addEventHandler(KeyEvent.KEY_PRESSED, event -> {
				// Submit value on ENTER or TAB
				if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
					if (!getText().isEmpty()) {
						multiSelect.addItem(getText());
						setText("");
					}
				}

				// Tab to previous field on BACKSPACE in blank field
				if (event.getCode() == KeyCode.BACK_SPACE && getText().isEmpty())
					multiSelect.focusPrevious(this);
			});
		}
	}

	private static class DefaultCellFactory<E> implements BiFunction<MultiSelect<E>, E, Node> {
		public Node apply(MultiSelect<E> multiSelect, E item) {
			SplitMenuButton button = new SplitMenuButton();
			button.setText(multiSelect.getConverter().toString(item));

			MenuItem remove = new MenuItem(String.format("Remove %s", item));
			remove.setOnAction(event -> {
				int index = multiSelect.getChildrenUnmodifiable().indexOf(button);
				multiSelect.getItems().remove(item);
				multiSelect.getChildrenUnmodifiable().get(index).requestFocus();
			});

			button.getItems().add(remove);

			button.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if (event.getCode() == KeyCode.BACK_SPACE)
					remove.getOnAction().handle(null);
			});
			return button;
		}
	}
}
