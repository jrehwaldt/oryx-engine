package de.hpi.oryxengine.worklist;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.WorklistManager;
import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.instance.ProcessInstanceContextImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;
import de.hpi.oryxengine.worklist.pattern.SimplePullPattern;
import de.hpi.oryxengine.worklist.pattern.SimplePushPattern;

/**
 * 
 * @author Gery
 *
 */
public class HumanTaskUserStoryTest {

    Token token;
    Resource<?> resource;
    Node endNode;
    
    @BeforeMethod
    public void setUp()
    throws Exception {

        // Prepare the organisation structure

        IdentityBuilder identityBuilder = new IdentityServiceImpl().getIdentityBuilder();
        Participant participant = identityBuilder.createParticipant("jannik");
        participant.setName("Jannik Streek");

        resource = participant;
        
        // Define the task
        String subject = "Jannik, get Gerardo a cup of coffee!";
        String description = "You know what I mean.";

        Pattern pushPattern = new SimplePushPattern();
        Pattern pullPattern = new SimplePullPattern();

        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);

        Task task = new TaskImpl(subject, description, allocationStrategies, participant);

        Activity humanTask = new HumanTaskActivity(task);
        Node humanTaskNode = new NodeImpl(humanTask, new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());

        AbstractActivity end = new EndActivity();
        endNode = new NodeImpl(end);
        
        humanTaskNode.transitionTo(endNode);
                
        token = new TokenImpl(humanTaskNode, new ProcessInstanceContextImpl(), new NavigatorImplMock());
    }

    @Test
    public void testResumptionOfProcess() throws Exception {

        token.executeStep();
        
        WorklistItem worklistItem = WorklistManager.getWorklistService().getWorklistItems(resource).get(0);
        WorklistManager.getWorklistService().beginWorklistItem(worklistItem);
        
        WorklistManager.getWorklistService().completeWorklistItem(worklistItem);
        
        String failureMessage = "Token should point to the endNode, but it points to " + token.getCurrentNode().getID() + ".";
        assertEquals(endNode, token.getCurrentNode(), failureMessage);
    }
}
