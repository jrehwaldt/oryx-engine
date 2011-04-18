package de.hpi.oryxengine.rest.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import de.hpi.oryxengine.exception.ResourceNotAvailableException;

/**
 * This provider maps the {@link ResourceNotAvailableException} to a HTTP status code.
 * 
 * @author Jan Rehwaldt
 */
@Provider
public class ResourceNotFoundMapper implements ExceptionMapper<ResourceNotAvailableException> {
    
    @Override
    public Response toResponse(ResourceNotAvailableException exception) {
        return Response.status(Status.NOT_FOUND).build();
    }
}
