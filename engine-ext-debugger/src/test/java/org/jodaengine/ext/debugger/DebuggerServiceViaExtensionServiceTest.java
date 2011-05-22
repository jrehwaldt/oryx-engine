package org.jodaengine.ext.debugger;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.Service;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.api.BreakpointService;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.listener.DebuggerDeploymentImportListener;
import org.jodaengine.ext.debugging.rest.DebuggerWebService;
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
        Assert.assertNotNull(this.extensionService.isExtensionAvailable(DebuggerDeploymentImportListener.class));
        Assert.assertNotNull(this.extensionService.isExtensionAvailable(DebuggerService.class));
        Assert.assertNotNull(this.extensionService.isExtensionAvailable(BreakpointService.class));
        
        // TODO extend
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
     * Keep it synced!
     * 
     * @throws ExtensionNotAvailableException test fails
     * @throws IllegalAccessException test fails
     */
    @Test
    public void testRequiredServiceIsStarted()
    throws ExtensionNotAvailableException, IllegalAccessException {
        
        DebuggerService service = this.extensionService.getExtensionService(
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
     * Tests that the listener for the {@link DebuggerDeploymentImportListener} is successfully provided.
     */
    @Test
    public void testRequiredDeploymentListenerIsProvided() {
        List<BpmnXmlParseListener> listeners = this.extensionService.getExtensions(BpmnXmlParseListener.class);
        
        Assert.assertTrue(listeners.size() > 0);
        
        boolean listenerAvailable = false;
        for (BpmnXmlParseListener listener: listeners) {
            if (listener instanceof DebuggerDeploymentImportListener) {
                listenerAvailable = true;
            }
        }
        
        Assert.assertTrue(listenerAvailable);
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
     * Keep it synced!
     * 
     * @throws ExtensionNotAvailableException test fails
     * @throws IllegalAccessException test fails
     */
    @Test
    public void testCreationOfWebServiceSingletons()
    throws ExtensionNotAvailableException, IllegalAccessException {
        
        DebuggerService debugger = this.extensionService.getExtensionService(
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
}
