package de.hpi.oryxengine.rest.api;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import de.hpi.oryxengine.JodaEngineServices;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.factories.process.ShortenedReferenceProcessDeployer;
import de.hpi.oryxengine.rest.demo.BenchmarkDeployer;
import de.hpi.oryxengine.rest.demo.DemoDataForWebservice;
import de.hpi.oryxengine.rest.demo.DemoProcessStartEmailForWebservice;
import de.hpi.oryxengine.rest.demo.LoadDemoProcessAsXmlForWebservice;

/**
 * Offers demo methods (like creating demo users) to the user, should be deactivated in deployment.
 */
@Path("/demo")
public class DemoWebService {

    // TODO move somewhere else
    
    private boolean referenceDeployed;
    private JodaEngineServices engineServices;
    
    /**
     * Instantiates a new demo web service.
     */
    public DemoWebService(JodaEngineServices engineServices) {

        super();
        this.engineServices = engineServices;
        referenceDeployed = false;
    }

    /**
     * Generates demo participants using the DemoDataForWebservice class.
     * It should only be invoked once.
     * 
     * @return the response (OK = 200)
     * @throws ResourceNotAvailableException 
     */
    @Path("/generate")
    @POST
    public Response generate() throws ResourceNotAvailableException {
        DemoDataForWebservice.generate();
        // we always return ok as the demo data was already created and that is ok
        return Response.ok().build();
    }
    
    /**
     * Generates the xml process.
     *
     * @return the response
     */
    @Path("/generate-xml-process")
    @POST
    public Response generateXmlProcess() {

        LoadDemoProcessAsXmlForWebservice.generate();
        // we always return ok as the demo data was already created and that is ok
        return Response.ok().build();
    }

    /**
     * Generates the process which is started by email.
     *
     * @return the response
     */
    @Path("/generate-process-start-email")
    @POST
    public Response generateProcessStartEmail() {

        DemoProcessStartEmailForWebservice.generate();
        // LoadDemoProcessAsXmlForWebservice.generate();
        // we always return ok as the demo data was already created and that is ok
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Generates (deploys) the reference process.
     * 
     * @return the response
     * @throws ResourceNotAvailableException 
     */
    @Path("/reference")
    @POST
    public Response reference() throws ResourceNotAvailableException {
        try {
            if (!referenceDeployed) {
                ShortenedReferenceProcessDeployer deployer = new ShortenedReferenceProcessDeployer();
                deployer.deploy(engineServices);
            }
            
            referenceDeployed = true;

            return Response.ok().build();
        } catch (IllegalStarteventException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
    
    /**
     * Generates the reference process without participants.
     *
     * @return the response
     * @throws ResourceNotAvailableException the resource not available exception
     */
    @Path("/reference-without-participants")
    @POST
    public Response referenceWithoutParticipants() throws ResourceNotAvailableException {
        try {

            BenchmarkDeployer.generate();

            return Response.ok().build();
        } catch (IllegalStarteventException e) {
            e.printStackTrace();
            return Response.serverError().build();
        } catch (DefinitionNotFoundException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
