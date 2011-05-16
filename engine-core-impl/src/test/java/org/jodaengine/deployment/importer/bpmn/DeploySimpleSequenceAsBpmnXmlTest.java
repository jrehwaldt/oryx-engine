package org.jodaengine.deployment.importer.bpmn;

import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.bpmn.BpmnEndActivity;
import org.jodaengine.node.activity.bpmn.BpmnStartEvent;
import org.jodaengine.node.activity.custom.AutomatedDummyActivity;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;

import java.util.List;

import org.testng.Assert;


/**
 * It tests the deployment of BPMN processes that where serialized as xml. The xml contains the structure the process
 * and additional information like DI-Information.
 * 
 * The process were modeled and exported as xml using academic signavio. The process contains a simple sequence.
 * 
 * The process can be inspected here (authkey important):
 * http://academic.signavio.com/p/model/2ad124b5b3144284a6f84ec442ecb888/png?inline&authkey
 * =ce81b965df1ca08e19b4b5e72defb618783191defa3461c81b3e8b05be714
 * 
 */
public class DeploySimpleSequenceAsBpmnXmlTest extends AbstractBPMNDeployerTest {

    public DeploySimpleSequenceAsBpmnXmlTest() {

        executableProcessResourcePath = "org/jodaengine/deployment/bpmn/SimpleSequence.bpmn.xml";
    }

    @Override
    protected void assertProcessDefintion(ProcessDefinition processDefinition) {

        List<Node> startNodes = processDefinition.getStartNodes();
        Assert.assertEquals(startNodes.size(), 1);

        Node onlyStartNode = startNodes.get(0);
        Assert.assertEquals(extractClass(onlyStartNode), BpmnStartEvent.class);
        Assert.assertEquals(onlyStartNode.getAttribute("name"), "Start");
        Assert.assertEquals(onlyStartNode.getOutgoingTransitions().size(), 1);

        Node nextNode = onlyStartNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(extractClass(nextNode), AutomatedDummyActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "A");
        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 1);

        nextNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(nextNode.getAttribute("name"), "B");
        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 1);

        nextNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(nextNode.getAttribute("name"), "C");
        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 1);

        Node endNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(extractClass(endNode), BpmnEndActivity.class);
        Assert.assertEquals(endNode.getAttribute("name"), "End");
        Assert.assertEquals(endNode.getOutgoingTransitions().size(), 0);
    }

    public Class<? extends Activity> extractClass(Node node) {

        return node.getActivityBehaviour().getClass();
    }
}
