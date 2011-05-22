package org.jodaengine.ext.logger;

import javax.annotation.Nonnull;

import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.plugin.activity.ActivityLifecycleChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a {@link Logger} and may be injected to observe
 * the lifecycle of certain {@link AbstractActivity}s.
 */
public final class ActivityLifecycleLogger
extends AbstractTokenListener {
    
    /** The instance. */
    private static ActivityLifecycleLogger instance;
    
    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Hide singleton constructor.
     */
    private ActivityLifecycleLogger() { }
    
    /**
     * Returns a lazily initialized logger instance.
     * 
     * @return a logger instance
     */
    public synchronized static @Nonnull ActivityLifecycleLogger getInstance() {
        if (instance == null) {
            instance = new ActivityLifecycleLogger();
        }
        
        return instance;
    }
    
    @Override
    public void stateChanged(@Nonnull ActivityLifecycleChangeEvent event) {
        logger.info("ActivityLifecycle: {}", event);
    }
}
