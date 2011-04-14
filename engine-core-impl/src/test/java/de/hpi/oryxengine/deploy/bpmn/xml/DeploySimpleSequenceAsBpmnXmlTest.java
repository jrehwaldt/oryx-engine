package de.hpi.oryxengine.deploy.bpmn.xml;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.activity.impl.BPMNStartEvent;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.repository.DeploymentBuilder;
import de.hpi.oryxengine.repository.ProcessDefinitionImporter;
import de.hpi.oryxengine.repository.importer.BpmnXmlInpustreamImporter;
import de.hpi.oryxengine.util.ReflectionUtil;

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
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DeploySimpleSequenceAsBpmnXmlTest extends AbstractTestNGSpringContextTests {

    private static final String EXECUTABLE_PROCESS_RESOURCE_PATH = "de/hpi/oryxengine/delpoy/bpmn/xml/SimpleSequence.bpmn.xml";

    @Test
    public void importProcessXMlAsStream()
    throws DefinitionNotFoundException {

        DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();

        InputStream bpmnXmlInputStream = ReflectionUtil.getResourceAsStream(EXECUTABLE_PROCESS_RESOURCE_PATH);
        ProcessDefinitionImporter processDefinitionImporter = new BpmnXmlInpustreamImporter(bpmnXmlInputStream);
        UUID deployedProcessDefinitionUUID = deploymentBuilder.deployProcessDefinition(processDefinitionImporter);

        ProcessDefinition processDefinition = ServiceFactory.getRepositoryService().getProcessDefinition(
            deployedProcessDefinitionUUID);

        List<Node> startNodes = processDefinition.getStartNodes();
        Assert.assertTrue(startNodes.size() == 1);

        Node onlyStartNode = startNodes.get(0);
        Assert.assertTrue(onlyStartNode.getActivityBlueprint().getActivityClass() == BPMNStartEvent.class);
        Assert.assertEquals(onlyStartNode.getAttribute("name"), "Start");
        Assert.assertTrue(onlyStartNode.getOutgoingTransitions().size() == 1);

        Node nextNode = onlyStartNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertTrue(nextNode.getActivityBlueprint().getActivityClass() == AutomatedDummyActivity.class);
        Assert.assertEquals(nextNode.getAttribute("name"), "A");
        Assert.assertTrue(nextNode.getOutgoingTransitions().size() == 1);

        nextNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(nextNode.getAttribute("name"), "B");
        Assert.assertTrue(nextNode.getOutgoingTransitions().size() == 1);

        nextNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertEquals(nextNode.getAttribute("name"), "C");
        Assert.assertTrue(nextNode.getOutgoingTransitions().size() == 1);

        Node endNode = nextNode.getOutgoingTransitions().get(0).getDestination();
        Assert.assertTrue(endNode.getActivityBlueprint().getActivityClass() == EndActivity.class);
        Assert.assertEquals(endNode.getAttribute("name"), "End");
        Assert.assertTrue(endNode.getOutgoingTransitions().size() == 0);
    }

    public void importProcessXMlAsSring() {

    }
}
