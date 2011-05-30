package org.jodaengine.ext;

import java.util.List;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * This interface should be implemented by classes, which are extensible via
 * listeners (observers) or handlers.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
interface Extensible {
    
    /**
     * Adds any number of listener instances.
     * 
     * @param <Listener> the listener's type
     * @param type the type
     * @param listeners any number of listeners
     */
    <Listener> void registerListeners(@Nonnull Class<Listener> type,
                                      List<Listener> listeners);
    
    /**
     * Adds any number of listener instances.
     * 
     * @param <Listener> the listener's type
     * @param type the type
     * @param listeners any number of listeners
     */
    <Listener> void registerListeners(@Nonnull Class<Listener> type,
                                      @Nonnull Listener ... listeners);
    
    /**
     * Removes all listeners registered for a certain type.
     * 
     * @param <Listener> the listener's type
     * @param type the type
     */
    <Listener> void clearListeners(@Nonnull Class<Listener> type);
    
    /**
     * Returns any listeners registered for a certain type.
     * 
     * @param <Listener> the listener's type
     * @param type the type
     * @return any number of listeners
     */
    @JsonIgnore
    <Listener> List<Listener> getListeners(@Nonnull Class<Listener> type);
    
    /**
     * Returns whether the implementation supports this extension type.
     * 
     * @param type the type
     * @return true, if it is supported
     */
    boolean supportsExtension(@Nonnull Class<?> type);
}
