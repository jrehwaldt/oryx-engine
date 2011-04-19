package de.hpi.oryxengine.deployment.importer.bpmn;

import org.testng.annotations.Test;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * It tests the deployment of an invalid BPMN process. The XMl contains a process definition tag but this one contains
 * any event or activity definitions.
 */
public class DeployInvalidProcessDefinitionAsBpmnXmlTest extends AbstractBPMNDeployerTest {

    public DeployInvalidProcessDefinitionAsBpmnXmlTest() {

        executableProcessResourcePath = "de/hpi/oryxengine/deployment/bpmn/InvalidProcessDefinition.bpmn.xml";
    }

    @Override
    @Test(expectedExceptions = DalmatinaRuntimeException.class)
    public void testCorrectProcessParsingOfXml()
    throws DefinitionNotFoundException {
    
        super.testCorrectProcessParsingOfXml();
    }

    @Override
    protected void assertProcessDefintion(ProcessDefinition processDefinition) {

    }
}
