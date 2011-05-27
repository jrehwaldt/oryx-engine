package org.jodaengine.node.activity.bpmn;

import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.factory.TransitionFactory;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.instance.ProcessInstanceContextImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * It test the {@link BpmnEventBasedGateway}.
 */
public class BpmnEventBasedXorGatewayTest extends AbstractJodaEngineTest {

    private Token token;
    private Node eventBasedXorGatewayNode, intermediateEvent1Node, intermediateEvent2Node, endNode1, endNode2;

    private static final int WAITING_TIME_1 = 300;
    private static final int WAITING_TIME_2 = 400;
    private static final int LONG_WAITING_TIME_TEST = 600;

    @BeforeMethod
    public void setUp() {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        // Building the IntermediateTimer
        eventBasedXorGatewayNode = BpmnNodeFactory.createBpmnEventBasedXorGatewayNode(builder);

        intermediateEvent1Node = BpmnNodeFactory.createBpmnIntermediateTimerEventNode(builder, WAITING_TIME_1);
        intermediateEvent2Node = BpmnNodeFactory.createBpmnIntermediateTimerEventNode(builder, WAITING_TIME_2);

        endNode1 = BpmnCustomNodeFactory.createBpmnNullNode(builder);
        endNode2 = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        TransitionFactory.createTransitionFromTo(builder, eventBasedXorGatewayNode, intermediateEvent1Node);
        TransitionFactory.createTransitionFromTo(builder, intermediateEvent1Node, endNode1);

        TransitionFactory.createTransitionFromTo(builder, eventBasedXorGatewayNode, intermediateEvent2Node);
        TransitionFactory.createTransitionFromTo(builder, intermediateEvent2Node, endNode2);

        Navigator nav = new NavigatorImplMock();
        ProcessInstanceContext processInstanceContextMock = new ProcessInstanceContextImpl();
        AbstractProcessInstance processInstanceMock = Mockito.mock(AbstractProcessInstance.class);
        Mockito.when(processInstanceMock.getContext()).thenReturn(processInstanceContextMock);

        token = new BpmnToken(eventBasedXorGatewayNode, processInstanceMock, nav);
        token = Mockito.spy(token);    
}

    @Test
    public void testRouting()
    throws Exception {

        token.executeStep();

        Thread.sleep(LONG_WAITING_TIME_TEST);
        Assert.assertEquals(token.getCurrentNode(), endNode1);
        
        Mockito.verify(token).resume(Mockito.any(ProcessIntermediateEvent.class));
    }
}
