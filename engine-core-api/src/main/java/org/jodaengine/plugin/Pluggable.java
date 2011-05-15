package org.jodaengine.plugin;

import javax.annotation.Nonnull;

/**
 * This interface should be implemented by pluggable objects.
 * 
 * @param <P>
 *            the plugin implementation class
 */
public interface Pluggable<P extends Plugin> {

    /**
     * Invoking this method will register a certain plugin
     * to be fired as specified by the underlying plugin lifecycle
     * implementation.
     * 
     * @param plugin
     *            the plugin to register
     */
    void registerPlugin(@Nonnull P plugin);

    /**
     * This method will deregister the given plugin from the pluggable. It will not be notified in the future.
     * 
     * @param plugin
     *            the plugin
     */
    void deregisterPlugin(@Nonnull P plugin);
}
