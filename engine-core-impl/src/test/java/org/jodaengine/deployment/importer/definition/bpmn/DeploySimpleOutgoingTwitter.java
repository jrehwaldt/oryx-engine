package org.jodaengine.deployment.importer.definition.bpmn;

import java.util.List;


import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.bpmn.BpmnEndEventActivity;
import org.jodaengine.node.activity.bpmn.BpmnStartEvent;
import org.jodaengine.node.activity.custom.TweetActivity;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.testng.Assert;


/**
 * It tests the deployment of BPMN processes that where serialized as xml. The xml contains the structure the process
 * This process only contains a start and an end event, and inbetween a little TweetActivity.
 * 
 */
public class DeploySimpleOutgoingTwitter extends AbstractBPMNDeployerTest {

    /**
     * Instantiates a new deploy simple user task as bpmn xml test, setting the path to the XML representation.
     */
    public DeploySimpleOutgoingTwitter() {

        executableProcessResourcePath = 
            "org/jodaengine/deployment/importer/definition/bpmn/SimpleIntermediateTwitterOutgoing.bpmn.xml";
    }

    @Override
    protected void assertProcessDefintion(ProcessDefinition processDefinition) {

        List<Node> startNodes = processDefinition.getStartNodes();
        Assert.assertEquals(startNodes.size(), 1);

        Node onlyStartNode = startNodes.get(0);
        Assert.assertEquals(extractActivityClass(onlyStartNode), BpmnStartEvent.class);
        Assert.assertEquals(onlyStartNode.getAttribute("name"), "Start");
        Assert.assertEquals(onlyStartNode.getOutgoingControlFlows().size(), 1);

        Node nextNode = onlyStartNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractActivityClass(nextNode), TweetActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "Twitter");

        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 1);

        Node endNode = nextNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractActivityClass(endNode), BpmnEndEventActivity.class);
        Assert.assertEquals(endNode.getAttribute("name"), "End");
        Assert.assertEquals(endNode.getOutgoingControlFlows().size(), 0);
    }

    /**
     * Extract the class of a given activity.
     *
     * @param node the node
     * @return the class<? extends activity>
     */
    private Class<? extends Activity> extractActivityClass(Node node) {
        return node.getActivityBehaviour().getClass();
    }
}
