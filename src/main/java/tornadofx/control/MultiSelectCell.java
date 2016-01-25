package tornadofx.control;

import javafx.scene.control.IndexedCell;
import javafx.scene.control.Skin;
import tornadofx.control.skin.MultiSelectCellSkin;

public class MultiSelectCell<E> extends IndexedCell<E> {

	public MultiSelectCell() {
		getStyleClass().add("multi-select-cell");
		setFocusTraversable(true);
	}

	public void updateItem(E item, boolean empty) {
		super.updateItem(item, empty);
	}

	public void requestFocus() {
		if (getChildren().isEmpty())
			super.requestFocus();
		else
			getChildren().get(0).requestFocus();
	}

	protected Skin<?> createDefaultSkin() {
		return new MultiSelectCellSkin<>(this);
	}
}
