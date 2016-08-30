package tornadofx.control.test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Customer {
	private SimpleIntegerProperty id = new SimpleIntegerProperty(this, "id");
	private SimpleStringProperty username = new SimpleStringProperty(this, "username");
	private SimpleStringProperty zip = new SimpleStringProperty(this, "zip");
	private SimpleStringProperty city = new SimpleStringProperty(this, "city");
	private ObjectProperty<LocalDate> registered = new SimpleObjectProperty<>(this, "registered");

	public int getId() {
		return id.get();
	}

	public SimpleIntegerProperty idProperty() {
		return id;
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public String getUsername() {
		return username.get();
	}

	public SimpleStringProperty usernameProperty() {
		return username;
	}

	public void setUsername(String username) {
		this.username.set(username);
	}

	public String getZip() {
		return zip.get();
	}

	public SimpleStringProperty zipProperty() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip.set(zip);
	}

	public String getCity() {
		return city.get();
	}

	public SimpleStringProperty cityProperty() {
		return city;
	}

	public void setCity(String city) {
		this.city.set(city);
	}

    public LocalDate getRegistered() {
        return registered.get();
    }

    public ObjectProperty<LocalDate> registeredProperty() {
        return registered;
    }

    public void setRegistered(LocalDate registered) {
        this.registered.set(registered);
    }

    public static Customer createSample(Integer id) {
		Customer customer = new Customer();
		customer.setId(id);
		customer.setUsername("john@doe.com");
		customer.setZip("10001");
		customer.setCity("New York");
        customer.setRegistered(LocalDate.now().minusYears(2));
		return customer;
	}
}
