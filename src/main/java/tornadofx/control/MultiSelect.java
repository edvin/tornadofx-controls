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
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import tornadofx.control.skin.MultiSelectSkin;

import java.util.List;

public final class MultiSelect<E> extends Control {
	private static final StyleablePropertyFactory<MultiSelect> FACTORY = new StyleablePropertyFactory<>(Region.getClassCssMetaData());
	private StyleableProperty<Number> hgap = FACTORY.createStyleableNumberProperty(this, "hgap", "-fx-hgap", MultiSelect::hgapProperty);
	private StyleableProperty<Number> vgap = FACTORY.createStyleableNumberProperty(this, "vgap", "-fx-vgap", MultiSelect::vgapProperty);

	private ObservableList<E> items;
	private ObjectProperty<Callback<MultiSelect<E>, MultiSelectCell<E>>> cellFactory = new SimpleObjectProperty<>();
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

	public Callback<MultiSelect<E>, MultiSelectCell<E>> getCellFactory() {
		return cellFactory.get();
	}

	public ObjectProperty<Callback<MultiSelect<E>, MultiSelectCell<E>>> cellFactoryProperty() {
		return cellFactory;
	}

	public void setCellFactory(Callback<MultiSelect<E>, MultiSelectCell<E>> cellFactory) {
		this.cellFactory.set(cellFactory);
	}

	public MultiSelect() {
		getStyleClass().add("multi-select");
		setFocusTraversable(true);
		items = FXCollections.observableArrayList();

		items.addListener((ListChangeListener<E>) c -> {
			while (c.next()) {
				if (c.wasRemoved())
					getChildren().remove(c.getFrom(), c.getTo() + 1);

				if (c.wasAdded()) {
					for (int i = 0; i < c.getAddedSize(); i++) {
						E item = c.getAddedSubList().get(i);
						MultiSelectCell<E> cell = createCell(i, item);
						getChildren().add(i + c.getFrom(), cell);
					}
				}
			}
		});

	}

	private MultiSelectCell<E> createCell(int index, E item) {
		MultiSelectCell<E> cell = getCellFactory().call(this);
		cell.setItem(item);
		cell.updateItem(item, false);
		cell.updateIndex(index);
		return cell;
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
}
