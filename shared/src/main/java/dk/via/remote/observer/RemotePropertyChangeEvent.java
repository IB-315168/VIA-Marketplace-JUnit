package dk.via.remote.observer;

import java.io.Serializable;

/**
 * A RemotePropertyChangeEvent is fired whenever a property changes on the server.
 *
 * A change event is accompanied by the name of the property changing as well as the old and new value of the property.
 * The old value may be null to indicate that there is no meaningful way to indicate the old value.
 *
 * @param <Value> the type of the old and new values. That is, the type of the property changing.
 */
public class RemotePropertyChangeEvent<Value extends Serializable> implements Serializable {
    private final Serializable source;
    private final String propertyName;
    private final Value oldValue;
    private final Value newValue;

    /**
     * Creates a new RemotePropertyChangeEvent object.
     *
     * @param source the object that has changed a property. The object needs to be Serializable - typically a remote object.
     * @param propertyName the name of the property that has changed.
     * @param oldValue the old value of the property or null if such a value is meaningless.
     * @param newValue the new value of the property.
     */
    public RemotePropertyChangeEvent(Serializable source, String propertyName, Value oldValue, Value newValue) {
        this.source = source;
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * The object that has changed a property.
     * @return the object that has changed a property.
     */
    public Serializable getSource() {
        return source;
    }

    /**
     * The name of the property that has changed.
     * @return the name of the property that has changed.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * The old value of the property or null if such a value is meaningless.
     * @return the old value of the property or null if such a value is meaningless.
     */
    public Value getOldValue() {
        return oldValue;
    }

    /**
     * The new value of the property.
     * @return the new value of the property.
     */
    public Value getNewValue() {
        return newValue;
    }
}
