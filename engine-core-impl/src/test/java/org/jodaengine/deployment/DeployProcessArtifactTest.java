package org.jodaengine.deployment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.instantiation.StartInstantiationPattern;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
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

    @BeforeClass
    public void setUp()
    throws IllegalStarteventException {

        repo = ServiceFactory.getRepositoryService();
        deploymentBuilder = repo.getDeploymentBuilder();

        ProcessDefinitionBuilder defBuilder = deploymentBuilder.getProcessDefinitionBuilder();
        defBuilder.addStartInstantiationPattern(Mockito.mock(StartInstantiationPattern.class));
        definition = defBuilder.buildDefinition();
    }

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

    private void assertInputStream(InputStream inputStream)
    throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Assert.assertEquals(bufferedReader.readLine(), "Hello Joda-Engine");
        Assert.assertEquals(bufferedReader.readLine(), "");
        Assert.assertEquals(bufferedReader.readLine(), "GoodBye Joda-Engine");
        Assert.assertNull(bufferedReader.readLine());
    }
}
