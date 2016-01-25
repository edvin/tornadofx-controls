package tornadofx.control.behavior;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import javafx.scene.control.Control;
import javafx.scene.control.FocusModel;
import javafx.scene.control.MultipleSelectionModel;
import tornadofx.control.MultiSelectCell;

import java.util.Collections;

public class MultiSelectCellBehavor<E> extends CellBehaviorBase<MultiSelectCell<E>> {
	public MultiSelectCellBehavor(MultiSelectCell<E> control) {
		super(control, Collections.emptyList());
	}

	protected Control getCellContainer() {
		return getControl();
	}

	protected MultipleSelectionModel<?> getSelectionModel() {
		return null;
	}

	protected FocusModel<?> getFocusModel() {
		return null;
	}

	protected void edit(MultiSelectCell<E> cell) {

	}
}
