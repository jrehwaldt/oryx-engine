package de.hpi.oryxengine.plugin;

import java.util.Observable;
import java.util.Observer;

import javax.annotation.Nonnull;

/**
 * This class is a logger and may be injected to observe
 * the lifecycle of certain activities.
 */
public abstract class AbstractActivityLifecyclePlugin
implements ActivityLifecyclePlugin, Observer {
    
    /**
     * This method is invoked whenever the activity's state changes.
     * 
     * {@inheritDoc}
     */
    @Override
    public void update(@Nonnull Observable observable,
                       @Nonnull Object event) {
        stateChanged((ActivityLifecycleChangeEvent) event);
    }
}
