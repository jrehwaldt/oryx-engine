package org.jodaengine.example;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.ProcessDefinitionImpl;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Condition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.condition.HashMapCondition;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.mockito.Mockito;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * The Class LoopProcessTest.
 */
public class LoopProcessTest {

    private static final String DEFINITION_NAME = "TestLoopProcess";
    private static final String DEFINITION_DESCRIPTION = "This process tests the loop pattern.";

    private Token token;
    private Node start;
    private Node end;
    private Node xorSplit;
    private Node xorJoin;
    private Node node;
    private static final int RUNTIMES = 3;

    /**
     * Test the example loopProcess. It should execute several RUNTIMES and then end.
     * 
     * @throws JodaEngineException the JodaEngine exception
     */
    @Test
    public void testLoop()
    throws JodaEngineException {

        assertEquals(token.getCurrentNode(), start);
        token.executeStep();
        assertEquals(token.getCurrentNode(), xorJoin);
        token.executeStep();
        assertEquals(token.getCurrentNode(), node);
        token.executeStep();
        assertEquals(token.getInstance().getContext().getVariable("counter"), 1);
        assertEquals(token.getCurrentNode(), xorSplit);
        token.executeStep();
        assertEquals(token.getCurrentNode(), xorJoin);
        token.executeStep();
        assertEquals(token.getCurrentNode(), node);
        token.executeStep();
        assertEquals(token.getInstance().getContext().getVariable("counter"), 2);
        assertEquals(token.getCurrentNode(), xorSplit);
        token.executeStep();
        assertEquals(token.getCurrentNode(), xorJoin);
        token.executeStep();
        assertEquals(token.getCurrentNode(), node);
        token.executeStep();
        assertEquals(token.getInstance().getContext().getVariable("counter"), 2 + 1);
        assertEquals(token.getCurrentNode(), xorSplit);
        token.executeStep();
        assertEquals(token.getCurrentNode(), end);

    }

    /**
     * Sets up an example process.
     */
    @BeforeClass
    public void setUp() {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        start = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        // Create the XORJoin
        xorJoin = BpmnNodeFactory.createBpmnXorGatewayNode(builder);

        // Create a following Node
        node = BpmnCustomNodeFactory.createBpmnAddContextNumbersAndStoreNode(builder, "counter", new String[] {
            "increment", "counter" });

        // Create the XORSplit
        xorSplit = BpmnNodeFactory.createBpmnXorGatewayNode(builder);

        // Create the end
        end = BpmnNodeFactory.createBpmnEndEventNode(builder);

        // Setup XOR DATA
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("counter", RUNTIMES);
        Condition condition1 = new HashMapCondition(map, "<=");
        Condition condition2 = new HashMapCondition(map, "==");

        // Create {@link ControlFlow}s
        BpmnNodeFactory.createControlFlowFromTo(builder, start, xorJoin);
        BpmnNodeFactory.createControlFlowFromTo(builder, xorJoin, node);
        BpmnNodeFactory.createControlFlowFromTo(builder, node, xorSplit);
        BpmnNodeFactory.createControlFlowFromTo(builder, xorSplit, end, condition2);
        BpmnNodeFactory.createControlFlowFromTo(builder, xorSplit, xorJoin, condition1);

        // Bootstrap
        Navigator nav = new NavigatorImplMock();
        List<Node> startNodes = new ArrayList<Node>();
        startNodes.add(start);
        ProcessDefinitionID id = new ProcessDefinitionID(UUID.randomUUID().toString(), 0);
        ProcessDefinition definition = new ProcessDefinitionImpl(id, DEFINITION_NAME,
            DEFINITION_DESCRIPTION, startNodes);
        // TokenBuilder is not used here, therefore it can be null
        AbstractProcessInstance instance = new ProcessInstance(definition, Mockito.mock(BpmnTokenBuilder.class));
        instance.getContext().setVariable("counter", "0");
        instance.getContext().setVariable("increment", "1");

        // start node for token is set later on
        token = new BpmnToken(start, instance, nav);
    }

    /**
     * Tear down.
     */
    @AfterClass
    public void tearDown() {

        token = null;
        start = null;
        end = null;
        xorSplit = null;
        xorJoin = null;
        node = null;

    }
}
