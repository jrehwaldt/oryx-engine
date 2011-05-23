package org.jodaengine.ext.listener;

import java.util.List;

import javax.annotation.Nonnull;


/**
 * This interface should be implemented by pluggable objects.
 * 
 * @param <L>
 *            the listener implementation class
 */
public interface Listenable<L extends Listener> {

    /**
     * Invoking this method will register a certain listener
     * to be fired as specified by the underlying listener lifecycle
     * implementation.
     * 
     * @param listener
     *            the listener to register
     */
    void registerListener(@Nonnull L listener);

    /**
     * Invoking this method will register any number of listeners
     * to be fired as specified by the underlying listener lifecycle
     * implementation.
     * 
     * @param listeners
     *            the listeners to register
     */
    void registerListeners(@Nonnull List<L> listeners);

    /**
     * This method will deregister the given listener from the extension point. It will not be notified in the future.
     * 
     * @param listener
     *            the listener
     */
    void deregisterListener(@Nonnull L listener);
    
    /**
     * Returns the list of listeners.
     * 
     * @return the listeners.
     */
    @Nonnull List<L> getListeners();
}
