package de.hpi.oryxengine.rest.provider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import de.hpi.oryxengine.navigator.NavigatorStatistic;
import de.hpi.oryxengine.resource.AbstractResource;


/**
 * JAXB-JSON provider implementation and configuration.
 * 
 * http://igorshare.wordpress.com/2009/05/21/building-gwt-web-clients-part-2-1-
 *      how-to-control-json-output-format-from-jersey/
 * https://jaxb.dev.java.net/guide/Migrating_JAXB_2_0_applications_to_JavaSE_6.html#Using_JAXB_2_1_with_JavaSE_6
 */
@Provider
@Produces({ MediaType.APPLICATION_JSON })
public class JAXBContextResolver
implements ContextResolver<JAXBContext> {
    
    private JAXBContext context;
    private final Set<Class<?>> types;
    private Class<?>[] cTypes = {
        NavigatorStatistic.class,
        AbstractResource.class
    };
    
    /**
     * Default constructor.
     * 
     * @throws Exception if initializing the context fails
     */
    public JAXBContextResolver()
    throws Exception {
        types = new HashSet<Class<?>>(Arrays.asList(cTypes));
//        context = new JSONJAXBContext(JSONConfiguration.natural().build(), cTypes);
    }
    
    @Override
    public JAXBContext getContext(Class<?> objectType) {
        if (types.contains(objectType)) {
            return context;
        } else {
            return null;
        }
    }
}
