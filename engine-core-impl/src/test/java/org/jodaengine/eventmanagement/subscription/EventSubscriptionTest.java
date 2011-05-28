package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.EventManager;
import org.jodaengine.eventmanagement.adapter.AbstractCorrelatingEventAdapter;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.processevent.start.DefaultProcessStartEvent;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the start event subscriptions.
 */
public class EventSubscriptionTest {

    private EventManager eventManager;
    private AbstractCorrelatingEventAdapter<?> correlationAdapter;
    private AdapterConfiguration adapterConfiguration;

    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {

        this.eventManager = new EventManager();
        eventManager.start(new JodaEngine());

        correlationAdapter = Mockito.mock(AbstractCorrelatingEventAdapter.class);
        adapterConfiguration = Mockito.mock(AdapterConfiguration.class);
        Mockito.when(adapterConfiguration.registerAdapter(Mockito.any(AdapterManagement.class))).thenReturn(
            correlationAdapter);
    }

    /**
     * Tear down.
     */
    @AfterMethod
    public void tearDown() {

        eventManager.stop();
    }

    /**
     * Test process start event is in list of adapter.
     */
    @Test
    public void testForwardingProcessStartEventSubscription() {

        ProcessStartEvent startEvent = Mockito.mock(DefaultProcessStartEvent.class);
        Mockito.when(startEvent.getAdapterConfiguration()).thenReturn(adapterConfiguration);

        eventManager.registerStartEvent(startEvent);

        ArgumentCaptor<DefaultProcessStartEvent> event = ArgumentCaptor.forClass(DefaultProcessStartEvent.class);
        Mockito.verify(correlationAdapter).registerStartEvent(event.capture());
    }

    /**
     * Tests the forwarding process of calls when an intermediate event is subscribed.
     */
    @Test
    public void testForwardingProcessIntermediateEventSubscription() {

        ProcessIntermediateEvent intermediateEvent = Mockito.mock(ProcessIntermediateEvent.class);
        Mockito.when(intermediateEvent.getAdapterConfiguration()).thenReturn(adapterConfiguration);

        eventManager.registerIntermediateEvent(intermediateEvent);

        ArgumentCaptor<ProcessIntermediateEvent> event = ArgumentCaptor.forClass(ProcessIntermediateEvent.class);
        Mockito.verify(correlationAdapter).registerIntermediateEvent(event.capture());
    }
}
