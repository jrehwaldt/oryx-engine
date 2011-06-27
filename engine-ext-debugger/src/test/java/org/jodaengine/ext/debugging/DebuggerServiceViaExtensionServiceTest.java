package org.jodaengine.ext.debugging;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Nonnull;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.Service;
import org.jodaengine.deployment.importer.archive.AbstractDarHandler;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.ext.debugging.api.BreakpointService;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.api.ReferenceResolverService;
import org.jodaengine.ext.debugging.listener.DebuggerBpmnXmlParseListener;
import org.jodaengine.ext.debugging.listener.DebuggerDarHandler;
import org.jodaengine.ext.debugging.listener.DebuggerRepositoryDeploymentListener;
import org.jodaengine.ext.debugging.listener.DebuggerTokenListener;
import org.jodaengine.ext.debugging.listener.SplitJoinListener;
import org.jodaengine.ext.debugging.rest.DebuggerWebService;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.ext.listener.JoinListener;
import org.jodaengine.ext.listener.RepositoryDeploymentListener;
import org.jodaengine.ext.listener.SplitListener;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class tests the proper creation of any services and instances,
 * which are available for the {@link DebuggerService}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
public class DebuggerServiceViaExtensionServiceTest extends AbstractJodaEngineTest {
    
    private ExtensionService extensionService;
    
    /**
     * Setup.
     */
    @BeforeMethod
    public void setUp() {
        this.extensionService = this.jodaEngineServices.getExtensionService();
    }
    
    /**
     * Tests that all required components for the {@link DebuggerService} extension are available
     * via the {@link ExtensionService}.
     */
    @Test
    public void testRequiredComponentsAreAvailable() {
        Assert.assertTrue(this.extensionService.isExtensionAvailable(DebuggerBpmnXmlParseListener.class));
        Assert.assertTrue(this.extensionService.isExtensionAvailable(DebuggerRepositoryDeploymentListener.class));
        Assert.assertTrue(this.extensionService.isExtensionAvailable(DebuggerDarHandler.class));
        Assert.assertTrue(this.extensionService.isExtensionAvailable(DebuggerTokenListener.class));
        Assert.assertTrue(this.extensionService.isExtensionAvailable(DebuggerService.class));
        Assert.assertTrue(this.extensionService.isExtensionAvailable(BreakpointService.class));
        Assert.assertTrue(this.extensionService.isExtensionAvailable(ReferenceResolverService.class));
        Assert.assertTrue(this.extensionService.isExtensionAvailable(SplitJoinListener.class));
        }
    
    /**
     * Tests that the {@link DebuggerService} is successfully started by the {@link ExtensionService}.
     * 
     * This also tests that required fields are successfully set.
     * 
     * We use massive reflection here to avoid inheritance.
     * If one of the reflection exceptions is thrown this will most likely be
     * <ul>
     * <li>
     *   a) an issue with the local configuration of the {@link SecurityManager}
     * </li>
     * <li>
     * or
     *   b) the {@link DebuggerServiceImpl} was refactored (but this is unlikely, the
     *      test is rather <b>independent</b> of a concrete {@link DebuggerService} implementation).
     * </li>
     * </ul>
     * 
     * @throws ExtensionNotAvailableException test fails
     * @throws IllegalAccessException test fails
     */
    @Test
    public void testRequiredServiceIsStarted()
    throws ExtensionNotAvailableException, IllegalAccessException {
        
        DebuggerService service = this.extensionService.getServiceExtension(
            DebuggerService.class,
            DebuggerService.DEBUGGER_SERVICE_NAME);
        
        Assert.assertNotNull(service);
        Assert.assertTrue(service.isRunning());
        
        Assert.assertTrue(DebuggerServiceImpl.class.equals(service.getClass()));
        
        for (Field field: service.getClass().getDeclaredFields()) {
            Type fieldType = field.getGenericType();
            
            //
            // make field accessible
            //
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            
            if (fieldType instanceof Class) {
                Class<?> fieldClass = (Class<?>) fieldType;
                
                //
                // check that the navigator is set successfully (find the field first, if available)
                //
                if (Navigator.class.isAssignableFrom(fieldClass)) {
                    Object navigator = field.get(service);
                    
                    Assert.assertNotNull(navigator);
                    Assert.assertEquals(this.jodaEngineServices.getNavigatorService(), navigator);
                }
                
                //
                // check that the joda services are set successfully (find the field first, if available)
                //
                if (JodaEngineServices.class.isAssignableFrom(fieldClass)) {
                    Object setJodaEngineServices = field.get(service);
                    
                    Assert.assertNotNull(setJodaEngineServices);
                    Assert.assertEquals(this.jodaEngineServices, setJodaEngineServices);
                }
            }
        }
    }
    
    /**
     * Tests that our {@link ReferenceResolverService} is available.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @Test
    public void testRequiredReferenceResolverServiceIsStarted()
    throws ExtensionNotAvailableException {
        
        ReferenceResolverService service = this.extensionService.getServiceExtension(
            ReferenceResolverService.class,
            ReferenceResolverService.RESOLVER_SERVICE_NAME);
        
        Assert.assertNotNull(service);
        Assert.assertTrue(service.isRunning());
        
        Assert.assertTrue(ReferenceResolverServiceImpl.class.equals(service.getClass()));
    }
    
    /**
     * Tests that the listener for the {@link DebuggerBpmnXmlParseListener} is successfully provided.
     */
    @Test
    public void testRequiredBpmnXmlListenerIsProvided() {
        List<BpmnXmlParseListener> listeners = this.extensionService.getExtensions(BpmnXmlParseListener.class);
        
        Assert.assertFalse(listeners.isEmpty());
        Assert.assertTrue(isTypeContained(listeners, DebuggerBpmnXmlParseListener.class));
    }
    
