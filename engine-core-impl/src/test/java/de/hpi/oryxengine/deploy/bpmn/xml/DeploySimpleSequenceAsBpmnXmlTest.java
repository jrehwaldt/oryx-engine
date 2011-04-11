package de.hpi.oryxengine.deploy.bpmn.xml;

import java.io.InputStream;
import java.util.List;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.BPMNStartEvent;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
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
public class DeploySimpleSequenceAsBpmnXmlTest {
    
    private static final String executableProcessResourcePath = "/de/hpi/oryxengine/delpoy/bpmn/xml/SimpleSequence.bpmn.xml";
    
    @Test
    public void importProcessXMlAsStream() {
     // TODO: [@Gerardo:] mal wieder auskommentieren
//        InputStream executableProcessInputStream = ReflectionUtil.getResourceAsStream(executableProcessResourcePath);
//        
//        ProcessDefinition processDefinition = ProcessImporter.createProcessOutOf(executableProcessInputStream);
//        
//        List<Node> startNodes = processDefinition.getStartNodes();
//        Assert.assertTrue(startNodes.size() == 1);
//        
//        Node onlyStartNode = startNodes.get(0);
//        Assert.assertTrue(onlyStartNode.getActivity() instanceof BPMNStartEvent);
//        Assert.assertEquals(onlyStartNode.get, "Start");
        
        

        
        

    }
    public void importProcessXMlAsSring() {
        
    }
}
