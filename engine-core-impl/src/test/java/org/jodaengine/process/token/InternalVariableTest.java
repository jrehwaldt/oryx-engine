package org.jodaengine.process.token;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.factory.TransitionFactory;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Testing the internal variable behavior of the {@link Token}.
 */
public class InternalVariableTest {

    private static final String INTERNAL_VARIABLE_ID = "internal-variable";
    private static final int INTERNAL_VARIABLE_VALUE = 1;

    private Node customNode, endNode;
    private Token token;

    @BeforeMethod
    public void setUp() {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        // Building the IntermediateTimer
        customNode = builder.getNodeBuilder()
        .setActivityBehavior(new SettingInternalVariableActivity(INTERNAL_VARIABLE_ID, INTERNAL_VARIABLE_VALUE))
        .setIncomingBehaviour(new SimpleJoinBehaviour()).setOutgoingBehaviour(new TakeAllSplitBehaviour()).buildNode();

        endNode = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        TransitionFactory.createTransitionFromTo(builder, customNode, endNode);

        Navigator nav = new NavigatorImplMock();
        AbstractProcessInstance processInstanceMock = Mockito.mock(AbstractProcessInstance.class);
        token = new TokenImpl(customNode, processInstanceMock, nav);
    }

    @Test
    public void testDeletionOfInternalVariables()
    throws JodaEngineException {

        token.executeStep();

        Assert.assertEquals(token.getAllInternalVariables().size(), 1);
        Assert.assertEquals(token.getInternalVariable(INTERNAL_VARIABLE_ID), INTERNAL_VARIABLE_VALUE);

        token.resume(null);

        Assert.assertNull(token.getInternalVariable(INTERNAL_VARIABLE_ID));
        Assert.assertEquals(token.getAllInternalVariables().size(), 0);
    }
}
