package org.jodaengine.rest.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * This provider maps the {@link IllegalArgumentException} to a HTTP status code.
 * 
 * @author Jan Rehwaldt
 */
@Provider
public class IllegalArgumentMapper implements ExceptionMapper<IllegalArgumentException> {
    
    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return Response.status(Status.BAD_REQUEST).build();
    }
}
