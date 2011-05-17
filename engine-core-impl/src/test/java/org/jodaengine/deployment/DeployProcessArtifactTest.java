package org.jodaengine.deployment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.util.testing.AbstractJodaEngineTest;


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
    throws DefinitionNotFoundException, IOException {

        // Deploying the String "Hello Joda-Engine ..."
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello Joda-Engine\n").append("\n").append("GoodBye Joda-Engine");

        UUID stringArtifactUUID = deploymentBuilder.deployArtifactAsString("stringArtifact", stringBuilder.toString());

        AbstractProcessArtifact processArtifact = repo.getProcessResource(stringArtifactUUID);
        Assert.assertEquals(processArtifact.getName(), "stringArtifact");

        assertInputStream(processArtifact.getInputStream());
    }


    @Test
    public void testArtifactDeploymentAsFile() throws DefinitionNotFoundException, IOException {
        
        File fileToDeploy = new File(TEST_FILE_SYSTEM_PATH);
        UUID fileArtifactUUID = deploymentBuilder.deployArtifactAsFile("fileArtifact", fileToDeploy);

        AbstractProcessArtifact processArtifact = repo.getProcessResource(fileArtifactUUID);
        Assert.assertEquals(processArtifact.getName(), "fileArtifact");
        
        assertInputStream(processArtifact.getInputStream());
    }
    
    @Test
    public void testArtifactDeploymentAsClasspathResource() throws DefinitionNotFoundException, IOException {
        
        
        UUID classpathArtifactUUID = deploymentBuilder.deployArtifactAsClasspathResource("classpathArtifact",
            TEST_FILE_CLASSPATH);
        
        AbstractProcessArtifact processArtifact = repo.getProcessResource(classpathArtifactUUID);
        Assert.assertEquals(processArtifact.getName(), "classpathArtifact");
        
        assertInputStream(processArtifact.getInputStream());
    }
    
    @Test
    public void testArtifactDeploymentAsInputStream() throws DefinitionNotFoundException, IOException {
        
        // Deploying the String "Hello Joda-Engine ..."
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello Joda-Engine\n").append("\n").append("GoodBye Joda-Engine");
        InputStream inputStreamToDeploy = new ByteArrayInputStream(stringBuilder.toString().getBytes());
            
        UUID inputStreamArtifactUUID = deploymentBuilder.deployArtifactAsInputStream("inputStreamArtifact",
            inputStreamToDeploy);
        
        AbstractProcessArtifact processArtifact = repo.getProcessResource(inputStreamArtifactUUID);
        Assert.assertEquals(processArtifact.getName(), "inputStreamArtifact");
        
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
