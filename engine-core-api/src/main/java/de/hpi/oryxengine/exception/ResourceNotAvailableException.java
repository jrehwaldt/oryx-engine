package de.hpi.oryxengine.exception;

import java.util.UUID;

import de.hpi.oryxengine.resource.AbstractResource;

/**
 * This is a runtime exception stating that a resource does not exist.
 * 
 * @author Jan Rehwaldt
 */
public class ResourceNotAvailableException extends JodaEngineException {
    private static final long serialVersionUID = 6124739997417080338L;
    
    private static final String MESSAGE = "The requested resource is (no longer) available.";

    private final UUID resourceID;
    private final Class<? extends AbstractResource<?>> resourceClazz;
    
    /**
     * Default constructor.
     *
     * @param resourceClazz the class of the resource
     * @param resourceID the id of the resource that is not available
     */
    public ResourceNotAvailableException(Class<? extends AbstractResource<?>> resourceClazz, UUID resourceID) {
        super(MESSAGE, null);
        this.resourceClazz = resourceClazz;
        this.resourceID = resourceID;
    }
    
    /**
     * Default constructor. Currently disabled.
     */
    private ResourceNotAvailableException() {
        super(MESSAGE, null);
        resourceID = null;
        resourceClazz = null;
    }
    
    /**
     * Gets the resource id.
     *
     * @return the resource id
     */
    public UUID getResourceID() {
        return resourceID;
    }
    
    /**
     * Gets the resource class.
     *
     * @return the resource class
     */
    public Class<? extends AbstractResource<?>> getResourceClass() {
        return resourceClazz;
    }
    
    
}
