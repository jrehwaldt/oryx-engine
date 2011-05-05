package de.hpi.oryxengine.deployment.importer.bpmn;

import java.util.List;

import org.testng.Assert;

import de.hpi.oryxengine.node.activity.NullActivity;
import de.hpi.oryxengine.node.activity.bpmn.BpmnEndActivity;
import de.hpi.oryxengine.node.activity.bpmn.BpmnStartEvent;
import de.hpi.oryxengine.node.activity.custom.AutomatedDummyActivity;
import de.hpi.oryxengine.node.incomingbehaviour.AndJoinBehaviour;
import de.hpi.oryxengine.node.incomingbehaviour.SimpleJoinBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.XORSplitBehaviour;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;

/**
 * It tests the deployment of BPMN processes that where serialized as xml. The xml contains the structure the process
 * and additional information like DI-Information.
 * 
 * The process were modeled and exported as xml using academic signavio. The process contains two pairs of split and
 * join gateways.
 * 
 * The process can be inspected here (authkey important):
 * http://academic.signavio.com/p/model/b3f9ac0d3b4f46ff8f21426c0f641338
 * /png?inline&authkey=eb5d7e6bdadc19a3878bdfb0e31f93844160d2b779efe74d3a64a188a5ce31ff
 * 
 */
public class DeploySimpleGatewaysAsBpmnXmlTest extends AbstractBPMNDeployerTest {

    public DeploySimpleGatewaysAsBpmnXmlTest() {

        executableProcessResourcePath = "de/hpi/oryxengine/deployment/bpmn/SimpleGateways.bpmn.xml";
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
        Assert.assertEquals(nextNode.getActivityBlueprint().getActivityClass(), AutomatedDummyActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "A");
        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 1);

        nextNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(nextNode.getActivityBlueprint().getActivityClass(), NullActivity.class);
        Assert.assertEquals(nextNode.getIncomingBehaviour().getClass(), SimpleJoinBehaviour.class);
        Assert.assertEquals(nextNode.getOutgoingBehaviour().getClass(), XORSplitBehaviour.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "Question?");
        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 2);
        Node xorGatewayNode = nextNode;

        nextNode = xorGatewayNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(nextNode.getActivityBlueprint().getActivityClass(), NullActivity.class);
        Assert.assertEquals(nextNode.getIncomingBehaviour().getClass(), AndJoinBehaviour.class);
        Assert.assertEquals(nextNode.getOutgoingBehaviour().getClass(), TakeAllSplitBehaviour.class);
        Assert.assertNull(nextNode.getAttribute("name"));
        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 2);
        Node parallelGateway = nextNode;

        nextNode = parallelGateway.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(nextNode.getActivityBlueprint().getActivityClass(), AutomatedDummyActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "B");
        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 1);
        Node parallelJoinNode = nextNode.getOutgoingTransitions().get(0).getDestination();

        nextNode = parallelGateway.getOutgoingTransitions().get(1).getDestination();
        Assert.assertEquals(nextNode.getActivityBlueprint().getActivityClass(), AutomatedDummyActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "C");
        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 1);

        nextNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(nextNode, parallelJoinNode);
        Assert.assertEquals(nextNode.getActivityBlueprint().getActivityClass(), NullActivity.class);
        Assert.assertEquals(nextNode.getIncomingBehaviour().getClass(), AndJoinBehaviour.class);
        Assert.assertEquals(nextNode.getOutgoingBehaviour().getClass(), TakeAllSplitBehaviour.class);
        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 1);

        Node xorGatewayJoinNode = xorGatewayNode.getOutgoingTransitions().get(1).getDestination();
        nextNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(nextNode, xorGatewayJoinNode);
        Assert.assertEquals(nextNode.getActivityBlueprint().getActivityClass(), NullActivity.class);
        Assert.assertEquals(nextNode.getIncomingBehaviour().getClass(), SimpleJoinBehaviour.class);
        Assert.assertEquals(nextNode.getOutgoingBehaviour().getClass(), XORSplitBehaviour.class);

        nextNode = xorGatewayJoinNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(nextNode.getActivityBlueprint().getActivityClass(), AutomatedDummyActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "D");
        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 1);

        Node endNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(endNode.getActivityBlueprint().getActivityClass(), BpmnEndActivity.class);
        Assert.assertEquals(endNode.getAttribute("name"), "End");
        Assert.assertEquals(endNode.getOutgoingTransitions().size(), 0);
    }

}
