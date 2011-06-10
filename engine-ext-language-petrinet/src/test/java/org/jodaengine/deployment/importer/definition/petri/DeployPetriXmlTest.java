package org.jodaengine.deployment.importer.definition.petri;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.bpmn.BpmnEndEventActivity;
import org.jodaengine.node.activity.bpmn.BpmnStartEvent;
import org.jodaengine.node.activity.custom.AutomatedDummyActivity;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.petri.PetriProcessDefinitionBuilder;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.util.testing.JodaEngineTestConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;


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
@JodaEngineTestConfiguration(configurationFile = "petriNetjodaengine.cfg.xml")
public class DeployPetriXmlTest extends AbstractPetriDeployerTest {

    public DeployPetriXmlTest() {

        executableProcessResourcePath = "Token-example.pnml";
    }

    @Test
    @Override
    public void assertProcessDefintion() throws DefinitionNotFoundException {

        ProcessDefinition processDefinition = super.testCorrectProcessParsingOfXml();
        
        Assert.assertEquals(processDefinition.getDescription(), "Token-Example");

        List<Node> startNodes = processDefinition.getStartNodes();
        Assert.assertEquals(startNodes.size(), 1);

        Node onlyStartNode = startNodes.get(0);
        Assert.assertEquals(onlyStartNode.getAttribute("name"), "p1");
        Assert.assertEquals(onlyStartNode.getOutgoingControlFlows().size(), 1);

        Node nextNode = onlyStartNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 1);

        nextNode = nextNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(nextNode.getAttribute("name"), "p2");
        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 0);

    }
    
    /**
     * In this Test, a very simple petri net is: 1) read from a file 2) parsed 3) deployed and then 4) executed.
     *
     * @throws DefinitionNotFoundException the definition not found exception
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void testDeployAndExecution() throws DefinitionNotFoundException, InterruptedException {
        ProcessDefinition processDefinition = super.testCorrectProcessParsingOfXml();

        ProcessDefinitionID id = processDefinition.getID();
        
        AbstractProcessInstance processInstance = jodaEngineServices.getNavigatorService().startProcessInstance(
                    id);

        Thread.sleep(1000);

        assertEquals(processInstance.getAssignedTokens().size(), 1);
        assertEquals(processInstance.getAssignedTokens().get(0).getCurrentNode().getAttribute("name"), "p2");
    }
    

}
