package de.hpi.oryxengine.worklist.gui.api;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.resource.ResourceType;
import de.hpi.oryxengine.resource.worklist.WorklistItem;
import de.hpi.oryxengine.worklist.gui.WorklistServiceFacade;

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
public final class WorklistWebService
implements WorklistServiceFacade {
    
    private final WorklistService service;
    
    private final IdentityService identity;
    
    /**
     * Default constructor.
     */
    public WorklistWebService() {
        this.service = ServiceFactory.getWorklistService();
        this.identity = ServiceFactory.getIdentityService();
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
    @Path("/items/test/{resource}")
    @GET
//    @Override
    public @Nonnull List<WorklistItem> getWorklistItems(@QueryParam("resource") Resource<?> resource) {
        
        
        return null;
    }
    
    @Path("/items/{resource-type}-{resource-id}")
    @GET
    @Override
    public @Nonnull List<WorklistItem> getWorklistItems(@PathParam("resource-type") ResourceType resourceType,
                                                        @PathParam("resource-id") UUID resourceId) {
        
        
        return null;
    }
    
    @Path("/item/{worklist-item-id}/claim/{resource-type}-{resource-id}")
    @POST
    @Override
    public void claimWorklistItemBy(@Nonnull UUID worklistItemId,
                                    @Nonnull ResourceType resourceType,
                                    @Nonnull UUID resourceId)
    throws DalmatinaException {
        
    }
    
    @Path("/item/{worklist-item-id}/begin/{resource-type}-{resource-id}")
    @POST
    @Override
    public void beginWorklistItemBy(@Nonnull UUID worklistItemId,
                                    @Nonnull ResourceType resourceType,
                                    @Nonnull UUID resourceId)
    throws DalmatinaException {
        
    }
    
    @Path("/item/{worklist-item-id}/complete/{resource-type}-{resource-id}")
    @POST
    @Override
    public void completeWorklistItemBy(@Nonnull UUID worklistItemId,
                                       @Nonnull ResourceType resourceType,
                                       @Nonnull UUID resourceId)
    throws DalmatinaException {
        
    }
    
    @Path("/item/{worklist-item-id}/abort/{resource-type}-{resource-id}")
    @POST
    @Override
    public void abortWorklistItemBy(@Nonnull UUID worklistItemId,
                                    @Nonnull ResourceType resourceType,
                                    @Nonnull UUID resourceId)
    throws DalmatinaException {
        
    }
}
