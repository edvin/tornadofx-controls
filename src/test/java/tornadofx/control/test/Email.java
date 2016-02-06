package tornadofx.control.test;

public class Email {
	private String email;
	private String name;

	public Email(String email, String name) {
		this.email = email;
		this.name = name;
	}

	public String toString() {
		return name == null ? email : name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
