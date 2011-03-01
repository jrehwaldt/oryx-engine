package de.hpi.oryxengine.plugin.navigator;

import java.util.Observable;
import java.util.Observer;

import javax.annotation.Nonnull;

/**
 * This class may be injected to observe
 * the lifecycle of a navigator thread.
 */
public abstract class AbstractNavigatorListener
implements NavigatorListener, Observer {
    
    /**
     * This method is called whenever the navigator changes its state.
     * 
     * {@inheritDoc}
     */
    @Override
    public void update(@Nonnull Observable observable,
                       @Nonnull Object param) {
        
        
    }
}
