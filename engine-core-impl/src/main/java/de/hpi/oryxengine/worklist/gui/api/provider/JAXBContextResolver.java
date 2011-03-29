package de.hpi.oryxengine.worklist.gui.api.provider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

import de.hpi.oryxengine.resource.ResourceType;


/**
 * JAXB-JSON provider implementation and configuration.
 * 
 * http://igorshare.wordpress.com/2009/05/21/building-gwt-web-clients-part-2-1-
 *      how-to-control-json-output-format-from-jersey/
 * https://jaxb.dev.java.net/guide/Migrating_JAXB_2_0_applications_to_JavaSE_6.html#Using_JAXB_2_1_with_JavaSE_6
 */
@Provider
public class JAXBContextResolver
implements ContextResolver<JAXBContext> {
    
    private JAXBContext context;
    private final Set<Class<?>> types;
    private Class<?>[] cTypes = {
        UUID.class, ResourceType.class
//        ResourceImpl.class, OrganizationUnitImpl.class, ParticipantImpl.class, PositionImpl.class, RoleImpl.class,
//        ArrayList.class, List.class
    };
    
    /**
     * Default constructor.
     * 
     * @throws Exception if initializing the context fails
     */
    public JAXBContextResolver()
    throws Exception {
        types = new HashSet<Class<?>>(Arrays.asList(cTypes));
        context = new JSONJAXBContext(JSONConfiguration.natural().build(), cTypes);
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
