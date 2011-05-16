package org.jodaengine.plugin.activity;

import javax.annotation.Nonnull;

import org.jodaengine.plugin.Plugin;

/**
 * This interface should be implemented by plugins, which
 * aim to react on lifecycle changes of an activity instance.
 * 
 * This is the <b>global</b> one, which means it is active for all
 * singleton instances of an activity.
 */
public interface ActivityLifecyclePlugin extends Plugin {

    /**
     * Hook for activity lifecycle changes.
     * 
     * @param event
     *            the change event
     */
    void stateChanged(@Nonnull ActivityLifecycleChangeEvent event);
}
