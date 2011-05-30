package org.jodaengine.ext.service;

import org.jodaengine.ext.Extension;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.ext.listener.token.ActivityLifecycleChangeEvent;
import org.testng.Assert;

/**
 * Listener implementation for testing the {@link ExtensionService} integration in
 * our {@link Token} as {@link AbstractTokenListener}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-28
 */
@Extension("testing-token-integration")
public class TestingTokenListener extends AbstractTokenListener {
    
    protected TestingListenerExtensionService listenerService;
    
    /**
     * Default constructor.
     * 
     * @param listenerService the listener service
     */
    public TestingTokenListener(TestingListenerExtensionService listenerService) {
        Assert.assertNotNull(listenerService);
        this.listenerService = listenerService;
        
        this.listenerService.registered(this);
    }

    @Override
    public void stateChanged(ActivityLifecycleChangeEvent event) {
        this.listenerService.invoked(this);
    }
}
