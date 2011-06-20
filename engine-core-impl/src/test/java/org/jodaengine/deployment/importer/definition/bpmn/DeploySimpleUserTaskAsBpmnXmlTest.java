package org.jodaengine.deployment.importer.definition.bpmn;

import java.util.List;

import org.jodaengine.ServiceFactory;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.bpmn.BpmnEndEventActivity;
import org.jodaengine.node.activity.bpmn.BpmnHumanTaskActivity;
import org.jodaengine.node.activity.bpmn.BpmnStartEvent;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractRole;
import org.jodaengine.resource.IdentityBuilder;
import org.jodaengine.resource.IdentityService;
import org.jodaengine.resource.allocation.CreationPattern;
import org.jodaengine.resource.allocation.pattern.creation.AbstractCreationPattern;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

/**
 * It tests the deployment of BPMN processes that where serialized as xml. The xml contains the structure the process
 * and additional information like DI-Information. The process contains only one UserTask that is associated to Thorben
 * telling him to get Gerardo a cup of coffee.
 * 
 * The process were modeled and exported as xml using academic signavio. The process contains a simple sequence.
 * 
 * The process can be inspected here (authkey important):
 * http://academic.signavio.com/p/model/ea45aa9318334c1680cf24a42532cc09/png?inline&authkey=
 * fdfb5839ba50b4b3cef089f2eec11f5ffdb4ebb298bff7ad2173de19aa3475
 * 
 */
public class DeploySimpleUserTaskAsBpmnXmlTest extends AbstractBPMNDeployerTest {

    private AbstractParticipant thorben;
    private AbstractRole aRole;

    /**
     * Instantiates a new deploy simple user task as bpmn xml test, setting the path to the XML representation.
     */
    public DeploySimpleUserTaskAsBpmnXmlTest() {

        executableProcessResourcePath = "org/jodaengine/deployment/importer/definition/bpmn/SimpleUserTask.bpmn.xml";
    }

    /**
     * Creates the participant needed by the process.
     */
    @BeforeMethod
    public void setUp() {

        IdentityBuilder identityBuilder = ServiceFactory.getIdentityService().getIdentityBuilder();
        thorben = identityBuilder.createParticipant("Thorben");
        aRole = identityBuilder.createRole("A Role");
    }

    @Override
    protected void assertProcessDefintion(ProcessDefinition processDefinition) {

        List<Node> startNodes = processDefinition.getStartNodes();
        Assert.assertEquals(startNodes.size(), 1);

        Node onlyStartNode = startNodes.get(0);
        Assert.assertEquals(extractActivityClass(onlyStartNode), BpmnStartEvent.class);
        Assert.assertEquals(onlyStartNode.getAttribute("name"), "Start");
        Assert.assertEquals(onlyStartNode.getOutgoingControlFlows().size(), 1);

        // Check the first user task
        Node nextNode = onlyStartNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractActivityClass(nextNode), BpmnHumanTaskActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "Thorben, please process this task!");
        Assert.assertEquals(nextNode.getAttribute("description"), "It is only a demo task.");

        // Asserting the task
        AbstractCreationPattern pattern = (AbstractCreationPattern) extractCreationPattern(nextNode);

        Assert.assertEquals(pattern.getItemSubject(), "Thorben, please process this task!");
        Assert.assertEquals(pattern.getItemDescription(), "It is only a demo task.");
        Assert.assertEquals(pattern.getItemFormID(), "form.html");
        Assert.assertEquals(pattern.getAssignedResources().iterator().next(), thorben);

        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 1);
        
        // check the second user task
        nextNode = nextNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractActivityClass(nextNode), BpmnHumanTaskActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "A Role, please process this task!");
        Assert.assertEquals(nextNode.getAttribute("description"), "It is only a demo task.");

        // Asserting the task
        pattern = (AbstractCreationPattern) extractCreationPattern(nextNode);

        Assert.assertEquals(pattern.getItemSubject(), "A Role, please process this task!");
        Assert.assertEquals(pattern.getItemDescription(), "It is only a demo task.");
        Assert.assertEquals(pattern.getItemFormID(), "form2.html");
        Assert.assertEquals(pattern.getAssignedResources().iterator().next(), aRole);

        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 1);
        

        Node endNode = nextNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractActivityClass(endNode), BpmnEndEventActivity.class);
        Assert.assertEquals(endNode.getAttribute("name"), "End");
        Assert.assertEquals(endNode.getOutgoingControlFlows().size(), 0);
    }

    private Class<? extends Activity> extractActivityClass(Node node) {
        return node.getActivityBehaviour().getClass();
    }

    /**
     * Doing a little Java Reflection. I like it that way. ;-)
     * @param node
     * @return
     */
    private CreationPattern extractCreationPattern(Node node) {
        
        BpmnHumanTaskActivity bpmnHumanTaskActivity = (BpmnHumanTaskActivity) node.getActivityBehaviour();
        
        return bpmnHumanTaskActivity.getCreationPattern();
    }
}
