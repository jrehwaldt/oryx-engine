package de.hpi.oryxengine.plugin;

import java.util.Observable;
import java.util.Observer;

import javax.annotation.Nonnull;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.hpi.oryxengine.activity.AbstractActivityImpl;
import de.hpi.oryxengine.activity.ExecutionState;

/**
 * This class is a logger and may be injected to observe
 * the lifecycle of certain activities.
 */
public final class ActivityLifecycleLogger
implements Observer {
    
    private static ActivityLifecycleLogger instance;
    
    private final Logger logger = Logger.getLogger(getClass());
    private final Level level = Level.DEBUG;
    
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
    public static ActivityLifecycleLogger getInstance() {
        if (instance == null) {
            instance = new ActivityLifecycleLogger();
        }
        
        return instance;
    }
    
    /**
     * Register this logger instance with the provided activity.
     * 
     * @param activity the observed activity
     */
    public void registerWithActivity(@Nonnull Observable activity) {
        activity.addObserver(this);
    }
    
    /**
     * This method is invoked whenever the activity's state changes.
     * 
     * {@inheritDoc}
     */
    @Override
    public void update(@Nonnull Observable activity,
                       @Nonnull Object state) {
        stateChanged((AbstractActivityImpl) activity, (ExecutionState) state);
    }
    
    /**
     * Logs the change event to the default logger.
     * 
     * @param activity the activity, which changed
     * @param state the new state
     */
    private void stateChanged(@Nonnull AbstractActivityImpl activity,
                              @Nonnull ExecutionState state) {
        logger.log(level, "Activity " + activity.toString() + " changed. New state: " + state.toString());
    }
}
