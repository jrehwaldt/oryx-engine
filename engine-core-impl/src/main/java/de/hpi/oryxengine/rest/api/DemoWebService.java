package de.hpi.oryxengine.rest.api;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Offers demo methods (like creating demo users) to the user, should be deactivated in deployment.
 */
@Path("/demo")
public class DemoWebService {
    
    /**
     * Generates demo participants using the DemoDataForWebservice class.
     */
    @Path("/generate")
    @POST
    public void generate() {
        DemoDataForWebservice.generate();
        
    }

}
