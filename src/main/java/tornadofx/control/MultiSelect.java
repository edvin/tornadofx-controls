package tornadofx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import tornadofx.control.skin.MultiSelectSkin;

public final class MultiSelect<E> extends Control {
	private static final StyleablePropertyFactory<MultiSelect> FACTORY = new StyleablePropertyFactory<>(Region.getClassCssMetaData());
	private StyleableProperty<Number> hgap = FACTORY.createStyleableNumberProperty(this, "hgap", "-fx-hgap", MultiSelect::hgapProperty);
	private StyleableProperty<Number> vgap = FACTORY.createStyleableNumberProperty(this, "vgap", "-fx-vgap", MultiSelect::vgapProperty);

	private ObservableList<E> items;
	private ObjectProperty<Callback<MultiSelect<E>, MultiSelectCell<E>>> cellFactory = new SimpleObjectProperty<>();
	private ObjectProperty<Node> editor = new SimpleObjectProperty<>();

	public ObservableList<E> getItems() {
		return items;
	}

	public void addItem(E item) {
		getItems().add(item);
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
		items = FXCollections.observableArrayList();
	}


	protected Skin<?> createDefaultSkin() {
		return new MultiSelectSkin<>(this);
	}

	public String getUserAgentStylesheet() {
		return MultiSelect.class.getResource("multiselect.css").toExternalForm();
	}
}
