package de.hpi.oryxengine.rest;

import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.testng.annotations.BeforeClass;

import de.hpi.oryxengine.AbstractJodaEngineTest;

/**
 * Abstract class providing anything necessary for server api tests.
 * 
 * @author Jan Rehwaldt
 */
public abstract class AbstractJsonServerTest extends AbstractJodaEngineTest {
    
    protected static final int WAIT_FOR_PROCESSES_TO_FINISH = 100;
    protected static final int TRIES_UNTIL_PROCESSES_FINISH = 100;
    protected static final short NUMBER_OF_INSTANCES_TO_START = 2;
    protected static final int HTTP_STATUS_OK = 200;
    
    public static final String TMP_PATH = "./target/";
        
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
    
    /**
     * Make a get request to the specified url.
     *
     * @param url the url as a String
     * @return the answer of the webservice as a String (usually JSON)
     * @throws URISyntaxException the uRI syntax exception
     */
    protected String makeGETRequest(String url) throws URISyntaxException {
        // set up our request
        MockHttpRequest request = MockHttpRequest.get(url);
        
        return invokeRequest(request).getContentAsString();
    }
    
    /**
     * Make a get request to the specified url.
     *
     * @param url the url as a String
     * @return the answer of the webservice as a String (usually JSON)
     * @throws URISyntaxException the uRI syntax exception
     */
    protected String makeDELETERequest(String url) throws URISyntaxException {
        // set up our request
        MockHttpRequest request = MockHttpRequest.get(url);
        
        return invokeRequest(request).getContentAsString();
    }
    
    /**
     * Make a PUT request with JSON content.
     *
     * @param url the url the PUT request is send to
     * @param json the json which should be transferred as a String
     * @return the mock http response (in case you need it for check if it is ok)
     * @throws URISyntaxException the uRI syntax exception
     */
    protected MockHttpResponse makePUTRequestWithJson(String url, String json) throws URISyntaxException {
        MockHttpRequest request = MockHttpRequest.put(url);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(json.getBytes());
        
        return invokeRequest(request);
    }
    
    /**
     * Helper method which invokes the Request and returns the answer as a String.
     *
     * @param request the request
     * @return the answer as a String
     */
    private MockHttpResponse invokeRequest(MockHttpRequest request) {
        MockHttpResponse response = new MockHttpResponse();
        // invoke the request
        dispatcher.invoke(request, response);
        
        return response;
    }
}
