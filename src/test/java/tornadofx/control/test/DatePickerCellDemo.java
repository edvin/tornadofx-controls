package tornadofx.control.test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import tornadofx.control.DatePickerTableCell;

import java.time.LocalDate;

public class DatePickerCellDemo extends Application {
    public void start(Stage stage) throws Exception {
        TableView<Customer> tableView = new TableView<>(FXCollections.observableArrayList(Customer.createSample(555)));
        tableView.setEditable(true);

        TableColumn<Customer, String> username = new TableColumn<>("Username");
        username.setCellValueFactory(param -> param.getValue().usernameProperty());

        TableColumn<Customer, LocalDate> registered = new TableColumn<>("Registered");
        registered.setCellValueFactory(param -> param.getValue().registeredProperty());
        registered.setCellFactory(DatePickerTableCell.forTableColumn());
        registered.setPrefWidth(150);

        tableView.getColumns().addAll(username, registered);

        stage.setScene(new Scene(tableView, 800, 600));
        stage.show();
    }
}
