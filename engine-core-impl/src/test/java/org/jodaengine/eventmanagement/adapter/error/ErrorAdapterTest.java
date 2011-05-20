package org.jodaengine.eventmanagement.adapter.error;

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

    private ErrorAdapter errorAdapterSpy;
    private ErrorAdapterConfiguration config;

    /**
     * Setup test case.
     */
    @BeforeTest
    public void setUp() {

        this.config = new ErrorAdapterConfiguration();
        this.errorAdapterSpy = Mockito.spy(new ErrorAdapter(this.config));
    }

    /**
     * Tests the error adapter's correlation.
     */
    @Test
    public void testExceptionCorrelation() {

        this.errorAdapterSpy.exceptionOccured("Some message", new JodaEngineException("huhu"));
        ArgumentCaptor<ErrorAdapterEvent> event = ArgumentCaptor.forClass(ErrorAdapterEvent.class);
        Mockito.verify(this.errorAdapterSpy).correlate(event.capture());
        Assert.assertFalse(event.getValue() == null, "event should not be null");
    }
}
