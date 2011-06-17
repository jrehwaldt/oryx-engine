package org.jodaengine.process.instantiation;

import java.util.HashMap;
import java.util.Map;

import org.jodaengine.eventmanagement.processevent.incoming.IncomingStartProcessEvent;
import org.jodaengine.process.definition.ProcessDefinitionInside;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instantiation.pattern.EventBasedInstanceCreationPattern;
import org.jodaengine.process.structure.Node;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This test concentrates on testing the ProcessInstanciationPattern that is responsible for exclusive instantiation.
 */
public class EventBasedInstanceCreationPatternTest extends AbstractJodaEngineTest {

    private EventBasedInstanceCreationPattern eventBasedInstanceCreationPattern;
    private InstantiationPatternContext patternContext;
    private IncomingStartProcessEvent startEvent;
    private Node startNode;

    /**
     * Setting up all the mocks.
     */
    @BeforeMethod
    public void setUp() {

        eventBasedInstanceCreationPattern = new EventBasedInstanceCreationPattern();

        Map<IncomingStartProcessEvent, Node> startTriggers = new HashMap<IncomingStartProcessEvent, Node>();

        startEvent = Mockito.mock(IncomingStartProcessEvent.class);
        startNode = Mockito.mock(Node.class);
        startTriggers.put(startEvent, startNode);

        ProcessDefinitionInside processDefinition = Mockito.mock(ProcessDefinitionInside.class);
        Mockito.when(processDefinition.getStartTriggers()).thenReturn(startTriggers);

        patternContext = new InstantiationPatternContextImpl(processDefinition, startEvent);
    }

    /**
     * Tests the instantiation of a process that should be triggered exclusively by an event.
     */
    @Test
    public void testProcessInstantiationThroughEvent() {

        AbstractProcessInstance processInstance = eventBasedInstanceCreationPattern
        .createProcessInstance(patternContext);

        Assert.assertEquals(processInstance.getAssignedTokens().size(), 1);
        Assert.assertEquals(processInstance.getAssignedTokens().get(0).getCurrentNode(), startNode);
    }
}
