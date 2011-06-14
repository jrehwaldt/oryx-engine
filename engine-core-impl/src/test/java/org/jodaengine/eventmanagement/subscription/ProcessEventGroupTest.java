package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventManager;
import org.jodaengine.eventmanagement.adapter.manual.ManualTriggeringAdapter;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.processeventgroup.AbstractProcessIntermediateEventGroup;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.processeventgroup.ExclusiveProcessEventGroup;
import org.jodaengine.eventmanagement.subscription.processevent.intermediate.ProcessIntermediateManualTriggeringEvent;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the ProcessEventGroup implementation.
 */
public class ProcessEventGroupTest extends AbstractJodaEngineTest {

    private Token token;
    private IncomingIntermediateProcessEvent intermediateEvent1;
    private IncomingIntermediateProcessEvent intermediateEvent2;
    private IncomingIntermediateProcessEvent intermediateEvent3;

    /**
     * Setting up all necessary objects and mocks.
     */
    @BeforeMethod
    public void setUp() {

        token = Mockito.mock(Token.class);

        AbstractProcessIntermediateEventGroup eventGroup = new ExclusiveProcessEventGroup(token);

        intermediateEvent1 = new ProcessIntermediateManualTriggeringEvent("manualTrigger1", token, eventGroup);
        intermediateEvent2 = new ProcessIntermediateManualTriggeringEvent("manualTrigger2", token, eventGroup);
        intermediateEvent3 = new ProcessIntermediateManualTriggeringEvent("manualTrigger3", token, eventGroup);

        eventGroup.add(intermediateEvent1);
        eventGroup.add(intermediateEvent2);
        eventGroup.add(intermediateEvent3);

        ServiceFactory.getCorrelationService().registerIntermediateEvent(intermediateEvent1);
        ServiceFactory.getCorrelationService().registerIntermediateEvent(intermediateEvent2);
        ServiceFactory.getCorrelationService().registerIntermediateEvent(intermediateEvent3);
    }

    /**
     * Cleaning up some objects.
     */
    @AfterMethod
    public void tearDown() {

        ManualTriggeringAdapter.resetManualTriggeringAdapter();
    }

    /**
     * Tests that after an {@link IncomingProcessEvent} of the {@link AbstractProcessIntermediateEventGroup} is triggered that all other
     * {@link IncomingProcessEvent processEvents} are unsubscribed.
     */
    @Test
    public void testUnsubscriptionOfOtherProcessEvents() {

        ManualTriggeringAdapter.triggerManually("manualTrigger1");

        // Extracting the adapters
        ManualTriggeringAdapter intermediateEvent1Adapter = extractManualTriggeringAdapterFor(intermediateEvent1);
        ManualTriggeringAdapter intermediateEvent2Adapter = extractManualTriggeringAdapterFor(intermediateEvent2);
        ManualTriggeringAdapter intermediateEvent3Adapter = extractManualTriggeringAdapterFor(intermediateEvent3);

        // Checking if the List of registered processEvents is clear
        Assert.assertTrue(intermediateEvent1Adapter.getProcessEvents().isEmpty());
        Assert.assertTrue(intermediateEvent2Adapter.getProcessEvents().isEmpty());
        Assert.assertTrue(intermediateEvent3Adapter.getProcessEvents().isEmpty());

        Mockito.verify(token).resume(Mockito.anyObject());
    }

    /**
     * Tests that two {@link IncomingProcessEvent}s belonging to a {@link AbstractProcessIntermediateEventGroup} are triggered one after another.
     * The second call should be ignored.
     */
    @Test
    public void testTriggeringAfterTriggering() {

        ManualTriggeringAdapter.triggerManually("manualTrigger1");

        ManualTriggeringAdapter.triggerManually("manualTrigger2");

        // Extracting the adapters
        ManualTriggeringAdapter intermediateEvent1Adapter = extractManualTriggeringAdapterFor(intermediateEvent1);
        ManualTriggeringAdapter intermediateEvent2Adapter = extractManualTriggeringAdapterFor(intermediateEvent2);
        ManualTriggeringAdapter intermediateEvent3Adapter = extractManualTriggeringAdapterFor(intermediateEvent3);

        // Checking if the List of registered processEvents is clear
        Assert.assertTrue(intermediateEvent1Adapter.getProcessEvents().isEmpty());
        Assert.assertTrue(intermediateEvent2Adapter.getProcessEvents().isEmpty());
        Assert.assertTrue(intermediateEvent3Adapter.getProcessEvents().isEmpty());

        Mockito.verify(token).resume(Mockito.anyObject());
    }

    /**
     * Extract the {@link ManualTriggeringAdapter} for the {@link IncomingIntermediateProcessEvent}. This is only a helper
     * method.
     * 
     * @param intermediateEvent
     *            - the {@link IncomingIntermediateProcessEvent} which belongs to the {@link ManualTriggeringAdapter}
     * @return the {@link ManualTriggeringAdapter}s
     */
    private static ManualTriggeringAdapter extractManualTriggeringAdapterFor(IncomingIntermediateProcessEvent intermediateEvent) {

        EventManager eventManager = (EventManager) ServiceFactory.getCorrelationService();

        ManualTriggeringAdapter manualTrigger = (ManualTriggeringAdapter) eventManager.getEventAdapters().get(
            intermediateEvent.getAdapterConfiguration());

        Assert.assertNotNull(manualTrigger);

        return manualTrigger;
    }
}
