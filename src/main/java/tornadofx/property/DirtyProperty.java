package tornadofx.property;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class DirtyProperty extends SimpleObjectProperty<Boolean> {
    private Boolean dirty = false;
    private ObservableList<Property> observables = FXCollections.observableArrayList();

    public DirtyProperty() {
        addDirtyListeners();
    }

    private void addDirtyListeners() {
        ChangeListener cl = (observable, oldValue, newValue) -> {

        };

        observables.addListener((ListChangeListener<Property>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {

                }

                if (change.wasRemoved()) {

                }
            }
        });
    }

    private static class DirtyListener implements ChangeListener {
        public void changed(ObservableValue property, Object oldValue, Object newValue) {

        }
    }

    public void reset() {

    }

    public Boolean isDirty() {
        return dirty;
    }
}
