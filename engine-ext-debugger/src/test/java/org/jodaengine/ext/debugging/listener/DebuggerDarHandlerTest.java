package org.jodaengine.ext.debugging.listener;

import static org.jodaengine.ext.debugging.api.DebuggerArtifactService.DEBUGGER_ARTIFACT_NAMESPACE;

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
 * This class tests the {@link DebuggerDarHandler} implementation.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-27
 */
public class DebuggerDarHandlerTest extends AbstractJodaEngineTest {
    
    private static final String RESOURCE_PATH = "org/jodaengine/ext/debugging/listener/";
    private static final String SVG_AVAILABLE = RESOURCE_PATH + "SvgAvailable.dar";
    
    private static final String SVG_EXPLICIT = DEBUGGER_ARTIFACT_NAMESPACE + "svg1.svg";
    private static final String SVG_IMPLICIT
        = DEBUGGER_ARTIFACT_NAMESPACE + "sid-e22003a4-8a99-4777-82d7-e18f94caabcd.svg";
    private static final String SVG_NOT_AVAILABLE = "sid-e22003a4-8a99-4777-82d7-notavailable";
    
    
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
        
        DebuggerService debugger = this.jodaEngineServices.getExtensionService().getServiceExtension(
            DebuggerService.class,
            DebuggerService.DEBUGGER_SERVICE_NAME);
        Assert.assertNotNull(debugger);
        
        RepositoryService repository = this.jodaEngineServices.getRepositoryService();
        Assert.assertNotNull(repository);
        
        DarImporter importer = repository.getNewDarImporter();
        
        URL darFileUrl = ReflectionUtil.getResource(SVG_AVAILABLE);
        File darFile = new File(new URI(darFileUrl.toString()));
        
        Assert.assertTrue(darFile.exists());
        Assert.assertTrue(darFile.canRead());
        
        //
        // get the deployment and deploy it in the repository
        //
        Deployment deployment = importer.importDarFile(darFile);
        Assert.assertNotNull(deployment);
        repository.deployInNewScope(deployment);
        
        Assert.assertNotNull(deployment.getArtifacts());
        Assert.assertEquals(deployment.getArtifacts().size(), 2);
        
        //
        // our artifact was found in the deployment
        //
        Assert.assertNotNull(deployment.getArtifacts().get(SVG_EXPLICIT));
        Assert.assertNotNull(deployment.getArtifacts().get(SVG_IMPLICIT));
        
        //
        // ... and it is also available via DebuggerService for each process instance
        //
        Assert.assertNotNull(deployment.getDefinitions());
        Assert.assertEquals(deployment.getDefinitions().size(), 2 + 1);
        for (ProcessDefinition definition: deployment.getDefinitions()) {
            
            DebuggerAttribute attribute = DebuggerAttribute.getAttributeIfExists(definition);
            Assert.assertNotNull(attribute);
            
            if (SVG_NOT_AVAILABLE.equals(definition.getID().getIdentifier())) {
                try {
                    debugger.getSvgArtifact(definition);
                    Assert.fail("We expected an exception here.");
                } catch (ProcessArtifactNotFoundException panfe) {
                    this.logger.debug("this behaviour is right");
                }
            } else {
                //
                // we'll get it two times to ensure that fetching it multiple times is possible
                //
                Assert.assertNotNull(debugger.getSvgArtifact(definition));
                Assert.assertNotNull(debugger.getSvgArtifact(definition));
            }
        }
    }
}
