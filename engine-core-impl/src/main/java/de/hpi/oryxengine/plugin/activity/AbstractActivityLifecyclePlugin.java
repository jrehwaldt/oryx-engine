package de.hpi.oryxengine.plugin.activity;

import java.util.Observable;
import java.util.Observer;

import javax.annotation.Nonnull;

/**
 * This class may be injected to observe
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
