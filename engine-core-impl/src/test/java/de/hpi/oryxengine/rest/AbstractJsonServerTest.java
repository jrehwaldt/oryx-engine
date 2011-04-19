package de.hpi.oryxengine.rest;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

import de.hpi.oryxengine.AbstractTest;

/**
 * Abstract class providing anything necessary for server api tests.
 * 
 * @author Jan Rehwaldt
 */
public abstract class AbstractJsonServerTest extends AbstractTest {
    
    protected static final int WAIT_FOR_PROCESSES_TO_FINISH = 100;
    protected static final int TRIES_UNTIL_PROCESSES_FINISH = 100;
    protected static final short NUMBER_OF_INSTANCES_TO_START = 2;
    
    public static final String TMP_PATH = "./target/";
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected Dispatcher dispatcher = null;
    protected ObjectMapper mapper = null;
    
    /**
     * Set up.
     */
    @BeforeClass
    public void setUpAndStartServer() {
        logger.debug("Start ObjectMapper and Server (if required)");
        
        //
        // configure the ObjectMapper
        //
        // See this for a description, why we configured Jackson this way:
        // http://stackoverflow.com/questions/4822856/does-jackson-without-annotations-absolutely-require-setters
        //
        this.mapper = new ObjectMapper();
        SerializationConfig config = this.mapper.getSerializationConfig();
        config.setSerializationInclusion(Inclusion.NON_NULL);
        config.enable(SerializationConfig.Feature.INDENT_OUTPUT);
        config.set(SerializationConfig.Feature.AUTO_DETECT_FIELDS, false);
        this.mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(Visibility.ANY));
        this.mapper.configure(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
        this.mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_FIELDS, true);
        this.mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        // we do this with explicit annotating the type
//        this.mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        
        //
        // configure the server mock
        //
        if (getResource() != null) {
            POJOResourceFactory factory = new POJOResourceFactory(getResource());
            
            this.dispatcher = MockDispatcherFactory.createDispatcher();
            this.dispatcher.getRegistry().addResourceFactory(factory);
        } else {
            logger.debug("Server is not available");
        }
    }
    
    /**
     * Create a resource factory, which needs to be registered within the server.
     * 
     * @return the resource factory to register within the server
     */
    protected abstract Class<?> getResource();
}
