package de.hpi.oryxengine.rest.exception;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This provider maps the {@link BadRequestException} to a HTTP status code.
 * 
 * @author Jan Rehwaldt
 */
@Provider
public class BadRequestMapper implements ExceptionMapper<BadRequestException> {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public Response toResponse(@Nonnull BadRequestException exception) {
        logger.error(String.format("Illegal request found %s", exception));
        return Response.status(Status.BAD_REQUEST).build();
    }
}
