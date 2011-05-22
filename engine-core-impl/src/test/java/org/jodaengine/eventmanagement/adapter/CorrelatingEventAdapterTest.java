package org.jodaengine.eventmanagement.adapter;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Testing basic functionality of the CorrelationEventAdapter.
 */
public class CorrelatingEventAdapterTest {

    private AbstractCorrelatingEventAdapter<?> eventAdapter;

    @BeforeMethod
    public void setUp() {

        this.eventAdapter = new DummyCorrelatingEventAdapter();
    }

    @Test
    public void testRegisteringStartEvent() {

        ProcessStartEvent startEvent = Mockito.mock(ProcessStartEvent.class);
        
        // Registering the startEvent
        eventAdapter.registerStartEvent(startEvent);

        Assert.assertEquals(eventAdapter.getProcessEvents().size(), 1);
        Assert.assertEquals(startEvent, eventAdapter.getProcessEvents().get(0));
    }

    @Test
    public void testRegisterAndCorrelateStartEvent() {

        ProcessStartEvent startEvent = Mockito.mock(ProcessStartEvent.class);
        Mockito.when(startEvent.evaluate(Mockito.any(AdapterEvent.class))).thenReturn(true);

        // At first register and then correlate
        eventAdapter.registerStartEvent(startEvent);
        eventAdapter.correlateAdapterEvent(Mockito.mock(AbstractAdapterEvent.class));

        Mockito.verify(startEvent).trigger();
        Assert.assertEquals(eventAdapter.getUnCorrelatedAdapterEvents().size(), 1);
    }

    @Test
    public void testRegisteringIntermediateEvent() {

        ProcessIntermediateEvent intermediateEvent = Mockito.mock(ProcessIntermediateEvent.class);
        
        // Registering the intermediateEvent
        eventAdapter.registerIntermediateEvent(intermediateEvent);

        Assert.assertEquals(eventAdapter.getProcessEvents().size(), 1);
        Assert.assertEquals(intermediateEvent, eventAdapter.getProcessEvents().get(0));
    }

    @Test
    public void testRegisterAndCorrelateIntermediateEvent() {

        ProcessIntermediateEvent intermediateEvent = Mockito.mock(ProcessIntermediateEvent.class);
        Mockito.when(intermediateEvent.evaluate(Mockito.any(AdapterEvent.class))).thenReturn(true);

        // At first register and then correlate
        eventAdapter.registerIntermediateEvent(intermediateEvent);
        eventAdapter.correlateAdapterEvent(Mockito.mock(AbstractAdapterEvent.class));

        Mockito.verify(intermediateEvent).trigger();
        Assert.assertEquals(eventAdapter.getUnCorrelatedAdapterEvents().size(), 1);
    }
}
