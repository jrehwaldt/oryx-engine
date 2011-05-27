package org.jodaengine.ext.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.Service;
import org.jodaengine.ext.Extension;

/**
 * This {@link Service} is for testing purposes. Listeners may register here when they were invoked
 * and the test can validate this information.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-27
 */
@Extension(TestingListenerExtensionService.DEMO_EXTENSION_SERVICE_NAME)
public class TestingListenerExtensionService implements Service {
    
    public static final String DEMO_EXTENSION_SERVICE_NAME
        = TestingExtensionService.DEMO_EXTENSION_SERVICE_NAME + "-listener-test";
    
    private List<Object> invokedInstances = new ArrayList<Object>();
    
    
    /**
     * Specifies that a certain instance was invoked.
     * 
     * @param instance the instance, which was invoked
     */
    public void invoked(@Nonnull Object instance) {
        this.invokedInstances.add(instance);
    }
    
    /**
     * Returns true in case an instance of the specified type was invoked.
     * 
     * @param type the type to lookup
     * @return true, if any was invoked
     */
    public boolean hasBeenInvoked(@Nonnull Class<?> type) {
        
        for (Object instance: this.invokedInstances) {
            if (instance.getClass().isAssignableFrom(type)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public void start(JodaEngineServices services) {
        
    }

    @Override
    public void stop() {
        
    }

    @Override
    public boolean isRunning() {
        return true;
    }
    
}
