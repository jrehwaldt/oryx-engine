package org.jodaengine.ext;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jodaengine.ext.util.TypeSafeInstanceMap;

/**
 * This is the abstract representation for {@link Extensible} classes.
 * 
 * It provides required methods for registering and unregistering listeners
 * and handlers.
 */
public abstract class AbstractExtensible implements Extensible {
    
    private TypeSafeInstanceMap listeners = new TypeSafeInstanceMap();
    
    @Override
    public <Listener> void registerListeners(Class<Listener> type,
                                             List<Listener> listeners) {
        
        if (!supportsExtension(type)) {
            throw new IllegalArgumentException("Type not supported");
        }
        
        this.listeners.addInstances(type, listeners);
    }

    @Override
    public <Listener> void registerListeners(Class<Listener> type,
                                             Listener ... listeners) {
        
        if (!supportsExtension(type)) {
            throw new IllegalArgumentException("Type not supported");
        }
        
        this.listeners.addInstances(type, listeners);
    }
    
    @Override
    public <Listener> void clearListeners(Class<Listener> type) {
        
        if (!supportsExtension(type)) {
            throw new IllegalArgumentException("Type not supported");
        }
        
        this.listeners.clearInstances(type);
    }

    @Override
    @JsonIgnore
    public <Listener> List<Listener> getListeners(Class<Listener> type) {
        
        if (!supportsExtension(type)) {
            throw new IllegalArgumentException("Type not supported");
        }
        
        return this.listeners.getInstances(type);
    }

}
