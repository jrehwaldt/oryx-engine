package org.jodaengine.ext.service;

import org.jodaengine.ext.Extension;
import org.jodaengine.ext.listener.AbstractSchedulerListener;

import org.jodaengine.process.token.Token;

/**
 * Listener implementation for testing the {@link ExtensionService} integration in
 * our {@link Scheduler}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
@Extension("testing-scheduler-integration")
public class TestingSchedulerListener extends AbstractSchedulerListener {

    @Override
    public void processInstanceSubmitted(int numberOfTokens, Token token) {
        
    }

    @Override
    public void processInstanceRetrieved(int numberOfTokens, Token token) {
        
    }

}
