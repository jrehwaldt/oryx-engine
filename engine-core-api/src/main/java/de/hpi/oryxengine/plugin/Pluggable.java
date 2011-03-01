package de.hpi.oryxengine.plugin;

import javax.annotation.Nonnull;

/**
 * This interface should be implemented by pluggable objects.
 *
 * @param <P> the plugin implementation class
 */
public interface Pluggable<P extends Plugin> {
    
    /**
     * Invoking this method will register a certain plugin
     * to be fired as specified by the underlying plugin lifecycle
     * implementation.
     * 
     * @param plugin the plugin to register
     */
    void registerPlugin(@Nonnull P plugin);
}
