package org.jodaengine.deployment.importer.definition.bpmn;

import java.io.InputStream;

import org.jodaengine.ServiceFactory;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.ProcessDefinitionImporter;
import org.jodaengine.deployment.importer.definition.BpmnXmlImporter;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.util.ReflectionUtil;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * This class is designed to be inherited from. It provides a basic method body for testing the import of BPMN
 * serialized XML files.
 */
public abstract class AbstractBPMNDeployerTest extends AbstractJodaEngineTest {

    protected String executableProcessResourcePath;

    /**
     * Test correct process parsing of xml.
     * 
     * @throws DefinitionNotFoundException
     *             the definition not found exception
     */
    @Test
    public void testCorrectProcessParsingOfXml()
    throws DefinitionNotFoundException {

        if (executableProcessResourcePath == null) {
            String failureMessage = "Please set the variable 'executableProcessResourcePath' in the Test '"
                + this.getClass().getName() + "'.";
            Assert.fail(failureMessage);
        }

        DeploymentBuilder deploymentBuilder = jodaEngineServices.getRepositoryService().getDeploymentBuilder();

        InputStream bpmnXmlInputStream = ReflectionUtil.getResourceAsStream(executableProcessResourcePath);
        Assert.assertNotNull(bpmnXmlInputStream);
        ProcessDefinitionImporter processDefinitionImporter = new BpmnXmlImporter(bpmnXmlInputStream);
        ProcessDefinition definition = processDefinitionImporter.createProcessDefinition();
        ProcessDefinitionID deployedProcessDefinitionUUID = definition.getID();
        
        deploymentBuilder.addProcessDefinition(definition);
        Deployment deployment = deploymentBuilder.buildDeployment();
        ServiceFactory.getRepositoryService().deployInNewScope(deployment);

        ProcessDefinition processDefinition = jodaEngineServices.getRepositoryService().getProcessDefinition(
            deployedProcessDefinitionUUID);

        Assert.assertEquals(processDefinition.getID(), deployedProcessDefinitionUUID);

        assertProcessDefintion(processDefinition);
    }

    /**
     * Asserting the created {@link ProcessDefinition}. In this method you can check whether all properties have been
     * set correctly.
     * 
     * @param processDefinition
     *            - the {@link ProcessDefinition} that should be asserted
     */
    protected abstract void assertProcessDefintion(ProcessDefinition processDefinition);
}
