package org.jodaengine.deployment.importer.archive;

import java.io.File;

import org.jodaengine.deployment.Deployment;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the registration of {@link BpmnXmlParseListener}s through the {@link ExtensionService}.
 */
public class DefinitionHandlerTest extends AbstractJodaEngineTest {

    private static final String DAR_RESOURCE_PATH = "src/test/resources/org/jodaengine/deployment/importer/archive/";

    /**
     * Test of the registration and use of the {@link TestingBpmnXmlParseListener}.
     * 
     * @throws IOException
     * @throws ZipException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    @Test
    public void testParseListenerRegistration() {

        File darFile = new File(DAR_RESOURCE_PATH + "testDefinitionOnly.dar");
        DarImporter importer = new DarImporterImpl(jodaEngineServices.getRepositoryService(),
            jodaEngineServices.getExtensionService());
        Deployment deployment = importer.importDarFile(darFile);
        ProcessDefinition definition = deployment.getDefinitions().iterator().next();
        Assert.assertTrue((Boolean) definition.getAttribute("testListenerCalled"));
    }
}
