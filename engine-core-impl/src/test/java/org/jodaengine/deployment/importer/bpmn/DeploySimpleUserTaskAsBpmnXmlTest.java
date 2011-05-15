package org.jodaengine.deployment.importer.bpmn;

import org.jodaengine.ServiceFactory;
import org.jodaengine.allocation.CreationPattern;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.bpmn.BpmnEndActivity;
import org.jodaengine.node.activity.bpmn.BpmnHumanTaskActivity;
import org.jodaengine.node.activity.bpmn.BpmnStartEvent;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.resource.AbstractParticipant;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;


/**
 * It tests the deployment of BPMN processes that where serialized as xml. The xml contains the structure the process
 * and additional information like DI-Information. The process contains only one UserTask that is associated to Jannik
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

    /**
     * Instantiates a new deploy simple user task as bpmn xml test, setting the path to the XML representation.
     */
    public DeploySimpleUserTaskAsBpmnXmlTest() {

        executableProcessResourcePath = "org/jodaengine/deployment/bpmn/SimpleUserTask.bpmn.xml";
    }

    /**
     * Creates the participant needed by the process.
     */
    @BeforeMethod
    public void setUp() {

        thorben = ServiceFactory.getIdentityService().getIdentityBuilder().createParticipant("Thorben");
    }

    @Override
    protected void assertProcessDefintion(ProcessDefinition processDefinition) {

        List<Node> startNodes = processDefinition.getStartNodes();
        Assert.assertEquals(startNodes.size(), 1);

        Node onlyStartNode = startNodes.get(0);
        Assert.assertEquals(extractActivityClass(onlyStartNode), BpmnStartEvent.class);
        Assert.assertEquals(onlyStartNode.getAttribute("name"), "Start");
        Assert.assertEquals(onlyStartNode.getOutgoingTransitions().size(), 1);

        Node nextNode = onlyStartNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(extractActivityClass(nextNode), BpmnHumanTaskActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "Thorben, please process this task!");
        Assert.assertEquals(nextNode.getAttribute("description"), "It is only a demo task.");

        // Asserting the task
        CreationPattern pattern = extractCreationPattern(nextNode);

        Assert.assertEquals(pattern.getItemSubject(), "Thorben, please process this task!");
        Assert.assertEquals(pattern.getItemDescription(), "It is only a demo task.");
        Assert.assertEquals(pattern.getAssignedResources()[0], thorben);

        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 1);

        Node endNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(extractActivityClass(endNode), BpmnEndActivity.class);
        Assert.assertEquals(endNode.getAttribute("name"), "End");
        Assert.assertEquals(endNode.getOutgoingTransitions().size(), 0);
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
