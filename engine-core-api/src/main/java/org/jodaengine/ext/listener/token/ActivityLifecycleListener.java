package org.jodaengine.ext.listener.token;

import javax.annotation.Nonnull;

import org.jodaengine.ext.listener.Listener;
import org.jodaengine.node.activity.Activity;

/**
 * This interface should be implemented by {@link Listener}s, which
 * aim to react on lifecycle changes of an {@link Activity} instance.
 */
public interface ActivityLifecycleListener extends Listener {

    /**
     * Hook for activity lifecycle changes.
     * 
     * @param event
     *            the change event
     */
    void stateChanged(@Nonnull ActivityLifecycleChangeEvent event);
}
