package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventManager;
import org.jodaengine.eventmanagement.adapter.manual.ManualTriggeringAdapter;
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

    /**
     * Setting up all necessary objects and mocks.
     */
    @BeforeMethod
    public void setUp() {

        token = Mockito.mock(Token.class);

    }

    @Test
    public void testOneEventOfGroup() {

        // 1. eine Eventgruppe erzeugen
        ProcessEventGroup eventGroup = new ProcessEventGroup(token);
        // 2. die einzelnen Elemente der Eventgruppe registrieren

        ProcessIntermediateEvent intermediateEvent1 = new ProcessIntermediateManualTriggeringEvent("manualTrigger1",
            token, eventGroup);
        ProcessIntermediateEvent intermediateEvent2 = new ProcessIntermediateManualTriggeringEvent("manualTrigger2",
            token, eventGroup);

        eventGroup.add(intermediateEvent1);
        eventGroup.add(intermediateEvent2);

        ServiceFactory.getCorrelationService().registerIntermediateEvent(intermediateEvent1);
        ServiceFactory.getCorrelationService().registerIntermediateEvent(intermediateEvent2);

        ManualTriggeringAdapter.triggerManually("manualTrigger1");

        EventManager eventManager = (EventManager) ServiceFactory.getCorrelationService();
        // Iterator<EventAdapter> eventAdapterIterator = eventManager.getEventAdapters().values().iterator();
        //
        // EventAdapter eventAdapter = eventAdapterIterator.next();
        // if (eventAdapterIterator instanceof ErrorAdapter) {
        //
        // ManualTriggeringAdapter intermediateEvent1Adapter = (ManualTriggeringAdapter) eventAdapterIterator.next();
        // ManualTriggeringAdapter intermediateEvent2Adapter = (ManualTriggeringAdapter) eventAdapterIterator.next();
        // } else {
        //
        // }

        ManualTriggeringAdapter intermediateEvent1Adapter = (ManualTriggeringAdapter) eventManager.getEventAdapters()
        .get(intermediateEvent1.getAdapterConfiguration());
        ManualTriggeringAdapter intermediateEvent2Adapter = (ManualTriggeringAdapter) eventManager.getEventAdapters()
        .get(intermediateEvent2.getAdapterConfiguration());

        Assert.assertNotNull(intermediateEvent1Adapter);
        Assert.assertNotNull(intermediateEvent2Adapter);

        Assert.assertTrue(intermediateEvent1Adapter.getProcessEvents().isEmpty());
        Assert.assertTrue(intermediateEvent2Adapter.getProcessEvents().isEmpty(),
            String.valueOf(intermediateEvent2Adapter.getProcessEvents().size()));
    }

    /**
     * Cleaning up some objects.
     */
    @AfterMethod
    public void tearDown() {

        ManualTriggeringAdapter.resetManualTriggeringAdapter();
    }

}
