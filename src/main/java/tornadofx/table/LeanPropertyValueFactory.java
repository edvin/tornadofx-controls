package tornadofx.table;

import javafx.beans.NamedArg;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.lang.reflect.Field;

/**
 * A PropertyValueFactory that looks up the value using a field with the same name as the property, as opposed
 * to a method with the same name. Eg. the public field Customer.idProperty will be resolved via "id".
 *
 * @see javafx.scene.control.cell.PropertyValueFactory
 *
 * @param <S> The type of object for the table
 * @param <T> The type of object for the column
 */
@SuppressWarnings("unchecked")
public class LeanPropertyValueFactory<S, T> implements Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> {
    private final String property;
    private Field field;

    public LeanPropertyValueFactory(@NamedArg("property") String property) {
        this.property = property;
    }

    public ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> param) {
        if (param != null && param.getValue() != null) {
            if (field == null) {
                try {
                    field = param.getValue().getClass().getDeclaredField(property + "Property");
                    if (!field.isAccessible())
                        field.setAccessible(true);
                } catch (NoSuchFieldException e) {
                    throw new IllegalArgumentException(String.format("No field named %s in %s", property, param.getValue().getClass()));
                }
            }

            try {
                return (ObservableValue<T>) field.get(param.getValue());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(String.format("Unable to extract value from field %s", field.getName()), e);
            }
        }

        return null;
    }
}
