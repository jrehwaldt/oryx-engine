package de.hpi.oryxengine.plugin.navigator;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorState;

/**
 * A logger implementation, which observes the state of a navigator thread.
 */
public final class NavigatorListenerLogger
extends AbstractNavigatorListener {
    
    private static NavigatorListenerLogger instance;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Hide singleton constructor.
     */
    private NavigatorListenerLogger() {
        
    }
    
    /**
     * Returns a lazily initialized logger instance.
     * 
     * @return a logger instance
     */
    public synchronized static @Nonnull NavigatorListenerLogger getInstance() {
        if (instance == null) {
            instance = new NavigatorListenerLogger();
        }
        
        return instance;
    }
    
    @Override
    protected void stateChanged(@Nonnull Navigator navigator, NavigatorState navState) {
        log(navigator);
    }
    
    /**
     * Intern method to log a state change.
     * 
     * @param navigator the navigator
     */
    private void log(@Nonnull Navigator navigator) {
        logger.info("NavigationListener: {}", navigator);
    }
}
