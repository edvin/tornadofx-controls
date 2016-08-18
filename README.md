# Tornado FXControls

CSS Stylable Controls for JavaFX

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/no.tornado/tornadofx-controls/badge.svg)](https://search.maven.org/#search|ga|1|no.tornado.tornadofx-controls)

## DatePickerTableCell

TableCell that supports editing of `LocalDate` properties with a DatePicker.
 
## DateTimePicker

An extension to the JavaFX DatePicker control that lets you input the the time and date in with a configurable format
and operate on a `LocalDateTime` value.

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

### NaviSelect

A chooser with a navigate-to button that lets you select an entry and also edit it. The select mechanism must be implemented manually,
for example by opening a dialog with a search field and a list view.

![](http://i.imgur.com/IvfGVuw.png)

Typical use case can be selecting a customer, but the complete customer list cannot be loaded into the dropdown, so you need to
open a dialog to choose. The navigate-to button would take you to a customer edit screen.

```java
Customer customer = customerService.getCustomer(42);
NaviSelect<Customer> navi = new NaviSelect<>();
navi.setValue(customer);
navi.setOnEdit(event -> {
    // Show dialog to search for customers, when done, set the new value into the navi
    navi.setValue(newlySelectedCustomer);
});
```

To override how the customer is rendered in the dropdown you can configure the visual converter:

```java
navi.setVisualConverter(Customer::getName);
```

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
## Form layout

![Form](/screenshots/form.png?raw=true "Form")

A CSS stylable Form layout that is very convenient to use both with FXML and in code.
 
### FXML Example
 
```xml
<Form>
    <Fieldset text="Contact Information" inputGrow="SOMETIMES">
        <Field text="Id">
            <TextField />
        </Field>
        <Field text="Username">
            <TextField />
        </Field>
        <Field text="Zip/City">
            <TextField minWidth="80" maxWidth="80" />
            <TextField />
        </Field>
        <Field>
            <Button text="Save"/>
        </Field>
    </Fieldset>
</Form> 
```
 
### Java Example
 
```java
Form form = new Form();

Fieldset contactInfo = form.fieldset("Contact Information");

contactInfo.field("Id", new TextField());
contactInfo.field("Username", new TextField());

TextField zipInput = new TextField();
zipInput.setMinWidth(80);
zipInput.setMaxWidth(80);
contactInfo.field("Zip/City", zipInput, new TextField());

contactInfo.field(new Button("Save"));
```

### Responsive layout

A fieldset can lay out it's labels on the same line as the inputs (`orientation=HORIZONTAL`)
or the labels can be laid out above the inputs (`orientation=VERTICAL`).

By changing the `wrapWidth` property on a fieldset you can make a fieldset switch from `HORIZONTAL`
 to `VERTICAL` automatically when the form is resized to a smaller width than `wrapWidth`.
 
### Legends

A fieldset can have an optional icon specified for it's legend by setting the `icon` property
to any node.

### CSS

Use the default [CSS Stylesheet](https://github.com/edvin/tornadofx-controls/blob/master/src/main/resources/tornadofx/control/form.css)
as a starting point. The substructure of a `Form` is described below.

#### Substructure


- form - VBox
	- fieldset - VBox
		- legend - Label
		- field - Field
			- label-container - HBox
				- label - Label
			- input-container - HBox
				- arbitrary input components

## UnitConverter for TextField (kMGTPE)

Bind a `Long` property to a TextField with `UnitConverter` and you can write *2G* instead of 2147483648.

```java
TextField storageInput = new TextField()
storageInput.textProperty().bindBidirectional(product.sizeProperty(), new UnitConverter())
```

Optionally configure `binary (true/false)` and `separator` (default "").

## DirtyState Tracker

Track dirty states for a collection of properties, with undo feature to rollback changes.

```java
// Track all properties in customer
DirtyState dirtyState = new DirtyState(customer);

// Track only username and password
DirtyStateTracker dirtyState = new DirtyStateTracker(customer,
	customer.usernameProperty(), customer.passwordProperty());

// Disable save button until anything is changed
saveButton.disableProperty().bind(dirtyState.not())

// Undo changes
undoButton.setOnAction(event -> dirtyState.undo());

// Show undo button when changes are performed
undoButton.visibleProperty().bind(dirtyState);
```

See the JavaDoc for more information and options. 

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


## Installation

### Maven
```xml
<dependency>
	<groupId>no.tornado</groupId>
	<artifactId>tornadofx-controls</artifactId>
	<version>1.0.4</version>
</dependency>
```

### Gradle

```groovy
compile 'no.tornado:tornadofx-controls:1.0.4'
```
