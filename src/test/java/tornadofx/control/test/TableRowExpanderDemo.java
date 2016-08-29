package tornadofx.control.test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tornadofx.control.DatePickerTableCell;
import tornadofx.control.Fieldset;
import tornadofx.control.Form;
import tornadofx.control.TableRowExpander;

import java.time.LocalDate;

@SuppressWarnings("unchecked")
public class TableRowExpanderDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        TableView<Customer> tableView = new TableView<>(FXCollections.observableArrayList(Customer.createSample()));
        tableView.setEditable(true);

        TableColumn<Customer, String> username = new TableColumn<>("Username");
        username.setCellValueFactory(param -> param.getValue().usernameProperty());

        TableColumn<Customer, LocalDate> registered = new TableColumn<>("Registered");
        registered.setCellValueFactory(param -> param.getValue().registeredProperty());
        registered.setCellFactory(DatePickerTableCell.forTableColumn());
        registered.setPrefWidth(150);

        tableView.getColumns().addAll(username, registered);

        TableRowExpander<Customer> expander = TableRowExpander.install(tableView, param -> {
            Customer customer = param.getValue();
            System.out.println("Creating form for customer " + customer.getUsername() + ", cell " + param.getTableRow());
            Form form = new Form();
            form.setPadding(new Insets(20));
            Fieldset fieldset = form.fieldset("Edit customer");

            TextField usernameField = new TextField();
            usernameField.textProperty().bindBidirectional(customer.usernameProperty());
            fieldset.field("Username", usernameField);

            DatePicker registeredField = new DatePicker();
            registeredField.valueProperty().bindBidirectional(customer.registeredProperty());
            fieldset.field("Registered", registeredField);

            Button save = new Button("Save");
            save.setOnAction(event -> param.toggleExpanded());
            fieldset.field(save);
            return form;
        });
/*
        expander.getExpanderColumn().setCellFactory(param -> new TableCell<Customer, Boolean>() {
            private Button button = new Button();

            {
                button.setOnAction(event -> expander.getExpanderColumn().toggleExpanded(getIndex()));
            }

            protected void updateItem(Boolean expanded, boolean empty) {
                super.updateItem(expanded, empty);
                if (expanded == null || empty) {
                    setGraphic(null);
                } else {
                    button.setText(expanded ? "Collapse" : "Expand");
                    setGraphic(button);
                }
            }
        });
*/
        stage.setScene(new Scene(tableView));
        stage.show();
    }
}
