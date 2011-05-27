package org.jodaengine.deployment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.allocation.Form;
import org.jodaengine.allocation.JodaFormField;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.process.activation.ProcessDefinitionDeActivationPattern;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.instantiation.StartInstantiationPattern;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Thes the deployment of forms using the {@link RepositoryService}.
 */
public class DeployFormTest extends AbstractJodaEngineTest {
    
    private static final String TEST_FILE_CLASSPATH = "org/jodaengine/deployment/testForm.html";
    private static final String TEST_FILE_SYSTEM_PATH = "src/test/resources/" + TEST_FILE_CLASSPATH;

    private DeploymentBuilder deploymentBuilder = null;
    private RepositoryService repo = null;
    private ProcessDefinition definition = null;
    
    /**
     * Sets services and builds a standard definition.
     * 
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     */
    @BeforeClass
    public void setUp()
    throws IllegalStarteventException {

        repo = ServiceFactory.getRepositoryService();
        deploymentBuilder = repo.getDeploymentBuilder();

        ProcessDefinitionBuilder defBuilder = deploymentBuilder.getProcessDefinitionBuilder();
        defBuilder.addStartInstantiationPattern(Mockito.mock(StartInstantiationPattern.class));
        defBuilder.addActivationPattern(Mockito.mock(ProcessDefinitionDeActivationPattern.class));
        definition = defBuilder.buildDefinition();
    }

    /**
     * Adds the definition to the deployment builder.
     */
    @BeforeMethod
    public void addDefinitionToBuilder() {

        deploymentBuilder.addProcessDefinition(definition);
    }

    /**
     * Deploys a form and checks the existence of the parsed form fields.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ProcessArtifactNotFoundException the process artifact not found exception
     */
    @Test
    public void testArtifactDeploymentAsString()
    throws IOException, ProcessArtifactNotFoundException {

        FileInputStream stream = new FileInputStream(new File(TEST_FILE_SYSTEM_PATH));
        Deployment deployment = deploymentBuilder.addInputStreamForm("form", stream).buildDeployment();
        repo.deployInNewScope(deployment);

        Form form = repo.getForm("form", definition.getID());
        List<JodaFormField> fields = form.getFormFields();
        Assert.assertEquals(fields.size(), 2);

        JodaFormField field1, field2;
        field1 = fields.get(0);
        field2 = fields.get(1);
        if (field1.getName() != "claimPoint1") {
            field1 = fields.get(1);
            field2 = fields.get(0);
        }
        if (!"claimPoint1".equals(field1.getName()) || !"claimPoint2".equals(field2.getName())) {
            Assert.fail("Fields were not read correctly");
        }
        
        
    }
}
