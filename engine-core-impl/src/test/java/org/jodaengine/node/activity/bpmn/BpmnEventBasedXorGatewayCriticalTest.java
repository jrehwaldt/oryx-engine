package org.jodaengine.node.activity.bpmn;

import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.factory.ControlFlowFactory;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
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
 * It test the {@link BpmnEventBasedGateway}. But this is more critical because the two triggers are only 5 ms apart.
 */
public class BpmnEventBasedXorGatewayCriticalTest extends AbstractJodaEngineTest {

    private final static int NEW_WAITING_TIME_1 = 300;
    private final static int NEW_WAITING_TIME_2 = 320;

    private static final int LONG_WAITING_TIME_TEST = 1000;

    protected Token token;
    protected Node eventBasedXorGatewayNode, intermediateEvent1Node, intermediateEvent2Node, endNode1, endNode2;

    /**
     * SetUp.
     */
    @BeforeMethod
    public void setUp() {

        buildProcessGraph();
        Navigator nav = new NavigatorImplMock();
        ProcessInstanceContext processInstanceContextMock = new ProcessInstanceContextImpl();
        AbstractProcessInstance processInstanceMock = Mockito.mock(AbstractProcessInstance.class);
        Mockito.when(processInstanceMock.getContext()).thenReturn(processInstanceContextMock);

        token = new BpmnToken(eventBasedXorGatewayNode, processInstanceMock, nav);
        token = Mockito.spy(token);
    }

    /**
     * The normal rounting behavior.
     * 
     * @throws Exception
     *             - if it fails
     */
    @Test(invocationCount = 1)
    public void testRouting()
    throws Exception {

        token.executeStep();

        Thread.sleep(LONG_WAITING_TIME_TEST);

        Assert.assertEquals(token.getCurrentNode(), endNode1, errorMessage());

        Mockito.verify(token).resume(Mockito.any(IncomingIntermediateProcessEvent.class));
    }

    /**
     * Builds the graph structure of the little test definition that should be tested.
     */
    private void buildProcessGraph() {

        BpmnProcessDefinitionBuilder builder = BpmnProcessDefinitionBuilder.newBuilder();

        // Building the IntermediateTimer
        eventBasedXorGatewayNode = BpmnNodeFactory.createBpmnEventBasedXorGatewayNode(builder);

        intermediateEvent1Node = BpmnNodeFactory.createBpmnIntermediateTimerEventNode(builder, NEW_WAITING_TIME_1);

        intermediateEvent2Node = BpmnNodeFactory.createBpmnIntermediateTimerEventNode(builder, NEW_WAITING_TIME_2);

        endNode1 = BpmnCustomNodeFactory.createBpmnNullNode(builder);
        endNode2 = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        ControlFlowFactory.createControlFlowFromTo(builder, eventBasedXorGatewayNode, intermediateEvent1Node);
        ControlFlowFactory.createControlFlowFromTo(builder, intermediateEvent1Node, endNode1);

        ControlFlowFactory.createControlFlowFromTo(builder, eventBasedXorGatewayNode, intermediateEvent2Node);
        ControlFlowFactory.createControlFlowFromTo(builder, intermediateEvent2Node, endNode2);

    }

    /**
     * Builds an error Message.
     * 
     * @return the errorMessage as String
     */
    private String errorMessage() {

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(eventBasedXorGatewayNode + " => eventBasedXorGatewayNode \n");
        strBuilder.append(intermediateEvent1Node + " => intermediateEvent1Node \n");
        strBuilder.append(intermediateEvent2Node + " => intermediateEvent2Node \n");
        strBuilder.append(endNode1 + " => endNode1 \n");
        strBuilder.append(endNode2 + " => endNode2 \n");

        return strBuilder.toString();
    }
}
