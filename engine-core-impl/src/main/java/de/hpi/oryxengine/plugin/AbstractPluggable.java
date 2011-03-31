package de.hpi.oryxengine.plugin;

import java.util.Observable;

/**
 * This is the abstract representation for pluggable classes.
 * It provides required methods for registering the plugin and
 * implements the observer pattern.
 * 
 * @param <P> the plugin implementation class implemented as observer
 */
public abstract class AbstractPluggable<P extends ObserverPlugin>
extends Observable
implements Pluggable<P> {
    
    @Override
    public void registerPlugin(P plugin) {
        this.addObserver(plugin);
    }
}
