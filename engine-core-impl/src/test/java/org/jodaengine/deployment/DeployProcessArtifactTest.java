package org.jodaengine.deployment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test class for deploying {@link AbstractProcessArtifact ProcessArtifacts}.
 */
public class DeployProcessArtifactTest extends AbstractJodaEngineTest {

    private static final String TEST_FILE_CLASSPATH = "org/jodaengine/deployment/deploying-file-artifact-test.file";
    private static final String TEST_FILE_SYSTEM_PATH = "src/test/resources/" + TEST_FILE_CLASSPATH;

    private DeploymentBuilder deploymentBuilder = null;
    private RepositoryService repo = null;

    @BeforeClass
    public void setUp() {

        repo = ServiceFactory.getRepositoryService();
        deploymentBuilder = repo.getDeploymentBuilder();
    }

    @Test
    public void testArtifactDeploymentAsString()
    throws IOException, ProcessArtifactNotFoundException {

        // Deploying the String "Hello Joda-Engine ..."
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello Joda-Engine\n").append("\n").append("GoodBye Joda-Engine");

        Deployment deployment = deploymentBuilder.addStringArtifact("stringArtifact", stringBuilder.toString())
        .buildDeployment();
        DeploymentScope scope = repo.deployInNewScope(deployment);

        AbstractProcessArtifact processArtifact = scope.getProcessArtifact("stringArtifact");
        Assert.assertEquals(processArtifact.getID(), "stringArtifact");

        assertInputStream(processArtifact.getInputStream());
    }

    @Test
    public void testArtifactDeploymentAsFile()
    throws IOException, ProcessArtifactNotFoundException {

        File fileToDeploy = new File(TEST_FILE_SYSTEM_PATH);

        Deployment deployment = deploymentBuilder.addFileArtifact("fileArtifact", fileToDeploy).buildDeployment();
        DeploymentScope scope = repo.deployInNewScope(deployment);

        AbstractProcessArtifact processArtifact = scope.getProcessArtifact("fileArtifact");
        Assert.assertEquals(processArtifact.getID(), "fileArtifact");

        assertInputStream(processArtifact.getInputStream());
    }

    @Test
    public void testArtifactDeploymentAsClasspathResource()
    throws IOException, ProcessArtifactNotFoundException {


        Deployment deployment = deploymentBuilder
        .addClasspathResourceArtifact("classpathArtifact", TEST_FILE_CLASSPATH).buildDeployment();
        DeploymentScope scope = repo.deployInNewScope(deployment);

        AbstractProcessArtifact processArtifact = scope.getProcessArtifact("classpathArtifact");
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
        
        Deployment deployment = deploymentBuilder
        .addInputStreamArtifact("inputStreamArtifact", inputStreamToDeploy).buildDeployment();
        DeploymentScope scope = repo.deployInNewScope(deployment);

        AbstractProcessArtifact processArtifact = scope.getProcessArtifact("inputStreamArtifact");
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
