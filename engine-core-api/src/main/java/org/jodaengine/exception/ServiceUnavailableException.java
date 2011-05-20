package org.jodaengine.exception;

import javax.annotation.Nonnull;

import org.jodaengine.bootstrap.Service;

/**
 * This {@link Exception} indicates that a certain {@link Service} is currently not available.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-20
 */
public class ServiceUnavailableException extends JodaEngineRuntimeException {
    private static final long serialVersionUID = 6413161876815436422L;
    
    private static final String MESSAGE = "The requested service is currently not available [%s].";
    
    private final Class<? extends Service> serviceType;
    
    /**
     * Default constructor.
     * 
     * @param serviceType the service type, which is unavailable
     */
    public ServiceUnavailableException(@Nonnull Class<? extends Service> serviceType) {
        super(String.format(MESSAGE, serviceType.getSimpleName()));
        this.serviceType = serviceType;
    }
    
    /**
     * Returns the {@link Service} type.
     * 
     * @return the service type
     */
    public @Nonnull Class<? extends Service> getServiceType() {
        return this.serviceType;
    }
    
    @Override
    @Nonnull
    public String toString() {
        return String.format("%s", getServiceType().getSimpleName());
    }
}
