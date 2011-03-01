package de.hpi.oryxengine.plugin.activity;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a logger and may be injected to observe
 * the lifecycle of certain activities.
 */
public final class ActivityLifecycleLogger
extends AbstractActivityLifecyclePlugin {
    
    private static ActivityLifecycleLogger instance;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Hide singleton constructor.
     */
    private ActivityLifecycleLogger() {
        
    }
    
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void stateChanged(@Nonnull ActivityLifecycleChangeEvent event) {
        logger.info("ActivityLifecycle: {}", event);
    }
}
