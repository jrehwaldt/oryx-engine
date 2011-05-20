package org.jodaengine.rest.exception;

import javax.annotation.Nonnull;
import javax.naming.ServiceUnavailableException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This {@link Provider} maps the {@link ServiceUnavailableException} to a HTTP status code.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-20
 */
@Provider
public class ServiceUnavailableMapper implements ExceptionMapper<ServiceUnavailableException> {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public Response toResponse(@Nonnull ServiceUnavailableException exception) {
        logger.error("Service %s unavailable", exception.toString());
        return Response.status(Status.SERVICE_UNAVAILABLE).build();
    }
}
