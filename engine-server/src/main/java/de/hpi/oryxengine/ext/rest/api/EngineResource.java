package de.hpi.oryxengine.ext.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * API servlet providing an interface for engine configuration.
 * 
 * More info: http://jersey.java.net/nonav/documentation/latest/user-guide.html#getting-started
 * 
 * @author Jan Rehwaldt
 * @since 2011-03-04
 */
@Path("/engine")
public class EngineResource {
    /**
     * Provides status information for the engine.
     * 
     * @return a hello world message
     */
    @GET
    @Produces("text/plain")
    public String status() {
        return "Hello World";
    }
    
    /**
     * Starts the engine.
     * 
     * @return status code
     */
    @Path("/start")
    @GET
    @Produces("text/plain")
    public String start() {
        return "Start";
    }
    
    /**
     * Stops the engine.
     * 
     * @return status code
     */
    @Path("/stop")
    @GET
    @Produces("text/plain")
    public String stop() {
        return "Stop";
    }
}
