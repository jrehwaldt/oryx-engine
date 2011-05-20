package org.jodaengine.ext.service;

import org.jodaengine.ext.Extension;

/**
 * This {@link TestingExtensionService} requires a {@link TestingWebService}
 * singleton to be created.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-20
 */
@Extension(
    value = TestingWebExtensionService.DEMO_EXTENSION_SERVICE_NAME,
    webServices = TestingWebService.class)
public class TestingWebExtensionService extends TestingExtensionService {
    
    public final static String DEMO_EXTENSION_SERVICE_NAME
        = TestingExtensionService.DEMO_EXTENSION_SERVICE_NAME + "-web";
    
}
