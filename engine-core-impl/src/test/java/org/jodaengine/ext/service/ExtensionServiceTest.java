package org.jodaengine.ext.service;

import java.io.File;
import java.util.List;

import org.jodaengine.RepositoryService;
import org.jodaengine.bootstrap.Service;
import org.jodaengine.deployment.importer.archive.AbstractDarHandler;
import org.jodaengine.deployment.importer.archive.DarImporter;
import org.jodaengine.deployment.importer.archive.DarImporterImpl;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.ext.AbstractListenable;
import org.jodaengine.ext.listener.AbstractNavigatorListener;
import org.jodaengine.ext.listener.AbstractSchedulerListener;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorImpl;
import org.jodaengine.node.factory.ControlFlowFactory;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.structure.Node;
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
    
    private static final String DAR_RESOURCE_PATH = "src/test/resources/org/jodaengine/ext/listener/";
    
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
            if (listener instanceof TestingBpmnXmlParseListener) {
                TestingBpmnXmlParseListener testingListener = (TestingBpmnXmlParseListener) listener;
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
                
                Assert.assertNotNull(testingListener.testing);
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
        Assert.assertTrue(this.extensionService.isExtensionAvailable(BpmnXmlParseListener.class));
        
        List<Class<BpmnXmlParseListener>> bpmnListenerClasses
            = this.extensionService.getExtensionTypes(BpmnXmlParseListener.class);
        Assert.assertTrue(bpmnListenerClasses.size() > 0);
        
        Assert.assertTrue(this.extensionService.isExtensionAvailable(AbstractDarHandler.class));
        
        List<Class<AbstractDarHandler>> darListenerClasses
            = this.extensionService.getExtensionTypes(AbstractDarHandler.class);
        Assert.assertTrue(darListenerClasses.size() > 0);
    }
    
    /**
     * Tests the proper creation of specified web services.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @Test
    public void testCreationOfWebServiceSingletons()
    throws ExtensionNotAvailableException {
        
        TestingWebExtensionService testing = this.extensionService.getExtensionService(
            TestingWebExtensionService.class,
            TestingWebExtensionService.DEMO_EXTENSION_SERVICE_NAME);
        
        Assert.assertNotNull(testing);
        
        List<Service> webServices = this.extensionService.getExtensionWebServiceSingletons();
        
        Assert.assertNotNull(webServices);
        Assert.assertFalse(webServices.isEmpty());
        
        boolean containsWebService = false;
        for (Service webService: webServices) {
            Assert.assertNotNull(webService);
            
            if (webService instanceof TestingWebService) {
                containsWebService = true;
                TestingWebService testingWebService = (TestingWebService) webService;
                
                Assert.assertEquals(testing, testingWebService.getTesting());
                Assert.assertEquals(this.jodaEngineServices, testingWebService.getServices());
            }
        }
        
        Assert.assertTrue(containsWebService);
    }
    
    /**
     * Tests that the navigator extension is successfully found and
     * registered within our navigator.
     */
    @Test
    public void testNavigatorListenerIntegration() {
        
        List<Class<AbstractNavigatorListener>> listenerTypes
            = this.extensionService.getExtensionTypes(AbstractNavigatorListener.class);
        
        Assert.assertTrue(listenerTypes.contains(TestingNavigatorListener.class));
        
        NavigatorImpl navigator = (NavigatorImpl) this.jodaEngineServices.getNavigatorService();
        
        Assert.assertNotNull(navigator);
        Assert.assertTrue(navigator.isRunning());
        
        List<AbstractNavigatorListener> listeners = navigator.getListeners();
        
        //
        // check that each available listener type is registered as listener instance
        //
        forEachType : for (Class<AbstractNavigatorListener> listenerType: listenerTypes) {
            for (AbstractNavigatorListener listener: listeners) {
                Assert.assertNotNull(listener);
                
                if (listenerType.equals(listener.getClass())) {
                    continue forEachType;
                }
            }
            
            Assert.fail("No listener instance found for type " + listenerType);
        }
    }
    
    /**
     * Tests that the scheduler extension is successfully found and
     * registered within our navigator.
     */
    @Test
    public void testSchedulerListenerIntegration() {
        
        List<Class<AbstractSchedulerListener>> listenerTypes
            = this.extensionService.getExtensionTypes(AbstractSchedulerListener.class);
        
        Assert.assertTrue(listenerTypes.contains(TestingSchedulerListener.class));
        
        NavigatorImpl navigator = (NavigatorImpl) this.jodaEngineServices.getNavigatorService();
        
        Assert.assertNotNull(navigator);
        Assert.assertTrue(navigator.isRunning());
        
        AbstractListenable<AbstractSchedulerListener> scheduler
            = (AbstractListenable<AbstractSchedulerListener>) navigator.getScheduler();
        
        List<AbstractSchedulerListener> listeners = scheduler.getListeners();
        
        //
        // check that each available listener type is registered as listener instance
        //
        forEachType : for (Class<AbstractSchedulerListener> listenerType: listenerTypes) {
            for (AbstractSchedulerListener listener: listeners) {
                Assert.assertNotNull(listener);
                
                if (listenerType.equals(listener.getClass())) {
                    continue forEachType;
                }
            }
            
            Assert.fail("No listener instance found for type " + listenerType);
        }
    }
    
    /**
     * Creating a {@link DarImporterImpl} should not fail when no {@link ExtensionService} is available.
     */
    @Test
    public void testCreatingADarImporterWithoutExtensionManager() {
        DarImporter importer = new DarImporterImpl(this.jodaEngineServices.getRepositoryService(), null);
        Assert.assertNotNull(importer);
    }
    
    /**
     * Test the proper registration of {@link AbstractDarHandler} within our {@link DarImporter}.
     * 
     * @throws IllegalAccessException test fails
     * @throws ExtensionNotAvailableException test fails
     */
    @Test
    public void testRegisteringOfDarHandler() throws IllegalAccessException, ExtensionNotAvailableException {
        
        TestingListenerExtensionService listenerService = this.extensionService.getExtensionService(
            TestingListenerExtensionService.class,
            TestingListenerExtensionService.DEMO_EXTENSION_SERVICE_NAME);
        
        Assert.assertNotNull(listenerService);
        
        DarImporter importer = this.jodaEngineServices.getRepositoryService().getNewDarImporter();
        File darFile = new File(DAR_RESOURCE_PATH + "deployment/testDefinitionOnly.dar");
        
        //
        // we need to process the dar for the handler to be invoked
        //
        importer.importDarFile(darFile);

        Assert.assertTrue(listenerService.hasBeenRegistered(TestingDarHandler.class));
        Assert.assertTrue(listenerService.hasBeenInvoked(TestingDarHandler.class));
    }
    
    /**
     * Test of the registration and use of the {@link TestingBpmnXmlParseListener}.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @Test
    public void testParseListenerRegistrationInDarImporter() throws ExtensionNotAvailableException {
        
        TestingListenerExtensionService listenerService = this.extensionService.getExtensionService(
            TestingListenerExtensionService.class,
            TestingListenerExtensionService.DEMO_EXTENSION_SERVICE_NAME);
        
        Assert.assertNotNull(listenerService);
        
        File darFile = new File(DAR_RESOURCE_PATH + "deployment/testDefinitionOnly.dar");
        DarImporter importer = new DarImporterImpl(
            this.jodaEngineServices.getRepositoryService(),
            this.jodaEngineServices.getExtensionService());
        
        //
        // we need to process the dar for the handler to be invoked
        //
        importer.importDarFile(darFile);
        
        Assert.assertTrue(listenerService.hasBeenRegistered(TestingBpmnXmlParseListener.class));
        Assert.assertTrue(listenerService.hasBeenInvoked(TestingBpmnXmlParseListener.class));
    }
    
    /**
     * Test of the registration and use of the {@link TestingTokenListener}.
     * 
     * @throws IllegalStarteventException test fails
     * @throws ExtensionNotAvailableException test fails
     * @throws DefinitionNotFoundException test fails
     */
    @Test
    public void testTokenListenerIntegrationInNavigatorForBpmnToken()
    throws IllegalStarteventException, ExtensionNotAvailableException, DefinitionNotFoundException {
        
        //
        // get ExtensionService
        //
        TestingListenerExtensionService listenerService = this.extensionService.getExtensionService(
            TestingListenerExtensionService.class,
            TestingListenerExtensionService.DEMO_EXTENSION_SERVICE_NAME);
        
        Assert.assertNotNull(listenerService);
        
        //
        // create a demo process
        //
        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();
        
        Node startNode = BpmnNodeFactory.createBpmnStartEventNode(builder);
        
        int[] ints = {1, 1};
        Node forkNode1 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", ints);
        
        int[] anotherInts = {2, 2};
        Node forkNode2 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result2", anotherInts);
        
        Node endNode1 = BpmnNodeFactory.createBpmnEndEventNode(builder); 
        Node endNode2 = BpmnNodeFactory.createBpmnEndEventNode(builder);
        
        ControlFlowFactory.createControlFlowFromTo(builder, startNode, forkNode1);
        ControlFlowFactory.createControlFlowFromTo(builder, startNode, forkNode2);
        ControlFlowFactory.createControlFlowFromTo(builder, forkNode1, endNode1);
        ControlFlowFactory.createControlFlowFromTo(builder, forkNode2, endNode2);
        
        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);
        ProcessDefinition definition = builder.buildDefinition();
        
        RepositoryService repository = this.jodaEngineServices.getRepositoryService();
        Navigator navigator = this.jodaEngineServices.getNavigatorService();
        
        //
        // deploy the instance
        //
        repository.addProcessDefinition(definition);
        
        //
        // start it
        //
        navigator.startProcessInstance(definition.getID());
        
        //
        // tests the registering of the listener (proper invocation is tested elsewhere)
        //
        Assert.assertTrue(listenerService.hasBeenRegistered(TestingTokenListener.class));
    }
    
    /**
     * Test of the registration and use of the {@link TestingBpmnXmlParseListener}.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @Test
    public void testDeploymentListenerRegistrationInRepository() throws ExtensionNotAvailableException {
        
        TestingListenerExtensionService listenerService = this.extensionService.getExtensionService(
            TestingListenerExtensionService.class,
            TestingListenerExtensionService.DEMO_EXTENSION_SERVICE_NAME);
        
        Assert.assertNotNull(listenerService);
        
        Assert.assertTrue(listenerService.hasBeenRegistered(TestingRepositoryDeploymentListener.class));
        
        RepositoryService repository = this.jodaEngineServices.getRepositoryService();
        repository.deployInNewScope(repository.getDeploymentBuilder().buildDeployment());
        Assert.assertTrue(listenerService.hasBeenInvoked(TestingRepositoryDeploymentListener.class));
    }
}
