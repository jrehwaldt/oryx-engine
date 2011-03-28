package de.hpi.oryxengine.worklist.gui.api.provider;


/**
 * JAXB-JSON provider implementation and configuration.
 * 
 * http://igorshare.wordpress.com/2009/05/21/building-gwt-web-clients-part-2-1-
 *      how-to-control-json-output-format-from-jersey/
 * https://jaxb.dev.java.net/guide/Migrating_JAXB_2_0_applications_to_JavaSE_6.html#Using_JAXB_2_1_with_JavaSE_6
 */
//@Provider
public class JAXBContextResolver {
//implements ContextResolver<JAXBContext> {
//    
//    private JAXBContext context;
//    private final Set<Class<?>> types;
//    private Class<?>[] cTypes = { };
//    
//    /**
//     * Default constructor.
//     * 
//     * @throws Exception if initializing the context fails
//     */
//    public JAXBContextResolver()
//    throws Exception {
//        types = new HashSet<Class<?>>(Arrays.asList(cTypes));
//        context = new JSONJAXBContext(JSONConfiguration.natural().build(), cTypes);
//    } 
//    
//    @Override
//    public JAXBContext getContext(Class<?> objectType) {
//        if (types.contains(objectType)) {
//            return context;
//        } else {
//            return null;
//        }
//    }
}
