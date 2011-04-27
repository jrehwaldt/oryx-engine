package de.hpi.oryxengine.rest.api;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
/**
 * Offers demo methods (like creating demo users) to the user, should be deactivated in deployment.
 */
@Path("/demo")
public class DemoWebService {
    
    // shows you whether demo has already been invoked
    private boolean invoked;
    
    /**
     * Instantiates a new demo web service.
     */
    public DemoWebService() {
        super();
        invoked = false;
    }
    
    /**
     * Generates demo participants using the DemoDataForWebservice class.
     * It should only be invoked once.
     *
     * @return the response (OK = 200)
     */
    @Path("/generate")
    @POST
    public Response generate() {
        if (!invoked) {
            DemoDataForWebservice.generate();
            invoked = true;
        }
        // we always return ok as the demo data was already created and that is ok
        return Response.ok().build();        
    }

}
