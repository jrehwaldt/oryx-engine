package org.jodaengine.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.jodaengine.ext.listener.Listenable;

/**
 * This is the abstract representation for {@link Listenable} classes.
 * It provides required methods for registering the ObersverListener and
 * implements the observer pattern.
 * 
 * @param <IListener> the extension implementation class implemented as observer
 */
public abstract class AbstractPluggable<IListener extends ObersverListener>
extends Observable
implements Listenable<IListener> {
    
    private List<IListener> listeners;
    
    /**
     * Default constructor.
     */
    public AbstractPluggable() {
        this.listeners = new ArrayList<IListener>();
    }
    
    @Override
    public void registerListener(IListener listener) {
        this.listeners.add(listener);
        this.addObserver(listener);
    }
    
    @Override
    public void registerListeners(List<IListener> listeners) {
        
        for (IListener listener: listeners) {
            registerListener(listener);
        }
    }
    
    @Override
    public void deregisterListener(IListener listener) {
        this.listeners.remove(listener);
        this.deleteObserver(listener);
    }
    
    @Override
    public List<IListener> getListeners() {
        return this.listeners;
    }
}
