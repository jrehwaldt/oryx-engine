package de.hpi.oryxengine.worklist.gui.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistService;

/**
 * API servlet providing an interface for the worklist manager.
 * 
 * More info: http://jersey.java.net/nonav/documentation/latest/user-guide.html#getting-started
 * 
 * @author Jan Rehwaldt
 * @since 2011-03-24
 */
@Path("/worklist")
@Produces({ "application/xml", "application/json" })
public final class WorklistWebService {
//implements WorklistService {
    
    private final WorklistService service;
    
    /**
     * Default constructor.
     */
    public WorklistWebService() {
        this.service = ServiceFactory.getWorklistService();
    }
    
    /**
     * Provides status information for the engine.
     * 
     * @return a hello world message
     */
    @Path("/demo")
    @GET
    @Produces("text/plain")
    public String status() {
        if (this.service == null) {
            return "Hello World";
        } else {
            return "Hallo Welt.";
        }
    }
    
//    @Path("/items/{resource}")
//    @GET
////    @Override
//    public List<WorklistItem> getWorklistItems(@PathParam("resource") Resource<?> resource) {
//        return getWorklistItems(resource);
//    }
//    
//    
//    @Path("/items/all/{resources}")
//    @GET
////    @Override
//   public Map<Resource<?>, List<WorklistItem>> getWorklistItems(@PathParam("resources") List<Resource<?>> resources) {
//        return service.getWorklistItems(resources);
//    }
//
//    @Path("/item/{item}/claim/{resource}")
//    @POST
//    @Override
//    public void claimWorklistItemBy(@PathParam("item") WorklistItem worklistItem,
//                                    @PathParam("resource") Resource<?> resource)
//    throws DalmatinaException {
//        service.claimWorklistItemBy(worklistItem, resource);
//    }
//
//    @Path("/item/{item}/begin/{resource}")
//    @POST
//    @Override
//    public void beginWorklistItemBy(@PathParam("item") WorklistItem worklistItem,
//                                    @PathParam("resource") Resource<?> resource)
//    throws DalmatinaException {
//        service.beginWorklistItemBy(worklistItem, resource);
//    }
//
//    @Path("/item/{item}/complete/{resource}")
//    @POST
//    @Override
//    public void completeWorklistItemBy(@PathParam("item") WorklistItem worklistItem,
//                                       @PathParam("resource") Resource<?> resource)
//    throws DalmatinaException {
//        service.completeWorklistItemBy(worklistItem, resource);
//    }
//
//    @Path("/item/{item}/abort/{resource}")
//    @POST
//    @Override
//    public void abortWorklistItemBy(@PathParam("item") WorklistItem worklistItem,
//                                    @PathParam("resource") Resource<?> resource)
//    throws DalmatinaException {
//        service.abortWorklistItemBy(worklistItem, resource);
//    }
}
