package tornadofx.control.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tornadofx.control.MultiSelect;

import java.util.Arrays;
import java.util.List;

public class MultiSelectDemo extends Application {
	private static final List<Email> addresses = Arrays.asList(
		new Email("es@syse.no", "Edvin Syse"),
		new Email("bj@syse.no", "Bård Johannessen"),
		new Email("of@syse.no", "Øyvind Frøland")
	);

	public void start(Stage stage) throws Exception {
		MultiSelect<Email> multiSelect = new MultiSelect<>();

		multiSelect.setConverter(new StringConverter<Email>() {
			public String toString(Email object) {
				return object.email;
			}

			public Email fromString(String string) {
				return new Email(string, null);
			}
		});

		multiSelect.getItems().addAll(addresses);

		stage.setScene(new Scene(multiSelect, 600, 400));
		stage.show();
	}


	static class Email {
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
}
