package de.hpi.oryxengine.rest.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import de.hpi.oryxengine.exception.DefinitionNotFoundException;

/**
 * This provider maps the {@link DefinitionNotFoundException} to a HTTP status code.
 * 
 * @author Jan Rehwaldt
 */
@Provider
public class DefinitionNotFoundMapper implements ExceptionMapper<DefinitionNotFoundException> {
    
    @Override
    public Response toResponse(DefinitionNotFoundException exception) {
        return Response.status(Status.NOT_FOUND).build();
    }
}
