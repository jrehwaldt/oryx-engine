package org.jodaengine.ext.service;

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
    
    private ExtensionService extension = null;
    
    /**
     * Setup.
     */
    @BeforeMethod
    public void setUp() {
        this.extension = new ExtensionServiceImpl();
        this.extension.start(this.jodaEngineServices);
    }
    
    /**
     * Tests getting an unavailable extension.
     * 
     * @throws ExtensionNotAvailableException expected
     */
    @Test(expectedExceptions = ExtensionNotAvailableException.class)
    public void testGettingUnavailableExtension()
    throws ExtensionNotAvailableException {
        this.extension.getExtensionService(
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
        
        TestingExtensionService service = this.extension.getExtensionService(
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
        
        TestingExtensionService service = this.extension.getExtensionService(
            TestingExtensionService.class,
            TestingExtensionService.DEMO_EXTENSION_SERVICE_NAME);
        
        Assert.assertNotNull(service);
        Assert.assertTrue(service.isStarted());
        Assert.assertTrue(service.isRunning());
        Assert.assertFalse(service.isStopped());
        Assert.assertNotNull(service.getServices());
        Assert.assertEquals(this.jodaEngineServices, service.getServices());
    }
}
