package org.jodaengine.deployment.importer.definition.bpmn;

import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.testng.annotations.Test;


/**
 * It tests the deployment of an invalid BPMN process. The XMl contains a process definition tag but this one contains
 * any event or activity definitions.
 */
public class DeployInvalidProcessDefinitionAsBpmnXmlTest extends AbstractBPMNDeployerTest {

    public DeployInvalidProcessDefinitionAsBpmnXmlTest() {

        executableProcessResourcePath = "org/jodaengine/deployment/importer/definition/bpmn/InvalidProcessDefinition.bpmn.xml";
    }

    @Override
    @Test(expectedExceptions = JodaEngineRuntimeException.class)
    public void testCorrectProcessParsingOfXml()
    throws DefinitionNotFoundException {
    
        super.testCorrectProcessParsingOfXml();
    }

    @Override
    protected void assertProcessDefintion(ProcessDefinition processDefinition) {

    }
}
