package de.hpi.oryxengine.deployment.importer.bpmn;

import java.io.InputStream;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.hpi.oryxengine.AbstractJodaEngineTest;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.BpmnXmlInpustreamImporter;
import de.hpi.oryxengine.deployment.importer.ProcessDefinitionImporter;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.util.ReflectionUtil;

/**
 * This class is designed to be inherited from. It provides a basic method body for testing the import of BPMN
 * serialized XML files.
 */
public abstract class AbstractBPMNDeployerTest extends AbstractJodaEngineTest {

    protected String executableProcessResourcePath;

    @Test
    public void testCorrectProcessParsingOfXml()
    throws DefinitionNotFoundException {

        if (executableProcessResourcePath == null) {
            String failureMessage = "Please set the varibale 'executableProcessResourcePath' in the Test '"
                + this.getClass().getName() + "'.";
            Assert.fail(failureMessage);
        }

        DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();

        InputStream bpmnXmlInputStream = ReflectionUtil.getResourceAsStream(executableProcessResourcePath);
        Assert.assertNotNull(bpmnXmlInputStream);
        ProcessDefinitionImporter processDefinitionImporter = new BpmnXmlInpustreamImporter(bpmnXmlInputStream);
        UUID deployedProcessDefinitionUUID = deploymentBuilder.deployProcessDefinition(processDefinitionImporter);

        ProcessDefinition processDefinition = ServiceFactory.getRepositoryService().getProcessDefinition(
            deployedProcessDefinitionUUID);

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
