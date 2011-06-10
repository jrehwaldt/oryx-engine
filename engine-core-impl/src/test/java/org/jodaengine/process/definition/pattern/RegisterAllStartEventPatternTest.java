package org.jodaengine.process.definition.pattern;

import java.util.HashMap;
import java.util.Map;

import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.jodaengine.process.activation.ProcessDefinitionActivationPatternContext;
import org.jodaengine.process.activation.pattern.RegisterAllStartEventPattern;
import org.jodaengine.process.definition.BpmnProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Testing the {@link RegisterAllStartEventPattern}.
 */
public class RegisterAllStartEventPatternTest {

    private ProcessDefinitionActivationPatternContext patternContext;
    private EventSubscriptionManager eventManager;
    private ProcessStartEvent startEvent1;
    private ProcessStartEvent startEvent2;

    /**
     * Setting up mocks.
     */
    @BeforeMethod
    public void setUpMocks() {

        patternContext = Mockito.mock(ProcessDefinitionActivationPatternContext.class);
        eventManager = Mockito.mock(EventSubscriptionManager.class);
        Mockito.when(patternContext.getCorrelationService()).thenReturn(eventManager);

        BpmnProcessDefinition processDefinition = Mockito.mock(BpmnProcessDefinition.class);
        Mockito.when(patternContext.getProcessDefinition()).thenReturn(processDefinition);

        Map<ProcessStartEvent, Node> map = new HashMap<ProcessStartEvent, Node>();
        startEvent1 = Mockito.mock(ProcessStartEvent.class);
        startEvent2 = Mockito.mock(ProcessStartEvent.class);
        map.put(startEvent1, Mockito.mock(Node.class));
        map.put(startEvent2, Mockito.mock(Node.class));
        Mockito.when(processDefinition.getStartTriggers()).thenReturn(map);
    }

    /**
     * Testing calling the
     * {@link ProcessDefinitionActivationPattern#activateProcessDefinition(ProcessDefinitionActivationPatternContext)
     * activateProcessDefinition(...)} of the pattern.
     */
    @Test
    public void testActivation() {

        RegisterAllStartEventPattern registerStartEventsPattern = new RegisterAllStartEventPattern();

        // Call the method
        registerStartEventsPattern.activateProcessDefinition(patternContext);

        // Make assertions
        ArgumentCaptor<ProcessStartEvent> startEventArgument = ArgumentCaptor.forClass(ProcessStartEvent.class);
        Mockito.verify(eventManager, Mockito.times(2)).registerStartEvent(startEventArgument.capture());

        Assert.assertEquals(startEventArgument.getAllValues().size(), 2);
        Assert.assertTrue(startEventArgument.getAllValues().contains(startEvent2));
        Assert.assertTrue(startEventArgument.getAllValues().contains(startEvent1));

        Assert.assertEquals(registerStartEventsPattern.getRegisteredStartEvents().size(), 2);
    }

    /**
     * Testing calling twice the
     * {@link ProcessDefinitionActivationPattern#activateProcessDefinition(ProcessDefinitionActivationPatternContext)
     * activateProcessDefinition(...)} of the pattern. The pattern should activate only one.
     */
    @Test
    public void test2FollowingActivations() {

        RegisterAllStartEventPattern registerStartEventsPattern = new RegisterAllStartEventPattern();

        registerStartEventsPattern.activateProcessDefinition(patternContext);
        registerStartEventsPattern.activateProcessDefinition(patternContext);

        Mockito.verify(eventManager, Mockito.times(2)).registerStartEvent(Mockito.any(ProcessStartEvent.class));
        Assert.assertEquals(registerStartEventsPattern.getRegisteredStartEvents().size(), 2);
    }

    /**
     * Testing calling the
     * {@link ProcessDefinitionDeactivationPattern#deactivateProcessDefinition(ProcessDefinitionActivationPatternContext)
     * deactivateProcessDefinition(...)} of the pattern.
     */
    @Test
    public void testDeactivation() {

        RegisterAllStartEventPattern registerStartEventsPattern = new RegisterAllStartEventPattern();

        registerStartEventsPattern.activateProcessDefinition(patternContext);
        registerStartEventsPattern.deactivateProcessDefinition(patternContext);

        ArgumentCaptor<ProcessStartEvent> startEventArgument = ArgumentCaptor.forClass(ProcessStartEvent.class);
        Mockito.verify(eventManager, Mockito.times(2)).unsubscribeFromStartEvent(startEventArgument.capture());

        Assert.assertEquals(startEventArgument.getAllValues().size(), 2);
        Assert.assertTrue(startEventArgument.getAllValues().contains(startEvent2));
        Assert.assertTrue(startEventArgument.getAllValues().contains(startEvent1));

        Assert.assertEquals(registerStartEventsPattern.getRegisteredStartEvents().size(), 0);
    }
}
