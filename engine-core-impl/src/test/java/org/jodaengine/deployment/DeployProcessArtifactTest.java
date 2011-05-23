package org.jodaengine.deployment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.process.definition.AbstractProcessArtifact;
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
 * Test class for deploying {@link AbstractProcessArtifact ProcessArtifacts}.
 */
public class DeployProcessArtifactTest extends AbstractJodaEngineTest {

    private static final String TEST_FILE_CLASSPATH = "org/jodaengine/deployment/deploying-file-artifact-test.file";
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
        definition = defBuilder.buildDefinition();
    }

    /**
     * Adds the definition to the deployment builder.
     */
    @BeforeMethod
    public void addDefinitionToBuilder() {

        deploymentBuilder.addProcessDefinition(definition);
    }

    @Test
    public void testArtifactDeploymentAsString()
    throws IOException, ProcessArtifactNotFoundException {

        // Deploying the String "Hello Joda-Engine ..."
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello Joda-Engine\n").append("\n").append("GoodBye Joda-Engine");

        Deployment deployment = deploymentBuilder.addStringArtifact("stringArtifact", stringBuilder.toString())
        .buildDeployment();
        repo.deployInNewScope(deployment);

        AbstractProcessArtifact processArtifact = repo.getProcessArtifact("stringArtifact", definition.getID());
        Assert.assertEquals(processArtifact.getID(), "stringArtifact");

        assertInputStream(processArtifact.getInputStream());
    }

    @Test
    public void testArtifactDeploymentAsFile()
    throws IOException, ProcessArtifactNotFoundException {

        File fileToDeploy = new File(TEST_FILE_SYSTEM_PATH);

        Deployment deployment = deploymentBuilder.addFileArtifact("fileArtifact", fileToDeploy).buildDeployment();
        repo.deployInNewScope(deployment);

        AbstractProcessArtifact processArtifact = repo.getProcessArtifact("fileArtifact", definition.getID());
        Assert.assertEquals(processArtifact.getID(), "fileArtifact");

        assertInputStream(processArtifact.getInputStream());
    }

    @Test
    public void testArtifactDeploymentAsClasspathResource()
    throws IOException, ProcessArtifactNotFoundException {

        Deployment deployment = deploymentBuilder
        .addClasspathResourceArtifact("classpathArtifact", TEST_FILE_CLASSPATH).buildDeployment();
        repo.deployInNewScope(deployment);

        AbstractProcessArtifact processArtifact = repo.getProcessArtifact("classpathArtifact", definition.getID());
        Assert.assertEquals(processArtifact.getID(), "classpathArtifact");

        assertInputStream(processArtifact.getInputStream());
    }

    @Test
    public void testArtifactDeploymentAsInputStream()
    throws IOException, ProcessArtifactNotFoundException {

        // Deploying the String "Hello Joda-Engine ..."
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello Joda-Engine\n").append("\n").append("GoodBye Joda-Engine");
        InputStream inputStreamToDeploy = new ByteArrayInputStream(stringBuilder.toString().getBytes());

        Deployment deployment = deploymentBuilder.addInputStreamArtifact("inputStreamArtifact", inputStreamToDeploy)
        .buildDeployment();
        repo.deployInNewScope(deployment);

        AbstractProcessArtifact processArtifact = repo.getProcessArtifact("inputStreamArtifact", definition.getID());
        Assert.assertEquals(processArtifact.getID(), "inputStreamArtifact");

        assertInputStream(processArtifact.getInputStream());
    }

    /**
     * Tests that two individually deployed process definitions (i.e. in different scopes) cannot access the artifacts
     * of each other.
     * 
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     * @throws ProcessArtifactNotFoundException
     *             the process artifact not found exception
     */
    @Test
    public void testArtifactIsolation()
    throws IllegalStarteventException, ProcessArtifactNotFoundException {

        // do the first deployment with a String artifact
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello Joda-Engine\n").append("\n").append("GoodBye Joda-Engine");

        Deployment deployment = deploymentBuilder.addStringArtifact("stringArtifact", stringBuilder.toString())
        .buildDeployment();
        repo.deployInNewScope(deployment);

        // do another deployment with a new process definition
        ProcessDefinitionBuilder defBuilder = deploymentBuilder.getProcessDefinitionBuilder();
        defBuilder.addStartInstantiationPattern(Mockito.mock(StartInstantiationPattern.class));
        ProcessDefinition anotherDefinition = defBuilder.buildDefinition();
        Deployment anotherDeployment = deploymentBuilder.addProcessDefinition(anotherDefinition).buildDeployment();
        repo.deployInNewScope(anotherDeployment);

        // the following line should trigger the expected exception.
        try {
            repo.getProcessArtifact("stringArtifact", anotherDefinition.getID());
            Assert.fail("Exception expected");
        } catch (ProcessArtifactNotFoundException exception) {
            // do nothing as this is the expected path
        }

    }

    private void assertInputStream(InputStream inputStream)
    throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Assert.assertEquals(bufferedReader.readLine(), "Hello Joda-Engine");
        Assert.assertEquals(bufferedReader.readLine(), "");
        Assert.assertEquals(bufferedReader.readLine(), "GoodBye Joda-Engine");
        Assert.assertNull(bufferedReader.readLine());
    }
}
