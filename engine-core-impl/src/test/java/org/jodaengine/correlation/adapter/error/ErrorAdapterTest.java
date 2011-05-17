package org.jodaengine.correlation.adapter.error;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertFalse;

import org.jodaengine.eventmanagement.CorrelationManager;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapter;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapterEvent;
import org.jodaengine.exception.JodaEngineException;

import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;



/**
 * Test class for {@link ErrorAdapter} tests.
 * 
 * @author Jan Rehwaldt
 */
public class ErrorAdapterTest {
    
    private ErrorAdapter adapter = null;
    private ErrorAdapterConfiguration config = null;
    private CorrelationManager mock = null;
    
    /**
     * Setup test case.
     */
    @BeforeTest
    public void setUp() {
        this.mock = mock(CorrelationManager.class);
        this.config = new ErrorAdapterConfiguration();
        this.adapter = new ErrorAdapter(this.mock, this.config);
    }
    
    /**
     * Tests the error adapter's correlation.
     */
    @Test
    public void testExceptionCorrelation() {
        this.adapter.exceptionOccured("Some message", new JodaEngineException("huhu"));
        ArgumentCaptor<ErrorAdapterEvent> event = ArgumentCaptor.forClass(ErrorAdapterEvent.class);
        verify(this.mock).correlate(event.capture());
        assertFalse(event.getValue() == null, "event should not be null");
    }
}
