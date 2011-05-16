package org.jodaengine.plugin.activity;

import java.util.Observable;

import javax.annotation.Nonnull;

import org.jodaengine.plugin.ObserverPlugin;



/**
 * This class may be injected to observe
 * the lifecycle of certain activities.
 */
public abstract class AbstractTokenPlugin
implements ObserverPlugin, ActivityLifecyclePlugin {
    
    /**
     * This method is invoked whenever the activity's state changes.
     * 
     * {@inheritDoc}
     */
    @Override
    public void update(@Nonnull Observable observable,
                       @Nonnull Object event) {
        // TODO if several different events might occur here, the type of the event should be checked here
        stateChanged((ActivityLifecycleChangeEvent) event);
    }
}
