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
    
    /**
     * This test checks that after rebuilding the list of available extensions
     * any extension, which existed before will still be available.
     */
    @Test
    public void testAfterRebuildSameExtensionsExist() {
        
        List<BpmnXmlParseListener> beforeListeners = this.extensionService.getExtensions(BpmnXmlParseListener.class);
        Assert.assertTrue(beforeListeners.size() > 0);
        
        this.extensionService.rebuildExtensionDatabase(BpmnXmlParseListener.class);
        Assert.assertTrue(beforeListeners.size() > 0);
        
        List<BpmnXmlParseListener> afterListeners = this.extensionService.getExtensions(BpmnXmlParseListener.class);
        Assert.assertTrue(afterListeners.size() > 0);
        
        //
        // an instance of the extension class from the first run will exist
        //
        boolean containsListener = false;
        for (BpmnXmlParseListener beforeListener: beforeListeners) {
            for (BpmnXmlParseListener afterListener: afterListeners) {
                if (afterListener.getClass().equals(beforeListener.getClass())) {
                    containsListener = true;
                }
            }
        }
        
        Assert.assertTrue(containsListener);
    }
    
    /**
     * This test checks that each time the method {@link ExtensionService}#getExtensions(...)
     * is invoked a new instance is created.
     */
    @Test
    public void testGettingMultipleInstancesOnMultipleRunsOnGetExt() {
        
        List<BpmnXmlParseListener> beforeListeners = this.extensionService.getExtensions(BpmnXmlParseListener.class);
        Assert.assertTrue(beforeListeners.size() > 0);
        
        List<BpmnXmlParseListener> afterListeners = this.extensionService.getExtensions(BpmnXmlParseListener.class);
        Assert.assertTrue(afterListeners.size() > 0);
        
        //
        // no instance duplicates exist
        //
        for (BpmnXmlParseListener beforeListener: beforeListeners) {
            for (BpmnXmlParseListener afterListener: afterListeners) {
                Assert.assertNotSame(afterListener, beforeListener, "A dublicated listener instance was found");
            }
        }
    }
    
    /**
     * This test checks that each time the method {@link ExtensionService}#getExtensionService(...)
     * is invoked a new instance is created.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @Test
    public void testGettingOneInstanceOnMultipleRunsOfGetExtServices() throws ExtensionNotAvailableException {
        
        TestingExtensionService firstTestingService = this.extensionService.getExtensionService(
            TestingExtensionService.class,
            TestingExtensionService.DEMO_EXTENSION_SERVICE_NAME);
        
        Assert.assertNotNull(firstTestingService);
        
        TestingExtensionService secondTestingService = this.extensionService.getExtensionService(
            TestingExtensionService.class,
            TestingExtensionService.DEMO_EXTENSION_SERVICE_NAME);
        
        Assert.assertNotNull(secondTestingService);
        
        Assert.assertEquals(firstTestingService, secondTestingService);
    }
    
    /**
     * Tests that our testing extension types exist.
     */
    @Test
    public void testExistanceOfTestingExtensionTypes() {
        
        List<Class<BpmnXmlParseListener>> listenerClasses
            = this.extensionService.getExtensionTypes(BpmnXmlParseListener.class);
        Assert.assertTrue(listenerClasses.size() > 0);
    }
}
