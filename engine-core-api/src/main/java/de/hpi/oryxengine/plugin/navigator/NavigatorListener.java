package de.hpi.oryxengine.plugin.navigator;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.plugin.Plugin;

/**
 * This interface should be implemented by plugins, which
 * aim to listen for changes within navigator threads
 * such as emptied queues etc.
 */
public interface NavigatorListener
extends Plugin {
    
    /**
     * This method is called when a navigator was started.
     * 
     * @param navigator the navigator, which state has changed
     */
    void navigatorStarted(@Nonnull Navigator navigator);
    
    /**
     * This method is called when a navigator was stopped.
     * 
     * @param navigator the navigator, which state has changed
     */
    void navigatorStopped(@Nonnull Navigator navigator);
}
