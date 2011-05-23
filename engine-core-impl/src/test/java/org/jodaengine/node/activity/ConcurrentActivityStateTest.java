package org.jodaengine.node.activity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.factory.TransitionFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.node.helper.ActivityLifecycleAssuranceListener;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BPMNToken;
import org.jodaengine.process.token.BPMNTokenImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * The Class ConcurrentActivityStateTest. It tackles a problem with our process definition, nodes and the contained
 * activities. If a node is executed several times, its activity is already "dirty" (e.g. its state is COMPLETED).
 * 
 * The Problem exists in the following cases: 1. Two instances work on the same process definition, execute the same
 * activity 2. One token is in a loop and executes the activity twice
 * 
 * Possible solutions: 1. Create the node-Tree for each process instance (which implies activity recreation). This will
 * only solve case 1 2. Create an activity instance, when the token reaches the node and only link some kind of activity
 * blueprint to the node upon definition creation. This should solve both cases. Drawbacks are: Plugins cannot be
 * registered as it is not clear when to do that (maybe keep them with the process instance, when did we do this before
 * (not in a test)?). Deepcopys of activity members might be needed (e.g. the task that is assigned to the
 * HumanTaskActivity has to be copied). I don't have a clue how to implement this cleanly.
 */
public class ConcurrentActivityStateTest {
    private ProcessDefinition definition = null;
    private Node<BPMNToken> startNode = null;

    /**
     * One definition, two instances, two tokens that work on the same activities. This test ensures that these
     * activities do not share their state.
     * 
     * @throws JodaEngineException
     *             test fails
     */
    @Test
    public void testConcurrentActivityUse() throws JodaEngineException {

        // Create two process instances
        NavigatorImplMock nav = new NavigatorImplMock();
        AbstractProcessInstance<BPMNToken> instance1 = new ProcessInstanceImpl<BPMNToken>(definition);
        BPMNToken token1 = new BPMNTokenImpl(startNode, instance1, nav);
        instance1.addToken(token1);

        AbstractProcessInstance<BPMNToken> instance2 = new ProcessInstanceImpl<BPMNToken>(definition);
        BPMNToken token2 = new BPMNTokenImpl(startNode, instance2, nav);
        instance2.addToken(token2);

        assertEquals(token1.getCurrentActivityState(), ActivityState.INIT);
        assertEquals(token2.getCurrentActivityState(), ActivityState.INIT);

        // Execute a step with token1 on instance1. We expect, that the activity state changes for instance1, but not
        // for instance2/token2.

        ActivityLifecycleAssuranceListener plugin = new ActivityLifecycleAssuranceListener();
        ((BPMNTokenImpl) token1).registerListener(plugin);
        token1.executeStep();
        
        assertTrue(plugin.isCompletedCalled());
        assertEquals(token2.getCurrentActivityState(), ActivityState.INIT);

    }

    /**
     * Sets the up process.
     * 
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     */
    @BeforeClass
    public void setUpProcess()
    throws IllegalStarteventException {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        startNode = BpmnNodeFactory.createBpmnStartEventNode(builder);

        Node<BPMNToken> endNode = BpmnNodeFactory.createBpmnEndEventNode(builder);

        TransitionFactory.createTransitionFromTo(builder, startNode, endNode);
        
        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);
        
        definition = builder.buildDefinition();
    }
}
