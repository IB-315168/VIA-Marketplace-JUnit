package dk.via.remote.observer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * This is a utility class that can be used with a server object to support listening remotely to changes.
 * It manages a list of remote listeners and supports firing a property change event to all registered listeners.
 * The class needs to be initialized with a remote object either extending {@link UnicastRemoteObject} or created
 * using {@link UnicastRemoteObject#exportObject(Remote, int)}
 *
 <p>
 * Here is an example of {@code RemotePropertyChangeSupport} usage extending {@code UnicastRemoteObject}:
 * <pre>
 * public class MyService extends UnicastRemoteObject implements ServiceInterface {
 *     private final RemotePropertyChangeSupport pcs;
 *
 *     public MyService() throws RemoteException {
 *         pcs = new RemotePropertyChangeSupport(this);
 *     }
 *
 *   {@literal  @Override}
 *     public void addPropertyChangeListener(RemotePropertyChangeListener listener) throws RemoteException {
 *         this.pcs.addPropertyChangeListener(listener);
 *     }
 *
 *   {@literal  @Override}
 *     public void removePropertyChangeListener(RemotePropertyChangeListener listener) throws RemoteException {
 *         this.pcs.removePropertyChangeListener(listener);
 *     }
 *
 *     private String value;
 *
 *   {@literal  @Override}
 *     public String getValue() throws RemoteException {
 *         return this.value;
 *     }
 *
 *   {@literal  @Override}
 *     public void setValue(String newValue) throws RemoteException {
 *         String oldValue = this.value;
 *         this.value = newValue;
 *         this.pcs.firePropertyChange("value", oldValue, newValue);
 *     }
 *
 *     [...]
 * }
 * </pre>
 * <p>
 *
<p>
 * Here is an example of {@code RemotePropertyChangeSupport} using {@code UnicastRemoteObject.exportObject}:
 * <pre>
 * public class MyService implements ServiceInterface {
 *     private final RemotePropertyChangeSupport pcs;
 *
 *     public MyService(int port) throws RemoteException {
 *         Remote remote = UnicastRemoteObject.exportObject(this, port);
 *         pcs = new RemotePropertyChangeSupport(remote);
 *     }
 *
 *   {@literal  @Override}
 *     public void addPropertyChangeListener(RemotePropertyChangeListener listener) throws RemoteException {
 *         this.pcs.addPropertyChangeListener(listener);
 *     }
 *
 *   {@literal  @Override}
 *     public void removePropertyChangeListener(RemotePropertyChangeListener listener) throws RemoteException {
 *         this.pcs.removePropertyChangeListener(listener);
 *     }
 *
 *     private String value;
 *
 *   {@literal  @Override}
 *     public String getValue() throws RemoteException {
 *         return this.value;
 *     }
 *
 *   {@literal  @Override}
 *     public void setValue(String newValue) throws RemoteException {
 *         String oldValue = this.value;
 *         this.value = newValue;
 *         this.pcs.firePropertyChange("value", oldValue, newValue);
 *     }
 *
 *     [...]
 * }
 * </pre>
 * <p>
 * A {@code RemotePropertyChangeSupport} instance is thread-safe.
 *
 * @param <Value> the type of the old and new values. That is, the type of the property changing.
 * @see RemotePropertyChangeListener
 * @see RemotePropertyChangeEvent
 */
public class RemotePropertyChangeSupport<Value extends Serializable> {
    private static class ListenerProxy<Value extends Serializable> {
        private final String propertyName;
        private final RemotePropertyChangeListener<Value> listener;

        public ListenerProxy(String propertyName, RemotePropertyChangeListener<Value> listener) {
            this.propertyName = propertyName;
            this.listener = listener;
        }

        public ListenerProxy(RemotePropertyChangeListener<Value> listener) {
            this(null, listener);
        }

        public boolean acceptsProperty(String propertyName) {
            return this.propertyName == null || this.propertyName.equals(propertyName);
        }

        public void propertyChange(RemotePropertyChangeEvent<Value> evt) throws RemoteException {
            if (acceptsProperty(evt.getPropertyName())) {
                listener.propertyChange(evt);
            }
        }

        public RemotePropertyChangeListener<Value> getListener() {
            return listener;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListenerProxy<?> that = (ListenerProxy<?>) o;
            return Objects.equals(propertyName, that.propertyName) && listener.equals(that.listener);
        }

        @Override
        public int hashCode() {
            return Objects.hash(propertyName, listener);
        }
    }

    private final UnicastRemoteObject sourceBean;
    private final LinkedList<ListenerProxy<Value>> listeners;

    /**
     * Creates a new {@code RemotePropertyChangeSupport} object.
     *
     * @param sourceBean the object that might change properties.
     */
    public RemotePropertyChangeSupport(UnicastRemoteObject sourceBean) {
        this.sourceBean = sourceBean;
        this.listeners = new LinkedList<>();
    }

    /**
     * Adds a {@link RemotePropertyChangeListener} property change listener to the listener list. The listener will be
     * notified whenever any property changes value.
     *
     * @param listener the listener to be added.
     */
    public synchronized void addPropertyChangeListener(RemotePropertyChangeListener<Value> listener) {
        listeners.add(new ListenerProxy<>(listener));
    }

    /**
     * Adds a {@link RemotePropertyChangeListener} property change listener for a specific property. The listener will be
     * notified whenever the property with the provided name changes value.
     *
     * @param propertyName the property to listen for changes in. If {@code null}, the listener will not be added.
     * @param listener the listener to be added.
     */
    public synchronized void addPropertyChangeListener(String propertyName, RemotePropertyChangeListener<Value> listener) {
        if (propertyName == null) return;
        listeners.add(new ListenerProxy<>(propertyName, listener));
    }

    /**
     * Removes a {@link RemotePropertyChangeListener} property change listener from the listener list.
     * If the listener was never added or added for a specific property name, the call has no effect.
     *
     * @param listener the listener to be removed.
     */
    public synchronized void removePropertyChangeListener(RemotePropertyChangeListener<Value> listener) {
        listeners.remove(new ListenerProxy<>(listener));
    }

    /**
     * Removes a {@link RemotePropertyChangeListener} property change listener for a specific property.
     * If the listener was never added or not added for this specific property, the call has no effect.
     *
     * @param propertyName the property to listen for changes in. If {@code null}, the listener will not be added.
     * @param listener the listener to be added.
     */
    public synchronized void removePropertyChangeListener(String propertyName, RemotePropertyChangeListener<Value> listener) {
        if (propertyName == null) return;
        listeners.remove(new ListenerProxy<>(propertyName, listener));
    }

    /**
     * Notifies the relevant listeners that a property has changed.
     * The relevant listeners are listeners added with no property name specified or added to listen on
     * the specific property that has changed.
     *
     * @param evt The details of the property that has changed.
     * @throws RemoteException if a network error happens.
     */
    public void firePropertyChange(RemotePropertyChangeEvent<Value> evt) throws RemoteException {
        if (Objects.equals(evt.getOldValue(), evt.getNewValue())) return;
        synchronized(this) {
            for(ListenerProxy<Value> proxy: listeners) {
                proxy.propertyChange(evt);
            }
        }
    }

    /**
     * Notifies the relevant listeners that a property has changed.
     * The relevant listeners are listeners added with no property name specified or added to listen on
     * the specific property that has changed.
     *
     * @param propertyName the name of the property that has changed.
     * @param oldValue the old value of the property, or null if no old value is available.
     * @param newValue the new value of the property.
     * @throws RemoteException if a network error occurs.
     */
    public void firePropertyChange(String propertyName, Value oldValue, Value newValue) throws RemoteException {
        firePropertyChange(new RemotePropertyChangeEvent<>(sourceBean, propertyName, oldValue, newValue));
    }

    @SuppressWarnings("unchecked")
    private RemotePropertyChangeListener<Value>[] toArray(Stream<ListenerProxy<Value>> stream) {
        return stream.map(ListenerProxy::getListener).toArray(RemotePropertyChangeListener[]::new);
    }

    /**
     * Returns an array of all listeners added to the {@code RemotePropertyChangeSupport}.
     * The array contains all listeners regardless of whether they were added for a specific property.
     *
     * @return all listeners added to the {@code RemotePropertyChangeSupport}.
     */
    public RemotePropertyChangeListener<Value>[] getPropertyChangeListeners() {
        return toArray(listeners.stream());
    }

    /**
     * Returns an array of all listeners added to the {@code RemotePropertyChangeSupport} for the specified property.
     *
     * @param propertyName the name of the specified property.
     * @return all listeners added to the {@code RemotePropertyChangeSupport} for the specified property.
     */
    public RemotePropertyChangeListener<Value>[] getPropertyChangeListeners(String propertyName) {
        return toArray(listeners.stream().filter(proxy -> proxy.acceptsProperty(propertyName)));
    }

    /**
     * Check if there are any listeners for a specific property, including
     * those registered on all properties.  If {@code propertyName}
     * is null, only check for listeners registered on all properties.
     *
     * @param propertyName  the property name.
     * @return true if there are one or more listeners for the given property
     */
    public boolean hasListeners(String propertyName) {
        return listeners.stream().anyMatch(proxy -> proxy.acceptsProperty(propertyName));
    }
}
