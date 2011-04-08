package de.hpi.oryxengine.rest.provider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.resource.AbstractResource;

/**
 * Json/Xml reader, which supports our interface constructs.
 * 
 * @author Jan Rehwaldt
 */
@Provider
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class InterfaceMessageBodyReader
implements MessageBodyReader<AbstractResource<?>> {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Context
    private ContextResolver<JAXBContext> contextResolver; 
    
    @Override
    public boolean isReadable(Class<?> type,
                              Type genericType,
                              Annotation[] annotations,
                              MediaType mediaType) {

        logger.info("Using interface body reader for {}?", type);
        return AbstractResource.class.isAssignableFrom(type);
    }
    
    @Override
    public AbstractResource<?> readFrom(Class<AbstractResource<?>> type,
                                Type genericType,
                                Annotation[] annotations,
                                MediaType mediaType,
                                MultivaluedMap<String, String> httpHeaders,
                                InputStream entityStream)
    throws IOException {
        AbstractResource<?> resource = null;
        JAXBContext context = contextResolver.getContext(type);
        
        try {
            logger.info("Unmarshalling the resource ");
            resource = (AbstractResource<?>) context.createUnmarshaller().unmarshal(entityStream);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return resource;
    }

}
