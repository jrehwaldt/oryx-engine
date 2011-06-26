package org.jodaengine.deployment.importer.definition.petri;

import java.io.InputStream;

import org.jodaengine.ServiceFactory;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.ProcessDefinitionImporter;
import org.jodaengine.deployment.importer.definition.PetriNetXmlImporter;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.util.ReflectionUtil;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;

/**
 * This class is designed to be inherited from. It provides a basic method body for testing the import of PetriNet
 * serialized XML files.
 */
public abstract class AbstractPetriDeployerTest extends AbstractJodaEngineTest {

    protected String executableProcessResourcePath;

    /**
     * Test correct process parsing of xml.
     * @return 
     * 
     * @throws DefinitionNotFoundException
     *             the definition not found exception
     */
    public ProcessDefinition testCorrectProcessParsingOfXml()
    throws DefinitionNotFoundException {

        if (executableProcessResourcePath == null) {
            String failureMessage = "Please set the variable 'executableProcessResourcePath' in the Test '"
                + this.getClass().getName() + "'.";
            Assert.fail(failureMessage);
        }

        DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();

        InputStream petriXmlInputStream = ReflectionUtil.getResourceAsStream(executableProcessResourcePath);
        Assert.assertNotNull(petriXmlInputStream);
        ProcessDefinitionImporter processDefinitionImporter = new PetriNetXmlImporter(petriXmlInputStream);
        ProcessDefinition definition = processDefinitionImporter.createProcessDefinition();
        ProcessDefinitionID deployedProcessDefinitionUUID = definition.getID();
        
        deploymentBuilder.addProcessDefinition(definition);
        Deployment deployment = deploymentBuilder.buildDeployment();
        ServiceFactory.getRepositoryService().deployInNewScope(deployment);

        ProcessDefinition processDefinition = ServiceFactory.getRepositoryService().getProcessDefinition(
            deployedProcessDefinitionUUID);

        Assert.assertEquals(processDefinition.getID(), deployedProcessDefinitionUUID);
        return processDefinition;
    }

    /**
     * Asserting the created {@link ProcessDefinition}. In this method you can check whether all properties have been
     * set correctly.
     * 
     * @param processDefinition
     *            - the {@link ProcessDefinition} that should be asserted
     */
    public abstract void assertProcessDefintion() throws DefinitionNotFoundException;
}
