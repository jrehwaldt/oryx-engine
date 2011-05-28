package org.jodaengine.ext.service;

import org.jodaengine.ext.Extension;
import org.jodaengine.ext.listener.AbstractNavigatorListener;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorState;
import org.testng.Assert;

/**
 * Listener implementation for testing the {@link ExtensionService} integration in
 * our {@link Navigator}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
@Extension("testing-navigator-integration")
public class TestingNavigatorListener extends AbstractNavigatorListener {
    
    protected TestingListenerExtensionService listenerService;
    
    /**
     * Default constructor.
     * 
     * @param listenerService the listener service
     */
    public TestingNavigatorListener(TestingListenerExtensionService listenerService) {
        Assert.assertNotNull(listenerService);
        this.listenerService = listenerService;
        
        this.listenerService.registered(this);
    }
    
    @Override
    protected void stateChanged(Navigator nav,
                                NavigatorState navState) {
        this.listenerService.invoked(this);
    }
}
