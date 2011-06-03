package org.jodaengine.node.activity.bpmn;

import static org.mockito.Mockito.mock;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.incomingbehaviour.ComplexJoinBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeBuilder;
import org.jodaengine.process.structure.NodeBuilderImpl;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for constructs that can be expressed with the BPMN complex gateway, such as the blocking discriminator
 * (WF-pattern 28).
 */
public class BpmnComplexGatewayTest {
    private Node beforeNode1, beforeNode2, beforeNode3, discriminator, afterNode;
    private NavigatorImplMock nav;

    /**
     * Sets up a structure with 3 nodes connected by control flow to a 2-of-3 discriminator and one outgoing transition
     * to the next node.
     */
    @BeforeMethod
    public void setUp() {

        nav = new NavigatorImplMock();

        NodeBuilder builder = new NodeBuilderImpl();
        builder.setIncomingBehaviour(new SimpleJoinBehaviour());
        builder.setOutgoingBehaviour(new TakeAllSplitBehaviour());
        builder.setActivityBehavior(mock(Activity.class));

        beforeNode1 = builder.buildNode();
        beforeNode2 = builder.buildNode();
        beforeNode3 = builder.buildNode();
        afterNode = builder.buildNode();

        // parameter is 1: we want a 1-out-of-n-join
        builder.setIncomingBehaviour(new ComplexJoinBehaviour(1));
        
        // TODO use ComplexSplitBehaviour
        builder.setOutgoingBehaviour(new TakeAllSplitBehaviour());

        discriminator = builder.buildNode();

        // create Transitions
        beforeNode1.transitionTo(discriminator);
        beforeNode2.transitionTo(discriminator);
        beforeNode3.transitionTo(discriminator);

        discriminator.transitionTo(afterNode);
    }

    @Test
    public void testBlockingDiscriminator()
    throws JodaEngineException {

        AbstractProcessInstance instance = new ProcessInstance(mock(ProcessDefinition.class), new BpmnTokenBuilder(nav,
            null, null));

        // set a token on each of the nodes before the discriminator
        BpmnToken token1 = new BpmnToken(beforeNode1, instance, nav);
        BpmnToken token2 = new BpmnToken(beforeNode2, instance, nav);
        BpmnToken token3 = new BpmnToken(beforeNode3, instance, nav);

        // enable one incoming sequence flow
        token1.executeStep();
        
        Assert.assertEquals(token1.getCurrentNode(), discriminator, "token1 should be on the discriminator now");

        nav.flushWorkQueue();
        
        // fire the discriminator        
        token1.executeStep();
        Assert.assertEquals(nav.getWorkQueue().size(), 1, "There should be one rescheduled token");
        Token createdToken = nav.getWorkQueue().get(0);
        Assert.assertEquals(createdToken.getCurrentNode(), afterNode,
            "The token should be placed on the node following the discriminator");


        // token2 arrives at the discriminator
        token2.executeStep();
        
        nav.flushWorkQueue();

        // token2 executes the discriminator, while the discriminator is not ready and in state "waiting for reset".
        token2.executeStep();
        Assert.assertEquals(nav.getWorkQueue().size(), 0, "There should be no new scheduled token, "
            + "the discriminator should not have forwarded the token as it is not ready again");

        // do the same with token3.
        token3.executeStep();
        
        nav.flushWorkQueue();
        token3.executeStep();
        Assert.assertEquals(nav.getWorkQueue().size(), 0, "There should be no new scheduled token, "
            + "the discriminator should not have forwarded the token as it is not ready again");
        
        // we create a new token to check, whether the discriminator is now able to fire again
        BpmnToken aNewToken = new BpmnToken(beforeNode2, instance, nav);
        aNewToken.executeStep();
        
        nav.flushWorkQueue();
        
        // execute the discriminator
        aNewToken.executeStep();
        
        Assert.assertEquals(nav.getWorkQueue().size(), 1, "There should be one rescheduled token");
        createdToken = nav.getWorkQueue().get(0);
        Assert.assertEquals(createdToken.getCurrentNode(), afterNode,
            "The discriminator should have fired again");
    }
}
