package org.jodaengine.deployment.importer.archive;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.jodaengine.allocation.Form;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.resource.allocation.FormImpl;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the import of .dar-Files (.dar stands for deployment archive).
 */
public class DarImporterTest extends AbstractJodaEngineTest {

    private static final String PROCESS_NAME = "sid-e22003a4-8a99-4777-82d7-e18f94caabcd";
    private static final String TEST_RESOURCE_PATH = "src/test/resources/org/jodaengine/deployment/importer/archive/";

    private static final String FORM_NAME = "form.html";
    private static final String FORM_CONTENT = "<html></html>";

    @BeforeMethod
    public void setUp() {

    }

    /**
     * Test definition import. Imports a dar-file that contains a process definition only.
     */
    @Test
    public void testDefinitionImportOnly() {

        DarImporter importer = new DarImporterImpl(jodaEngineServices.getRepositoryService());
        File darFile = new File(TEST_RESOURCE_PATH + "testDefinitionOnly.dar");
        Deployment deployment = importer.importDarFile(darFile);

        Set<ProcessDefinition> containedDefinitions = deployment.getDefinitions();
        Assert.assertEquals(containedDefinitions.size(), 1, "There should be one definition");
        ProcessDefinition definition = containedDefinitions.iterator().next();

        // TODO @Thorben keep UUID used here and the test file in sync
        Assert.assertEquals(definition.getName(), PROCESS_NAME,
            "The definition should have the desired id as specified in the files");
    }
//
//    /**
//     * Tests the import of a form as a process artifact.
//     */
//    @Test
//    public void testFormImport() {
//
//        DarImporter importer = new DarImporterImpl(jodaEngineServices.getRepositoryService());
//        File darFile = new File(TEST_RESOURCE_PATH + "testWithForms.dar");
//        Deployment deployment = importer.importDarFile(darFile);
//
//        Map<String, AbstractProcessArtifact> artifacts = deployment.getArtifacts();
//        Assert.assertTrue(artifacts.containsKey(FORM_NAME), "There should be an artifact with the desired name.");
//
//        Form form = new FormImpl(artifacts.get(FORM_NAME));
//        Assert.assertEquals(form.getFormContentAsHTML().trim(), FORM_CONTENT,
//            "The form should have been imported correctly");
//    }

    /**
     * Tests the import of custom classes.
     */
    @Test
    public void testCustomClassImport() {

        DarImporter importer = new DarImporterImpl(jodaEngineServices.getRepositoryService());
        File darFile = new File(TEST_RESOURCE_PATH + "testWithClasses.dar");
        Deployment deployment = importer.importDarFile(darFile);

        Assert.assertTrue(deployment.getClasses().containsKey("test.simple.Dummy"),
            "The class entry should exist in the deployment.");
        Assert.assertNotNull(deployment.getClasses().get("test.simple.Dummy"),
            "Class data should exist in the deployment");
    }
}
