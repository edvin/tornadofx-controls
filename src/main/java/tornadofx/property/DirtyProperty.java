package tornadofx.property;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.lang.reflect.Method;
import java.util.List;

public class DirtyProperty extends ReadOnlyBooleanWrapper {
    private final ObservableList<Property> properties = FXCollections.observableArrayList();
	private final ObservableList<Property> dirtyProperties = FXCollections.observableArrayList();
	private final ObservableList<Property> unmodifiableDirtyProperties =
		FXCollections.unmodifiableObservableList(dirtyProperties);

	private DirtyListener dirtyListener = new DirtyListener();

	public DirtyProperty(Object bean) {
		this(bean, true);
	}

    public DirtyProperty(Object bean, boolean addDeclaredProperties) {
	    super(bean, "dirty", false);
	    monitorChanges();

	    if (addDeclaredProperties)
		    addDeclaredProperties();
    }

    public DirtyProperty(Object bean, List<Property> properties) {
	    super(bean, "dirty", false);
	    monitorChanges();
	    getProperties().addAll(properties);
    }

	private void addDeclaredProperties() {
		Object bean = getBean();
		if (bean == null) return;

		for (Method method : bean.getClass().getDeclaredMethods()) {
			if (method.getName().endsWith("Property") && Property.class.isAssignableFrom(method.getReturnType())
				&& !DirtyProperty.class.isAssignableFrom(method.getReturnType())) {
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
	        if (!dirtyProperties.contains(property))
		        dirtyProperties.add((Property) property);

			if (!isDirty())
				setValue(true);
        }
    }

    public void reset() {
	    dirtyProperties.clear();
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
