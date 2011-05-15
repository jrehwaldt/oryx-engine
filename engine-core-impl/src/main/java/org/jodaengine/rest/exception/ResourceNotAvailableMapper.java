package org.jodaengine.rest.exception;

import org.jodaengine.exception.ResourceNotAvailableException;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This provider maps the {@link ResourceNotAvailableException} to a HTTP status code.
 * 
 * @author Jan Rehwaldt
 */
@Provider
public class ResourceNotAvailableMapper implements ExceptionMapper<ResourceNotAvailableException> {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Response toResponse(@Nonnull ResourceNotAvailableException exception) {
        logger.error(String.format("Failed fetching the resource %s", exception));
        return Response.status(Status.NOT_FOUND).entity(exception.getMessage()).build();
    }
}
