package tornadofx.property;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Track dirty states for a collection of properties, with undo feature to rollback changes.</p>
 *
 * <p>Create an instance of DirtyStateTracker with a reference to the bean that holds the properties
 * you want to track changes in. All puplic properties are automatically tracked, unless you
 * supply an explicit list of properties to track.</p>
 *
 * <pre>
 *     // Track all properties in customer
 *     DirtyStateTracker dirtyState = new DirtyStateTracker(customer);
 *
 *     // Track only username and password
 *     DirtyStateTracker dirtyState = new DirtyStateTracker(customer,
 *          customer.usernameProperty(), customer.passwordProperty());
 * </pre>
 *
 * <p>The list of currently dirty properties can be obtained via {@code DirtyStateTracker#getUnmodifiableDirtyProperties()}.</p>
 *
 * <p>The {@code DirtyStateTracker} itself is a {@code ReadOnlyProperty} you can use to track if any of the properties are dirty. A good
 * use case for this is for example conditionally enabling of a save button:</p>
 *
 * <pre>
 *     // Disable save button until anything is changed
 *     saveButton.disableProperty().bind(dirtyState.not())
 * </pre>
 *
 * <p>To roll back any changes, call {@code DirtyStateTracker#reset()}.</p>
 *
 * <p>To reset dirty state and clear the rollback buffer, call {@code DirtyStateTracker#undo()}.</p>
 *
 * <pre>
 *     // Undo changes
 *     undoButton.setOnAction(event -> dirtyState.reset());
 *
 *     // Show undo button when changes are performed
 *     undoButton.visibleProperty().bind(dirtyState);
 * </pre>
 *
 * <p>It's possible to change the tracked properties after the tracker is created. Let's say you want to
 * listen to all properties but the id property of a customer object:</p>
 *
 * <pre>
 *     // Track all but the id property
 *     DirtyStateTracker dirtyState = new DirtyStateTracker(customer);
 *     dirtyState.getProperties().remove(customer.idProperty());
 * </pre>
 */
public class DirtyStateTracker extends ReadOnlyBooleanWrapper {
    private final ObservableList<Property> properties = FXCollections.observableArrayList();
	private final ObservableList<Property> dirtyProperties = FXCollections.observableArrayList();
	private final ObservableList<Property> unmodifiableDirtyProperties = FXCollections.unmodifiableObservableList(dirtyProperties);
	private final Map<Property, Object> initialValues = new HashMap<>();

	private DirtyListener dirtyListener = new DirtyListener();

	public DirtyStateTracker(Object bean) {
		this(bean, true);
	}

    public DirtyStateTracker(Object bean, boolean addDeclaredProperties) {
	    super(bean, "dirty", false);
	    monitorChanges();

	    if (addDeclaredProperties)
		    addDeclaredProperties();
    }

    public DirtyStateTracker(Object bean, List<Property> properties) {
	    super(bean, "dirty", false);
	    monitorChanges();
	    getProperties().addAll(properties);
    }

	private void addDeclaredProperties() {
		Object bean = getBean();
		if (bean == null) return;

		for (Method method : bean.getClass().getDeclaredMethods()) {
			if (method.getName().endsWith("Property") && Property.class.isAssignableFrom(method.getReturnType())
				&& !DirtyStateTracker.class.isAssignableFrom(method.getReturnType())) {
				try {
					Property property = (Property) method.invoke(bean);
					if (property != null)
						properties.add(property);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	private void monitorChanges() {
        properties.addListener((ListChangeListener<Property>) change -> {
            while (change.next()) {
                if (change.wasAdded()) change.getAddedSubList().forEach(p -> p.addListener(dirtyListener));
                if (change.wasRemoved()) change.getRemoved().forEach(p -> p.removeListener(dirtyListener));
            }
        });
    }

	private class DirtyListener implements ChangeListener {
        @SuppressWarnings("SuspiciousMethodCalls")
        public void changed(ObservableValue property, Object oldValue, Object newValue) {
	        if (!dirtyProperties.contains(property)) {
		        initialValues.put((Property) property, oldValue);
		        dirtyProperties.add((Property) property);
	        }

			if (!isDirty())
				setValue(true);
        }
    }

    public void reset() {
	    dirtyProperties.clear();
	    initialValues.clear();
		setValue(false);
    }

	public void undo() {
		initialValues.forEach(WritableValue::setValue);
		setValue(false);
	}

    public Boolean isDirty() {
        return getValue();
    }

	public ObservableList<Property> getProperties() {
		return properties;
	}

	public ObservableList<Property> getUnmodifiableDirtyProperties() {
		return unmodifiableDirtyProperties;
	}
}
