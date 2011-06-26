package org.jodaengine.ext.debugging;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jodaengine.RepositoryService;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentImpl;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.ext.debugging.util.AttributeKeyProvider;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This tests the {@link DebuggerArtifactServiceTest}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-13
 */
public class DebuggerArtifactServiceTest extends AbstractJodaEngineTest {
    
    private DebuggerService debugger;
    private RepositoryService repository;
    
    private ProcessDefinition mockDefinitionDeployed;
    private ProcessDefinition mockDefinitionUndeployed;
    
    private String artifact;
    
    /**
     * Setup.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @BeforeMethod
    public void setUp() throws ExtensionNotAvailableException {
        this.debugger = this.jodaEngineServices.getExtensionService().getServiceExtension(
            DebuggerService.class,
            DebuggerServiceImpl.DEBUGGER_SERVICE_NAME);
        Assert.assertNotNull(this.debugger);
        
        this.repository = this.jodaEngineServices.getRepositoryService();
        Assert.assertNotNull(this.repository);
        
        ProcessDefinitionID idDeployed = mock(ProcessDefinitionID.class);
        ProcessDefinitionID idUndeployed = mock(ProcessDefinitionID.class);
        Assert.assertFalse(idDeployed.equals(idUndeployed));
        
        this.mockDefinitionDeployed = mock(ProcessDefinition.class);
        this.mockDefinitionUndeployed = mock(ProcessDefinition.class);
        when(this.mockDefinitionDeployed.getID()).thenReturn(idDeployed);
        when(this.mockDefinitionUndeployed.getID()).thenReturn(idUndeployed);
        
        DebuggerAttribute attribute = new DebuggerAttribute();
        attribute.setSvgArtifact(idDeployed.toString());
        when(this.mockDefinitionDeployed.getAttribute(AttributeKeyProvider.getAttributeKey())).thenReturn(attribute);
        
        //
        // we deploy our definition
        //
        Deployment deployment = new DeploymentImpl();
        deployment.addProcessDefinition(this.mockDefinitionDeployed);
        this.repository.deployInNewScope(deployment);
        
        this.artifact = "It's inside.";
    }
    
    /**
     * Tests the adding and getting of a svg artifact.
     * 
     * @throws DefinitionNotFoundException test fails
     * @throws ProcessArtifactNotFoundException test fails
     */
    @Test
    public void testAddingAndGettingASvgArtifactForDeployedDefinition()
    throws ProcessArtifactNotFoundException, DefinitionNotFoundException {
        
        //
        // set a artifact and get it afterwards
        //
        this.debugger.setSvgArtifact(this.mockDefinitionDeployed, this.artifact);
        String art = this.debugger.getSvgArtifact(this.mockDefinitionDeployed);
        
        Assert.assertNotNull(art);
        Assert.assertTrue(this.artifact.equals(art));
    }
    
    /**
     * Tests the adding of a svg artifact for a undeployed definition.
     * 
     * @throws DefinitionNotFoundException expected
     * @throws ProcessArtifactNotFoundException test fails
     */
    @Test(expectedExceptions = DefinitionNotFoundException.class)
    public void testAddingASvgArtifactForUneployedDefinition()
    throws ProcessArtifactNotFoundException, DefinitionNotFoundException {
        
        this.debugger.setSvgArtifact(this.mockDefinitionUndeployed, this.artifact);
    }
    
    /**
     * Tests getting of a svg artifact for a deployed definition should throw an exception.
     * 
     * @throws DefinitionNotFoundException test fails
     * @throws ProcessArtifactNotFoundException expected
     */
    @Test(expectedExceptions = ProcessArtifactNotFoundException.class)
    public void testGettingNotAvailableSvgArtifactForDeployedDefinition()
    throws ProcessArtifactNotFoundException, DefinitionNotFoundException {
        
        this.debugger.getSvgArtifact(this.mockDefinitionDeployed);
    }
    
    /**
     * Tests getting of a svg artifact for a undeployed definition should throw an exception.
     * 
     * @throws DefinitionNotFoundException test fails
     * @throws ProcessArtifactNotFoundException expected
     */
    @Test(expectedExceptions = ProcessArtifactNotFoundException.class)
    public void testGettingNotAvailableSvgArtifactForUneployedDefinition()
    throws ProcessArtifactNotFoundException, DefinitionNotFoundException {
        
        this.debugger.getSvgArtifact(this.mockDefinitionUndeployed);
    }
}
