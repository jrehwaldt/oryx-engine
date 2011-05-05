package de.hpi.oryxengine.deployment.importer.bpmn;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.node.activity.bpmn.BpmnEndActivity;
import de.hpi.oryxengine.node.activity.bpmn.BpmnHumanTaskActivity;
import de.hpi.oryxengine.node.activity.bpmn.BpmnStartEvent;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.resource.AbstractParticipant;

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

        executableProcessResourcePath = "de/hpi/oryxengine/deployment/bpmn/SimpleUserTask.bpmn.xml";
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
        Assert.assertEquals(onlyStartNode.getActivityBlueprint().getActivityClass(), BpmnStartEvent.class);
        Assert.assertEquals(onlyStartNode.getAttribute("name"), "Start");
        Assert.assertEquals(onlyStartNode.getOutgoingTransitions().size(), 1);

        Node nextNode = onlyStartNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(nextNode.getActivityBlueprint().getActivityClass(), BpmnHumanTaskActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "Thorben, please process this task!");
        Assert.assertEquals(nextNode.getAttribute("description"), "It is only a demo task.");
        
        // Asserting the task
        Task task = (Task) nextNode.getActivityBlueprint().getParameters()[0];
        Assert.assertEquals(task.getSubject(), "Thorben, please process this task!");
        Assert.assertEquals(task.getDescription(), "It is only a demo task.");
        Assert.assertEquals(task.getAssignedResources().iterator().next(), thorben);
        
        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 1);

        Node endNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(endNode.getActivityBlueprint().getActivityClass(), BpmnEndActivity.class);
        Assert.assertEquals(endNode.getAttribute("name"), "End");
        Assert.assertEquals(endNode.getOutgoingTransitions().size(), 0);
    }
}
