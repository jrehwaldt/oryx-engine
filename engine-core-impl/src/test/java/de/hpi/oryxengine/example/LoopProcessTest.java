package de.hpi.oryxengine.example;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.AddContextNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionImpl;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.ConditionImpl;
import de.hpi.oryxengine.process.structure.Node;
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
        assertEquals(token.getCurrentNode(), xorSplit);
        token.executeStep();
        assertEquals(token.getCurrentNode(), xorJoin);
        token.executeStep();
        assertEquals(token.getCurrentNode(), node);
        token.executeStep();
        assertEquals(token.getCurrentNode(), xorSplit);
        token.executeStep();
        assertEquals(token.getCurrentNode(), xorJoin);
        token.executeStep();
        assertEquals(token.getCurrentNode(), node);
        token.executeStep();
        assertEquals(token.getCurrentNode(), xorSplit);
        token.executeStep();
        assertEquals(token.getCurrentNode(), end);

    }
    
    /**
     * Sets up an example process.
     */
    @BeforeClass
    public void setUp() {
        
        ProcessBuilder builder = new ProcessBuilderImpl();
        
        //Create StartNode
        NodeParameter param = new NodeParameterImpl(
            mock(Activity.class),
            new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());
        start = builder.createNode(param);
        
        //Bootstrap
        Navigator nav = new NavigatorImplMock();
        List<Node> startNodes = new ArrayList<Node>();
        startNodes.add(start);
        ProcessDefinition definition = new ProcessDefinitionImpl(UUID.randomUUID(), "testLoop", startNodes);
        ProcessInstance instance = new ProcessInstanceImpl(definition);
        instance.getContext().setVariable("counter", "0");
        instance.getContext().setVariable("increment", "1");
        token = new TokenImpl(node, instance, nav);
        
        //Setup XOR DATA
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("counter", RUNTIMES);
        Condition condition1 = new ConditionImpl(map, "<=");
        Condition condition2 = new ConditionImpl(map, "==");

        //Create the XORJoin
        xorJoin = builder.createNode(param);
        
        //Create a following Node
        param.setActivity(new AddContextNumbersAndStoreActivity("counter", "increment", "counter"));
        node = builder.createNode(param);
        
        //Create the XORSplit
        param = new NodeParameterImpl(new NullActivity(), new SimpleJoinBehaviour(),
            new XORSplitBehaviour());
        xorSplit = builder.createNode(param);
        
        //Create the end
        param = new NodeParameterImpl(
            new EndActivity(),
            new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());
        end = builder.createNode(param);

        builder.createTransition(start, xorJoin)
        .createTransition(xorJoin, node)
        .createTransition(node, xorSplit)
        .createTransition(xorSplit, end, condition2)
        .createTransition(xorSplit, xorJoin, condition1);
        
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
