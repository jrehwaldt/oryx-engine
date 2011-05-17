package org.jodaengine.rest.exception;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jodaengine.exception.DefinitionNotFoundException;


/**
 * This provider maps the {@link DefinitionNotFoundException} to a HTTP status code.
 * 
 * @author Jan Rehwaldt
 */
@Provider
public class DefinitionNotFoundMapper implements ExceptionMapper<DefinitionNotFoundException> {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public Response toResponse(@Nonnull DefinitionNotFoundException exception) {
        logger.error(String.format("Failed fetching the definition %s", exception));
        return Response.status(Status.NOT_FOUND).build();
    }
}
