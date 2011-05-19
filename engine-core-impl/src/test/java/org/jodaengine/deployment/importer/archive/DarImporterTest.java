package org.jodaengine.deployment.importer.archive;

import java.io.File;
import java.util.Set;
import java.util.UUID;

import org.jodaengine.deployment.Deployment;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the import of .dar-Files (.dar stands for deployment archive).
 */
public class DarImporterTest extends AbstractJodaEngineTest {
    
    private static final String PROCESS_NAME = "sid-e22003a4-8a99-4777-82d7-e18f94caabcd";

    @BeforeMethod
    public void setUp() {

    }

    /**
     * Test definition import. Imports a dar-file that contains a process definition only.
     */
    @Test
    public void testDefinitionImportOnly() {

        DarImporter importer = new DarImporterImpl(jodaEngineServices.getRepositoryService());
        File darFile = new File("src/test/resources/org/jodaengine/deployment/importer/archive/test.dar");
        Deployment deployment = importer.importDarFile(darFile);

        Set<ProcessDefinition> containedDefinitions = deployment.getDefinitions();
        Assert.assertEquals(containedDefinitions.size(), 1, "There should be one definition");
        ProcessDefinition definition = containedDefinitions.iterator().next();

        // TODO @Thorben keep UUID used here and the test file in sync
        Assert.assertEquals(definition.getName(), PROCESS_NAME,
            "The definition should have the desired id as specified in the files");
    }
}
