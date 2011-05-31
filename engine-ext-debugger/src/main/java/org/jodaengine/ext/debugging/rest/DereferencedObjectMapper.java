package org.jodaengine.ext.debugging.rest;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This {@link Provider} maps the {@link DereferencedObjectException} to a HTTP status code.
 * 
 * @author Jan Rehwaldt
 */
@Provider
public class DereferencedObjectMapper implements ExceptionMapper<DereferencedObjectException> {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public Response toResponse(@Nonnull DereferencedObjectException exception) {
        logger.error(String.format("Failed fetching %s", exception));
        return Response.status(Status.NOT_FOUND).build();
    }
}
