package de.hpi.oryxengine.example;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.AddContextNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinitionImpl;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.condition.HashMapCondition;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.XORSplitBehaviour;

/**
 * The Class LoopProcessTest.
 */
public class LoopProcessTest {
    
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
     * @throws DalmatinaException the dalmatina exception
     */
    @Test
    public void testLoop() throws DalmatinaException {
        
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
        
        ProcessDefinitionBuilder builder = new ProcessBuilderImpl();
        
        //Create StartNode
        ActivityBlueprint blueprint = new ActivityBlueprintImpl(NullActivity.class);
        
        NodeParameter emptyActivityParam = new NodeParameterImpl(
            blueprint,
            new SimpleJoinBehaviour(),
            new XORSplitBehaviour());
        start = builder.createNode(emptyActivityParam);
        
        //Bootstrap
        Navigator nav = new NavigatorImplMock();
        List<Node> startNodes = new ArrayList<Node>();
        startNodes.add(start);
        ProcessDefinition definition = new ProcessDefinitionImpl(UUID.randomUUID(), "testLoop", startNodes);
        AbstractProcessInstance instance = new ProcessInstanceImpl(definition);
        instance.getContext().setVariable("counter", "0");
        instance.getContext().setVariable("increment", "1");
        // start node for token is set later on
        token = new TokenImpl(null, instance, nav);
        
        //Setup XOR DATA
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("counter", RUNTIMES);
        Condition condition1 = new HashMapCondition(map, "<=");
        Condition condition2 = new HashMapCondition(map, "==");

        //Create the XORJoin
        xorJoin = builder.createNode(emptyActivityParam);
        
        //Create a following Node
        Class<?>[] constructorSig = {String.class, String[].class};
        Object[] params = {"counter", new String[]{"increment", "counter"}};
        blueprint = new ActivityBlueprintImpl(AddContextNumbersAndStoreActivity.class, constructorSig,
            params);
        NodeParameter addingActivityParam = new NodeParameterImpl(
            blueprint,
            new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());
        node = builder.createNode(addingActivityParam);
        
        //Create the XORSplit
        xorSplit = builder.createNode(emptyActivityParam);
        
        //Create the end
        blueprint = new ActivityBlueprintImpl(EndActivity.class);
        NodeParameter endParam = new NodeParameterImpl(
            blueprint,
            new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());
        end = builder.createNode(endParam);

        //Create Transitions
        builder.createTransition(start, xorJoin)
        .createTransition(xorJoin, node)
        .createTransition(node, xorSplit)
        .createTransition(xorSplit, end, condition2)
        .createTransition(xorSplit, xorJoin, condition1);
        
        //Set start
        token.setCurrentNode(start);
  
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
