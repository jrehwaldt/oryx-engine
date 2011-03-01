package de.hpi.oryxengine.plugin.navigator;

import java.util.Observable;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorState;
import de.hpi.oryxengine.plugin.ObserverPlugin;

/**
 * This class may be injected to observe
 * the lifecycle of a navigator.
 */
public abstract class AbstractNavigatorListener
implements ObserverPlugin, NavigatorListener {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * This method is called whenever the navigator changes its state.
     * 
     * {@inheritDoc}
     */
    @Override
    public void update(@Nonnull Observable observable,
                       @Nonnull Object param) {
        final NavigatorState state = (NavigatorState) param;
        final Navigator navigator = (Navigator) observable;
        
        switch (state) {
            case RUNNING:
                navigatorStarted(navigator);
                break;
            case STOPPED:
                navigatorStopped(navigator);
                break;
            default:
                logger.error("NavigatorPlugin fired for unknown state");
                break;
        }
    }
}
