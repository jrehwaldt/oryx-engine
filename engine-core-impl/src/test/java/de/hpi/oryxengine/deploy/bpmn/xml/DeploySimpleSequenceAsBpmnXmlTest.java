package de.hpi.oryxengine.deploy.bpmn.xml;

import org.testng.annotations.Test;

import de.hpi.oryxengine.AbstractTest;

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
public class DeploySimpleSequenceAsBpmnXmlTest extends AbstractTest {

    private static final String EXECUTABLE_PROCESS_RESOURCE_PATH = 
                                "/de/hpi/oryxengine/delpoy/bpmn/xml/SimpleSequence.bpmn.xml";

    @Test
    public void importProcessXMlAsStream() {

        // TODO: [@Gerardo:] mal wieder auskommentieren
        // InputStream executableProcessInputStream =
        // ReflectionUtil.getResourceAsStream(EXECUTABLE_PROCESS_RESOURCE_PATH);
        //
        // ProcessDefinition processDefinition = ProcessImporter.createProcessOutOf(executableProcessInputStream);
        //
        // List<Node> startNodes = processDefinition.getStartNodes();
        // Assert.assertTrue(startNodes.size() == 1);
        //
        // Node onlyStartNode = startNodes.get(0);
        // Assert.assertTrue(onlyStartNode.getActivity() instanceof BPMNStartEvent);
        // Assert.assertEquals(onlyStartNode.get, "Start");

    }

    public void importProcessXMlAsSring() {

    }
}
