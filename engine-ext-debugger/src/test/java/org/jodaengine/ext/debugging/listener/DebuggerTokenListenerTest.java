package org.jodaengine.ext.debugging.listener;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.jodaengine.RepositoryService;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.importer.archive.DarImporter;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.util.ReflectionUtil;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This class tests the {@link DebuggerTokenListener} implementation.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-27
 */
public class DebuggerTokenListenerTest extends AbstractJodaEngineTest {

    private static final String RESOURCE_PATH = "org/jodaengine/ext/debugging/listener/";
    private static final String WORKING_PROCESS_AVAILABLE = RESOURCE_PATH + "WorkingProcessWithSvgAvailable.dar";
    

    
    /**
     * Setup.
     */
    @BeforeClass
    public void setUp() {
        
    }
    
    /**
     * Tests that the dar handler properly registers an svg artifact within the repository.
     * 
     * @throws ExtensionNotAvailableException test fails
     * @throws ProcessArtifactNotFoundException test fails
     * @throws DefinitionNotFoundException test fails
     * @throws URISyntaxException test fails, file uri not valid
     */
    @Test
    public void testDarHandlerRegisteringASvg()
    throws ExtensionNotAvailableException, ProcessArtifactNotFoundException, URISyntaxException,
    DefinitionNotFoundException {
        
        DebuggerService debugger = this.jodaEngineServices.getExtensionService().getExtensionService(
            DebuggerService.class,
            DebuggerService.DEBUGGER_SERVICE_NAME);
        Assert.assertNotNull(debugger);
        
        RepositoryService repository = this.jodaEngineServices.getRepositoryService();
        Assert.assertNotNull(repository);
        
        DarImporter importer = repository.getNewDarImporter();
        
        URL darFileUrl = ReflectionUtil.getResource(WORKING_PROCESS_AVAILABLE);
        File darFile = new File(new URI(darFileUrl.toString()));
        
        Assert.assertTrue(darFile.exists());
        Assert.assertTrue(darFile.canRead());
        
        //
        // get the deployment and deploy it in the repository
        //
        Deployment deployment = importer.importDarFile(darFile);
        Assert.assertNotNull(deployment);
        repository.deployInNewScope(deployment);
        
        //
        // ... start the instance and check that it's breakpoints will trigger
        //
        Assert.assertNotNull(deployment.getDefinitions());
        Assert.assertEquals(deployment.getDefinitions().size(), 1);
        for (ProcessDefinition definition: deployment.getDefinitions()) {
            
            DebuggerAttribute attribute = DebuggerAttribute.getAttributeIfExists(definition);
            Assert.assertNotNull(attribute);
            
            Assert.assertEquals(attribute.getSvgArtifact(), "svg1.svg");
            
            //
            // TODO extend to register breakpoint and listen for it
            //
        }
    }
}
