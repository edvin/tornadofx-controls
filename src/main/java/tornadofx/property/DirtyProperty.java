package tornadofx.property;

import javafx.beans.property.SimpleObjectProperty;

public class DirtyProperty extends SimpleObjectProperty<Boolean> {
    private Boolean dirty = false;

    public void reset() {

    }

    public Boolean isDirty() {
        return dirty;
    }
}
