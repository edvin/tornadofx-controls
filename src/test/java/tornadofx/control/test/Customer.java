package tornadofx.control.test;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import tornadofx.property.DirtyProperty;

public class Customer {
	private SimpleIntegerProperty id = new SimpleIntegerProperty(this, "id");
	private SimpleStringProperty username = new SimpleStringProperty(this, "username");
	private SimpleStringProperty zip = new SimpleStringProperty(this, "zip");
	private SimpleStringProperty city = new SimpleStringProperty(this, "city");

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

	private DirtyProperty dirty;

	public boolean getDirty() {
		return dirty != null && dirty.get();
	}

	public DirtyProperty dirtyProperty() {
		if (dirty == null)
			dirty = new DirtyProperty(this);

		return dirty;
	}

	public static Customer createSample() {
		Customer customer = new Customer();
		customer.setId(555);
		customer.setUsername("john@doe.com");
		customer.setZip("10001");
		customer.setCity("New York");
		return customer;
	}
}
