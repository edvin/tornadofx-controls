package tornadofx.control.skin;

import com.sun.javafx.scene.control.skin.CellSkinBase;
import tornadofx.control.MultiSelectCell;
import tornadofx.control.behavior.MultiSelectCellBehavor;

public class MultiSelectCellSkin<E> extends CellSkinBase<MultiSelectCell<E>, MultiSelectCellBehavor<E>> {
	public MultiSelectCellSkin(MultiSelectCell<E> control) {
		super(control, new MultiSelectCellBehavor<>(control));
	}

}
