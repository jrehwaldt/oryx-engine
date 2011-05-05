package de.hpi.oryxengine.exception;

import java.util.UUID;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.resource.AbstractResource;

/**
 * This is an exception stating that a resource does not exist.
 * 
 * @author Jan Rehwaldt
 */
public class ResourceNotAvailableException extends JodaEngineException {
    private static final long serialVersionUID = 6124739997417080338L;
    
    private static final String MESSAGE = "The requested resource is not available or was removed.";

    private final UUID resourceID;
    private final Class<? extends AbstractResource<?>> resourceClazz;
    
    /**
     * Default constructor.
     *
     * @param resourceClazz the class of the resource
     * @param resourceID the id of the resource that is not available
     */
    public ResourceNotAvailableException(@Nonnull Class<? extends AbstractResource<?>> resourceClazz,
                                         @Nonnull UUID resourceID) {
        super(MESSAGE);
        
        this.resourceClazz = resourceClazz;
        this.resourceID = resourceID;
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
    
    @Override
    @Nonnull
    public String toString() {
        return String.format("%s[id: %s]", getResourceClass().getSimpleName(), getResourceID());
    }
}
