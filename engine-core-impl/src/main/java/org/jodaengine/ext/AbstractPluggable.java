package org.jodaengine.ext;

import java.util.Observable;

import org.jodaengine.plugin.Pluggable;

/**
 * This is the abstract representation for pluggable classes.
 * It provides required methods for registering the plugin and
 * implements the observer pattern.
 * 
 * @param <P> the plugin implementation class implemented as observer
 */
public abstract class AbstractPluggable<P extends ObserverListener>
extends Observable
implements Pluggable<P> {
    
    @Override
    public void registerPlugin(P plugin) {
        this.addObserver(plugin);
    }
    
    @Override
    public void deregisterPlugin(P plugin) {
        this.deleteObserver(plugin);
    }
}
