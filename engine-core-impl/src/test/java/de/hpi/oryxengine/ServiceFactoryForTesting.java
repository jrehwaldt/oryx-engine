package de.hpi.oryxengine;

/**
 * Extends {@link ServiceFactory} in order to provide methods for flushing several services.
 */
public class ServiceFactoryForTesting{
    
    /**
     * Clearing the {@link WorklistManager} in order to reset it.
     */
    public static void clearWorklistManager() {

//        worklistManager = null;
    }

    /**
     * Clearing the {@link IdentityService} in order to reset it.
     */
    public static void clearIdentityService() {
        
//        identityService = null;
    }
    
}
