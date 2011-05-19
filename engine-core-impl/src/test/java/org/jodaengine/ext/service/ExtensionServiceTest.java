package org.jodaengine.ext.service;

import java.util.List;

import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class tests the {@link ExtensionServiceImpl}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
public class ExtensionServiceTest extends AbstractJodaEngineTest {
    
    private ExtensionService extensionService = null;
    
    /**
     * Setup.
     */
    @BeforeMethod
    public void setUp() {
        this.extensionService = this.jodaEngineServices.getExtensionService();
    }
    
    /**
     * Tests getting an unavailable extension.
     * 
     * @throws ExtensionNotAvailableException expected
     */
    @Test(expectedExceptions = ExtensionNotAvailableException.class)
    public void testGettingUnavailableExtension()
    throws ExtensionNotAvailableException {
        this.extensionService.getExtensionService(
            TestingExtensionService.class,
            TestingExtensionService.DEMO_EXTENSION_SERVICE_NAME + "-not-available");
    }
    
    /**
     * Tests getting an available extension.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @Test
    public void testGettingAvailableExtension()
    throws ExtensionNotAvailableException {
        
        TestingExtensionService service = this.extensionService.getExtensionService(
            TestingExtensionService.class,
            TestingExtensionService.DEMO_EXTENSION_SERVICE_NAME);
        
        Assert.assertNotNull(service);
    }
    
    /**
     * Tests initialization of extension service.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @Test
    public void testInitializationOfExtension()
    throws ExtensionNotAvailableException {
        
        TestingExtensionService service = this.extensionService.getExtensionService(
            TestingExtensionService.class,
            TestingExtensionService.DEMO_EXTENSION_SERVICE_NAME);
        
        Assert.assertNotNull(service);
        Assert.assertTrue(service.isStarted());
        Assert.assertTrue(service.isRunning());
        Assert.assertFalse(service.isStopped());
        Assert.assertNotNull(service.getServices());
        Assert.assertEquals(this.jodaEngineServices, service.getServices());
    }
    
    /**
     * This test checks the availability of a {@link BpmnXmlParseListener} implementation
     * via the {@link ExtensionService} and the proper calling of it's constructor.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @Test
    public void testProvidingAExampleListener()
    throws ExtensionNotAvailableException {
        List<BpmnXmlParseListener> listeners = this.extensionService.getExtensions(BpmnXmlParseListener.class);
        
        Assert.assertTrue(listeners.size() > 0);
        
        boolean listenerAvailable = false;
        for (BpmnXmlParseListener listener: listeners) {
            if (listener instanceof TestingDeploymentListener) {
                TestingDeploymentListener testingListener = (TestingDeploymentListener) listener;
                listenerAvailable = true;

                Assert.assertNotNull(testingListener.extension);
                Assert.assertNotNull(testingListener.navigator);
                Assert.assertNotNull(testingListener.repository);
                Assert.assertNotNull(testingListener.services);
                Assert.assertNotNull(testingListener.testing);
                Assert.assertNotNull(testingListener.worklist);

                Assert.assertEquals(this.jodaEngineServices, testingListener.services);
                Assert.assertEquals(this.jodaEngineServices.getExtensionService(), testingListener.extension);
                Assert.assertEquals(this.jodaEngineServices.getNavigatorService(), testingListener.navigator);
                Assert.assertEquals(this.jodaEngineServices.getWorklistService(), testingListener.worklist);
                Assert.assertEquals(this.jodaEngineServices.getRepositoryService(), testingListener.repository);
                
                TestingExtensionService testingService = this.extensionService.getExtensionService(
                    TestingExtensionService.class,
                    TestingExtensionService.DEMO_EXTENSION_SERVICE_NAME);
                
                Assert.assertNotNull(testingService, "This will also fails another test and is just a backup assert.");
                
                Assert.assertEquals(testingService, testingListener.testing);
            }
        }
        
        Assert.assertTrue(listenerAvailable);
    }
}
