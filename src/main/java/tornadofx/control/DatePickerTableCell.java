package tornadofx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class DatePickerTableCell<S> extends TableCell<S, LocalDate> {
    private ObjectProperty<StringConverter<LocalDate>> converter = new SimpleObjectProperty<>(this, "converter");
    private DatePicker datePicker;

    public DatePickerTableCell(StringConverter<LocalDate> converter) {
        setConverter(converter);
        this.getStyleClass().add("datepicker-table-cell");
    }

    public static <S> Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> forTableColumn() {
        return column -> new DatePickerTableCell<>(new DefaultLocalDateConverter());
    }

    public static <S> Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> forTableColumn(StringConverter<LocalDate> converter) {
        return column -> new DatePickerTableCell<>(converter);
    }

    public void startEdit() {
        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable())
            return;

        super.startEdit();

        if (isEditing()) {
            if (datePicker == null)
                createDatePicker();

            setText(null);
            setGraphic(datePicker);
            datePicker.requestFocus();
        }
    }

    public void cancelEdit() {
        super.cancelEdit();
        setText(getItemText());
        setGraphic(null);
    }

    private String getItemText() {
        return getConverter().toString(getItem());
    }

    private void createDatePicker() {
        datePicker = new DatePicker(getItem());
        datePicker.converterProperty().bind(converterProperty());

        datePicker.setOnAction(event -> {
            commitEdit(datePicker.getValue());
            event.consume();
        });

        datePicker.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                t.consume();
            }
        });
    }

    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        if (isEmpty()) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                datePicker.setValue(getItem());
                setText(null);
                setGraphic(datePicker);
            } else {
                setText(getItemText());
                setGraphic(null);
            }
        }
    }

    public StringConverter<LocalDate> getConverter() {
        return converter.get();
    }

    public ObjectProperty<StringConverter<LocalDate>> converterProperty() {
        return converter;
    }

    public void setConverter(StringConverter<LocalDate> converter) {
        this.converter.set(converter);
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

    public static class DefaultLocalDateConverter extends StringConverter<LocalDate> {
        public String toString(LocalDate date) {
            return date != null ? date.toString() : "";
        }

        public LocalDate fromString(String string) {
            return string == null || string.isEmpty() ? null : LocalDate.parse(string);
        }
    }
}
