package org.jodaengine.deployment.importer.definition.bpmn;

import java.util.List;

import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.activity.bpmn.BpmnEndEventActivity;
import org.jodaengine.node.activity.bpmn.BpmnStartEvent;
import org.jodaengine.node.activity.custom.AutomatedDummyActivity;
import org.jodaengine.node.incomingbehaviour.AndJoinBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.node.outgoingbehaviour.XORSplitBehaviour;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.testng.Assert;


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

        executableProcessResourcePath = "org/jodaengine/deployment/importer/definition/bpmn/SimpleGateways.bpmn.xml";
    }

    @Override
    protected void assertProcessDefintion(ProcessDefinition processDefinition) {

        List<Node> startNodes = processDefinition.getStartNodes();
        Assert.assertEquals(startNodes.size(), 1);

        Node onlyStartNode = startNodes.get(0);
        Assert.assertEquals(extractClass(onlyStartNode), BpmnStartEvent.class);
        Assert.assertEquals(onlyStartNode.getAttribute("name"), "Start");
        Assert.assertEquals(onlyStartNode.getOutgoingControlFlows().size(), 1);

        Node nextNode = onlyStartNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractClass(nextNode), AutomatedDummyActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "A");
        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 1);

        nextNode = nextNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractClass(nextNode), NullActivity.class);
        Assert.assertEquals(nextNode.getIncomingBehaviour().getClass(), SimpleJoinBehaviour.class);
        Assert.assertEquals(nextNode.getOutgoingBehaviour().getClass(), XORSplitBehaviour.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "Question?");
        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 2);
        Node xorGatewayNode = nextNode;

        nextNode = xorGatewayNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractClass(nextNode), NullActivity.class);
        Assert.assertEquals(nextNode.getIncomingBehaviour().getClass(), AndJoinBehaviour.class);
        Assert.assertEquals(nextNode.getOutgoingBehaviour().getClass(), TakeAllSplitBehaviour.class);
        Assert.assertNull(nextNode.getAttribute("name"));
        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 2);
        Node parallelGateway = nextNode;

        nextNode = parallelGateway.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractClass(nextNode), AutomatedDummyActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "B");
        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 1);
        Node parallelJoinNode = nextNode.getOutgoingControlFlows().get(0).getDestination();

        nextNode = parallelGateway.getOutgoingControlFlows().get(1).getDestination();
        Assert.assertEquals(extractClass(nextNode), AutomatedDummyActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "C");
        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 1);

        nextNode = nextNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(nextNode, parallelJoinNode);
        Assert.assertEquals(extractClass(nextNode), NullActivity.class);
        Assert.assertEquals(nextNode.getIncomingBehaviour().getClass(), AndJoinBehaviour.class);
        Assert.assertEquals(nextNode.getOutgoingBehaviour().getClass(), TakeAllSplitBehaviour.class);
        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 1);

        Node xorGatewayJoinNode = xorGatewayNode.getOutgoingControlFlows().get(1).getDestination();
        nextNode = nextNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(nextNode, xorGatewayJoinNode);
        Assert.assertEquals(extractClass(nextNode), NullActivity.class);
        Assert.assertEquals(nextNode.getIncomingBehaviour().getClass(), SimpleJoinBehaviour.class);
        Assert.assertEquals(nextNode.getOutgoingBehaviour().getClass(), XORSplitBehaviour.class);

        nextNode = xorGatewayJoinNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractClass(nextNode), AutomatedDummyActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "D");
        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 1);

        Node endNode = nextNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractClass(endNode), BpmnEndEventActivity.class);
        Assert.assertEquals(endNode.getAttribute("name"), "End");
        Assert.assertEquals(endNode.getOutgoingControlFlows().size(), 0);
    }

    /**
     * Little helper for this test in order make short method call.
     */
    public Class<? extends Activity> extractClass(Node onlyStartNode) {

        return onlyStartNode.getActivityBehaviour().getClass();
    }

}
