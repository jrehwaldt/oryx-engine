package org.jodaengine.eventmanagement.adapter.error;

import org.jodaengine.eventmanagement.EventCorrelator;
import org.jodaengine.exception.JodaEngineException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Test class for {@link ErrorAdapter} tests.
 * 
 * @author Jan Rehwaldt
 */
public class ErrorAdapterTest {

    private ErrorAdapter errorAdapter;
    private ErrorAdapterConfiguration config;
    private EventCorrelator eventCorrelatorMock;

    /**
     * Setup test case.
     */
    @BeforeTest
    public void setUp() {

        this.eventCorrelatorMock = Mockito.mock(EventCorrelator.class);
        this.config = new ErrorAdapterConfiguration();
        this.errorAdapter = new ErrorAdapter(this.config);
    }

    /**
     * Tests the error adapter's correlation.
     */
    @Test
    public void testExceptionCorrelation() {

//        this.errorAdapter.exceptionOccured("Some message", new JodaEngineException("huhu"));
//        ArgumentCaptor<ErrorAdapterEvent> event = ArgumentCaptor.forClass(ErrorAdapterEvent.class);
//        Mockito.verify(this.eventCorrelatorMock).correlate(event.capture());
//        Assert.assertFalse(event.getValue() == null, "event should not be null");
    }
}
