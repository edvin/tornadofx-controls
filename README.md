# Tornado FX Controls

CSS Stylable Controls for JavaFX

### Add Tornado FX Controls to your project

```xml
<dependency>
	<groupId>no.tornado</groupId>
	<artifactId>tornadofx-controls</artifactId>
	<version>1.0</version>
</dependency>
```

## ListMenu

A menu that behaves and looks like a typical `ul`/`li` based *HTML5* menu.

![ListMenuDemo](/screenshots/listmenu.gif?raw=true "ListMenuDemo")

 - Configurable `orientation` and `iconPosition`.
 - Track user selection with `active` observable property
 - Pseudo state `active` for CSS styling
 - Custom css property `-fx-graphic-fixed-size` to align icons of different sizes, which are often the case for font based icons.
 - FXML compatible
 - [ListMenuDemo](       https://github.com/edvin/tornadofx-controls/blob/master/src/test/java/tornadofx/control/test/ListMenuDemo.java) source code
 - [Custom CSS Example] (https://github.com/edvin/tornadofx-controls/blob/master/src/test/resources/custom.css)

### Create programatically

```java
ListMenu menu = new ListMenu(
	new ListItem("Contacts", icon(FontAwesomeIcon.USER)),
	new ListItem("Projects", icon(FontAwesomeIcon.SUITCASE)),
	new ListItem("Settings", icon(FontAwesomeIcon.COG))
);

// Listen for selection
menu.activeProperty().addListener((observable, oldValue, newValue) -> {
	// Navigate based on ListItem 'newValue'
});
```

### Create with FXML

```xml
<ListMenu orientation="VERTICAL" iconPosition="LEFT">
	<ListItem active="true" text="Contacts">
		<graphic>
			<FontAwesomeIconView glyphName="USER"/>
		</graphic>
	</ListItem>
	<ListItem text="Projects">
		<graphic>
			<FontAwesomeIconView glyphName="SUITCASE"/>
		</graphic>
	</ListItem>
	<ListItem text="Settings">
		<graphic>
			<FontAwesomeIconView glyphName="COG"/>
		</graphic>
	</ListItem>
</ListMenu>
```

## UnitConverter for TextField (kMGTPE)

Bind a `Long` property to a TextField with `UnitConverter` and you can write *2G* instead of 2147483648.

```java
TextField storageInput = new TextField()
storageInput.textProperty().bindBidirectional(product.sizeProperty(), new UnitConverter())
```

Optionally configure `binary (true/false)` and `separator` (default "").

## LeanPropertyValueFactory

Fancy having public fields for your JavaFX properties instead of public methods in your model objects?
This PropertyValueFactory allows you to use these fields with a TableView:

```java
public class Customer {
	public field idProperty = SimpleObjectProperty<Integer>();
	public field nameProperty = SimpleObjectProperty<String>();
	// Getters and setters if you want :)
}
```

```xml
<TableView>
	<columns>
		<TableColumn text="Id">
			<cellValueFactory>
				<LeanPropertyValueFactory property="id"/>
			</cellValueFactory>
		</TableColumn>
		<TableColumn text="Name">
			<cellValueFactory>
				<LeanPropertyValueFactory property="name"/>
			</cellValueFactory>
		</TableColumn>
	</columns>
</TableView>

```