    /**
     * Tests that the handlers for the {@link DebuggerDarHandler} is successfully provided.
     */
    @Test
    public void testRequiredDarHandlerIsProvided() {
        List<AbstractDarHandler> handlers = this.extensionService.getExtensions(AbstractDarHandler.class);
        
        Assert.assertFalse(handlers.isEmpty());
        Assert.assertTrue(isTypeContained(handlers, DebuggerDarHandler.class));
    }
    
    /**
     * Tests that the listener for the {@link DebuggerRepositoryDeploymentListener} is successfully provided.
     */
    @Test
    public void testRequiredRepositoryDeploymentListenerIsProvided() {
        
        List<RepositoryDeploymentListener> listeners
            = this.extensionService.getExtensions(RepositoryDeploymentListener.class);
        
        Assert.assertFalse(listeners.isEmpty());
        Assert.assertTrue(isTypeContained(listeners, DebuggerRepositoryDeploymentListener.class));
    }
    
    /**
     * Tests that the listener for the {@link DebuggerTokenListener} is successfully provided.
     */
    @Test
    public void testRequiredDebuggerTokenListenerIsProvided() {
        
        List<AbstractTokenListener> listeners
            = this.extensionService.getExtensions(AbstractTokenListener.class);
        
        Assert.assertFalse(listeners.isEmpty());
        Assert.assertTrue(isTypeContained(listeners, DebuggerTokenListener.class));
    }
    
    /**
     * Tests that the listener for the {@link SplitJoinListener} is successfully provided.
     */
    @Test
    public void testRequiredSplitJoinListenerIsProvided() {
        List<JoinListener> joinListeners = this.extensionService.getExtensions(JoinListener.class);
        
        Assert.assertFalse(joinListeners.isEmpty());
        Assert.assertTrue(isTypeContained(joinListeners, SplitJoinListener.class));
        
        List<SplitListener> splitListeners = this.extensionService.getExtensions(SplitListener.class);
        
        Assert.assertFalse(splitListeners.isEmpty());
        Assert.assertTrue(isTypeContained(splitListeners, SplitJoinListener.class));
    }

    
    /**
     * Tests the proper creation of specified web services.
     * 
     * This also tests that required fields are successfully set.
     * 
     * We use massive reflection here to avoid inheritance.
     * If one of the reflection exceptions is thrown this will most likely be
     * <ul>
     * <li>
     *   a) an issue with the local configuration of the {@link SecurityManager}
     * </li>
     * <li>
     * or
     *   b) the {@link DebuggerServiceImpl} was refactored (but this is unlikely, the
     *      test is rather <b>independent</b> of a concrete {@link DebuggerService} implementation).
     * </li>
     * </ul>
     * 
     * @throws ExtensionNotAvailableException test fails
     * @throws IllegalAccessException test fails
     */
    @Test
    public void testCreationOfWebServiceSingletons()
    throws ExtensionNotAvailableException, IllegalAccessException {
        
        DebuggerService debugger = this.extensionService.getServiceExtension(
            DebuggerService.class,
            DebuggerService.DEBUGGER_SERVICE_NAME);
        
        Assert.assertNotNull(debugger);
        
        List<Service> webServices = this.extensionService.getExtensionWebServiceSingletons();
        
        Assert.assertNotNull(webServices);
        Assert.assertFalse(webServices.isEmpty());
        
        boolean containsWebService = false;
        for (Service webService: webServices) {
            Assert.assertNotNull(webService);
            
            if (webService instanceof DebuggerWebService) {
                containsWebService = true;
                DebuggerWebService debuggerWebService = (DebuggerWebService) webService;
                
                Assert.assertEquals(debugger.isRunning(), webService.isRunning());
                
                for (Field field: debuggerWebService.getClass().getDeclaredFields()) {
                    Type fieldType = field.getGenericType();
                    
                    //
                    // make field accessible
                    //
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    
                    if (fieldType instanceof Class) {
                        Class<?> fieldClass = (Class<?>) fieldType;
                        
                        //
                        // check that the navigator is set successfully (find the field first, if available)
                        //
                        if (DebuggerService.class.isAssignableFrom(fieldClass)) {
                            Object debuggerService = field.get(debuggerWebService);
                            
                            Assert.assertNotNull(debuggerService);
                            Assert.assertEquals(debugger, debuggerService);
                        }
                    }
                }
            }
        }
        
        Assert.assertTrue(containsWebService);
    }
    
    /**
     * This method verifies, that a certain instance type is available.
     * 
     * @param objects the instances
     * @param type the type, which should be available
     * @return true, if available
     */
    private boolean isTypeContained(@Nonnull List<? extends Object> objects,
                                    @Nonnull Class<?> type) {
        
        for (Object object: objects) {
            if (type.equals(object.getClass())) {
                return true;
            }
        }
        
        return false;
    }
}
