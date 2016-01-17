# Tornado FX Controls

CSS Stylable Controls for JavaFX

## ListMenu

A menu that behaves and looks like a typical `ul`/`li` based *HTML5* menu.

 ![ListMenuDemo](/screenshots/listmenu.gif?raw=true "ListMenuDemo")

 - Configurable `orientation` and `iconPosition`.
 - Track user selection with `active` observable property
 - Pseudo state `active` for CSS styling
 - Custom css property `-fx-graphic-fixed-size` to align icons of different sizes, which are often the case for font based icons.
 - [ListMenuDemo](       https://github.com/edvin/tornadofx-controls/blob/master/src/test/java/tornadofx/control/test/ListMenuDemo.java) source code
 - [Custom CSS Example] (https://github.com/edvin/tornadofx-controls/blob/master/src/test/resources/custom.css)

```java
ListMenu menu = new ListMenu(
	new ListItem("Contacts", icon(FontAwesomeIcon.USER)),
	new ListItem("Projects", icon(FontAwesomeIcon.SUITCASE)),
	new ListItem("Settings", icon(FontAwesomeIcon.COG))
);

menu.activeProperty().addListener((observable, oldValue, newValue) -> {
	// Navigate based on ListItem 'newValue'
});
```
