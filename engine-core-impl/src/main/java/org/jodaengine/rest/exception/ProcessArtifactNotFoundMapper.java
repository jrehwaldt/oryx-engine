package org.jodaengine.rest.exception;

import javax.annotation.Nonnull;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This {@link Provider} maps the {@link ProcessArtifactNotFoundException} to a HTTP status code.
 * 
 * @author Jan Rehwaldt
 */
@Provider
public class ProcessArtifactNotFoundMapper implements ExceptionMapper<ProcessArtifactNotFoundException> {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Response toResponse(@Nonnull ProcessArtifactNotFoundException exception) {
        logger.error(String.format("Failed fetching the artifact %s", exception));
        return Response.status(Status.NOT_FOUND)
                       .entity(exception.getMessage())
                       .type(MediaType.TEXT_PLAIN)
                       .build();
    }
}
