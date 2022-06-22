package dk.via.remote.observer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.EventListener;

/**
 * A RemotePropertyChangeEvent is fired whenever a property changes on the server.
 * You can register a RemotePropertyChangeListener with the server to receive notifications when the property changes.
 * @param <Value> the type of the old and new values. That is, the type of the property changing.
 */
public interface RemotePropertyChangeListener<Value extends Serializable> extends Remote, EventListener {
    /**
     * This method gets called when the property is changed.
     * @param evt a {@link RemotePropertyChangeEvent} object containing the details of the property change.
     * @throws RemoteException if the network communication fails.
     */
    void propertyChange(RemotePropertyChangeEvent<Value> evt) throws RemoteException;
}
