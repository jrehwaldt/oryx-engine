package org.jodaengine.eventmanagement.adapter.error;

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
    
    /**
     * Setup test case.
     */
    @BeforeTest
    public void setUp() {
        this.config = new ErrorAdapterConfiguration();
        this.adapter = new ErrorAdapter(this.config);
    }
    
    /**
     * Tests the error adapter's correlation.
     */
    @Test
    public void testExceptionCorrelation() {
//        this.adapter.exceptionOccured("Some message", new JodaEngineException("huhu"));
//        ArgumentCaptor<ErrorAdapterEvent> event = ArgumentCaptor.forClass(ErrorAdapterEvent.class);
//        verify(this.mock).correlate(event.capture());
//        assertFalse(event.getValue() == null, "event should not be null");
    }
}
