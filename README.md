# Tornado FX Controls

CSS Stylable Controls for JavaFX

## ListMenu

A menu that behaves like a typical `ul`/`li` based *HTML5* menu.
 
 - Configurable `orientation` and `iconPosition`.
 - Track user selection with `active` observable property
 - Custom css property `-fx-graphic-fixed-size` to align icons of different sizes, which are often the case for font based icons.
 - [ListMenuDemo](https://github.com/edvin/tornadofx-controls/blob/master/src/test/java/tornadofx/control/test/ListMenuDemo.java) available in the source code
 - [Custom CSS Example] (https://github.com/edvin/tornadofx-controls/blob/master/src/test/resources/custom.css)
  
[[https://github.com/edvin/tornadofx-controls/blob/master/screenshots/listmenu.png|alt=ListMenuDemo]]
 
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
