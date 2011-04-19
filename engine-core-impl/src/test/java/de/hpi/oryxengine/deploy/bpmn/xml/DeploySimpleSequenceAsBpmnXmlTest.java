package de.hpi.oryxengine.deploy.bpmn.xml;

import java.util.List;

import org.testng.Assert;

import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.activity.impl.BpmnStartEvent;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;

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

        executableProcessResourcePath = "de/hpi/oryxengine/delpoy/bpmn/xml/SimpleSequence.bpmn.xml";
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
        Assert.assertEquals(nextNode.getAttribute("name"), "B");
        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 1);

        nextNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(nextNode.getAttribute("name"), "C");
        Assert.assertEquals(nextNode.getOutgoingTransitions().size(), 1);

        Node endNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(endNode.getActivityBlueprint().getActivityClass(), EndActivity.class);
        Assert.assertEquals(endNode.getAttribute("name"), "End");
        Assert.assertEquals(endNode.getOutgoingTransitions().size(), 0);
    }
}
