package de.hpi.oryxengine.rest.api;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

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

import de.hpi.oryxengine.rest.exception.DefinitionNotFoundMapper;
import de.hpi.oryxengine.rest.exception.InvalidWorkItemMapper;
import de.hpi.oryxengine.rest.exception.ResourceNotAvailableMapper;


/**
 * Abstract class providing anything necessary for server api tests.
 * 
 * @author Jan Rehwaldt
 */
public abstract class AbstractJsonServerTest extends AbstractJodaEngineTest {
    
    protected static final Status HTTP_STATUS_OK = Status.OK;
    protected static final Status HTTP_STATUS_FAIL = Status.NOT_FOUND;
    protected static final Status HTTP_BAD_REQUEST = Status.BAD_REQUEST;
        
    public static final String TMP_PATH = "./target/";
        
    protected Dispatcher dispatcher = null;
    protected ObjectMapper mapper = null;

    private static final List<Class<? extends ExceptionMapper<?>>> PROVIDERS
        = new ArrayList<Class<? extends ExceptionMapper<?>>>();
    
    static {
        PROVIDERS.add(DefinitionNotFoundMapper.class);
        PROVIDERS.add(InvalidWorkItemMapper.class);
        PROVIDERS.add(ResourceNotAvailableMapper.class);
    }
    
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
            
            for (Class<? extends ExceptionMapper<?>> provider: PROVIDERS) {
                this.dispatcher.getProviderFactory().addExceptionMapper(provider);
            }
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
     * Make a simple get request returning the response.
     *
     * @param url the url
     * @return the mock http response
     * @throws URISyntaxException the uRI syntax exception
     */
    protected MockHttpResponse makeGETRequest(String url) throws URISyntaxException {
        // set up our request
        MockHttpRequest request = MockHttpRequest.get(url);
        
        return invokeSimpleRequest(request);
    }
    
    /**
     * Make a get request to the specified url.
     *
     * @param url the url as a String
     * @return the answer of the webservice as a String (usually JSON)
     * @throws URISyntaxException the uRI syntax exception
     */
    protected String makeGETRequestReturningJson(String url) throws URISyntaxException {
        // Make it a String so we can read the Json
        return makeGETRequest(url).getContentAsString();
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
        MockHttpRequest request = MockHttpRequest.delete(url);
        
        return invokeSimpleRequest(request).getContentAsString();
    }
    
    /**
     * Make a post request with form data.
     *
     * @param url the url
     * @param content the POST content, which should be processed
     * @return the mock http response
     * @throws URISyntaxException the uRI syntax exception
     */
    protected MockHttpResponse makePOSTFormRequest(String url, Map<String, String> content) throws URISyntaxException {
        // set up our request
        MockHttpRequest request = MockHttpRequest.post(url);
        
        return invokeFormRequest(request, content);
    }
    
    
    /**
     * Make a mock post request with a specific contentType and content.
     *
     * @param url the url
     * @param content the content
     * @param contentType the content type
     * @return the mock http response
     * @throws URISyntaxException the uRI syntax exception
     */
    protected MockHttpResponse makePOSTRequest(String url,
                                               String content,
                                               String contentType) throws URISyntaxException {
        MockHttpRequest request = MockHttpRequest.post(url);
        request.content(content.getBytes());
        request.contentType(contentType);
        
        return invokeSimpleRequest(request);
    }
    
    /**
     * Make patch request.
     *
     * @param url the url
     * @param content the content
     * @param contentType the content type
     * @return the mock http response
     * @throws URISyntaxException the uRI syntax exception
     */
    protected MockHttpResponse makePATCHRequest(String url, String content, String contentType) throws URISyntaxException{
        MockHttpRequest request = MockHttpRequest.create("PATCH", url);
        request.content(content.getBytes());
        request.contentType(contentType);
        
        return invokeSimpleRequest(request);
    }
    /**
     * Make a PUT request with JSON content.
     *
     * @param url the url the PUT request is send to
     * @param json the json which should be transferred as a String
     * @return the mock http response (in case you need it for check if it is ok)
     * @throws URISyntaxException the URI syntax exception
     */
    protected MockHttpResponse makePUTRequestWithJson(String url, String json) throws URISyntaxException {
        MockHttpRequest request = MockHttpRequest.put(url);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(json.getBytes());
        
        return invokeSimpleRequest(request);
    }
    
    /**
     * Make a PUT request with plain text content.
     *
     * @param url the url the PUT request is send to
     * @param data the string which should be passed with the put request
     * @return the mock http response (in case you need it for check if it is ok)
     * @throws URISyntaxException the URI syntax exception
     */
    protected MockHttpResponse makePUTRequestWithText(String url, String data) throws URISyntaxException {
        MockHttpRequest request = MockHttpRequest.put(url);
        request.contentType(MediaType.TEXT_PLAIN);
        request.content(data.getBytes());
        
        return invokeSimpleRequest(request);
    }
    
    /**
     * Helper method which invokes the Request and returns the answer as a String.
     *
     * @param request the mocked Request
     * @param content the POST content, which should be processed
     * @return the answer as a MockHttpResponse
     */
    private MockHttpResponse invokeFormRequest(MockHttpRequest request, Map<String, String> content) {
        MockHttpResponse response = new MockHttpResponse();

        //Only for Requests, that need data to be sent with
        if (content != null) {
            Iterator<Entry<String, String>> iterator = content.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> e = iterator.next();
                request.addFormHeader(e.getKey(), e.getValue());
            }

        }
        // invoke the request
        dispatcher.invoke(request, response);
        
        return response;
    }
    
    /**
     * Invokes a mock request without any form content or plain text content, etc.
     *
     * @param request the request
     * @return the mock http response
     */
    private MockHttpResponse invokeSimpleRequest(MockHttpRequest request) {
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        
        return response;
    }
    
}
