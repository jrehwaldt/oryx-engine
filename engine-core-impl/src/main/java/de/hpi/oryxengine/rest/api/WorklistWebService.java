package de.hpi.oryxengine.rest.api;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskImpl;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.ResourceType;
import de.hpi.oryxengine.resource.worklist.WorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;
import de.hpi.oryxengine.rest.WorklistServiceFacade;

/**
 * API servlet providing an interface for the worklist manager.
 * 
 * More info: http://jersey.java.net/nonav/documentation/latest/user-guide.html#getting-started
 * 
 * @author Jan Rehwaldt
 * @since 2011-03-24
 */
@Path("/worklist")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public final class WorklistWebService implements WorklistServiceFacade {

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
     * Creates a demo participant with a work item
     * 
     */
    @Path("/demo")
    @GET
    @Produces("text/plain")
    public String demoParticipant() {
        
        IdentityBuilder builder = this.identity.getIdentityBuilder();
        AbstractParticipant thomas = builder.createParticipant("Thomas Strunz");

        Task task = new TaskImpl("Kaffee holen", "Bohnenkaffee", null, thomas);
        Token token = new TokenImpl(null);
        WorklistItem item = new WorklistItemImpl(task, token);
        thomas.getWorklist().addWorklistItem(item);
        
        return thomas.getID().toString();
    }

    // @Path("/items/")
    // @GET
    // // @Override
    // public @Nonnull List<WorklistItem> getWorklistItems(@QueryParam("resource") ResourceImpl<?> resource) {
    // return this.service.getWorklistItems(resource);
    // }
    //
    // @Path("/items/position/")
    // @GET
    // // @Override
    // public @Nonnull List<WorklistItem> getWorklistItems(@QueryParam("resource") Resource<?> resource) {
    // return this.service.getWorklistItems(resource);
    // }
    //
    // @Path("/items/organization-unit/")
    // @GET
    // // @Override
    // public @Nonnull List<WorklistItem> getWorklistItems(@QueryParam("resource") OrganizationUnitImpl resource) {
    // return this.service.getWorklistItems(resource);
    // }

    @Path("/items/{resource-type}--{resource-id}")
    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorklistItem> getWorklistItems(@PathParam("resource-type") ResourceType resourceType,
                                               @PathParam("resource-id") String resourceId) {

        UUID resourceUUID = UUID.fromString(resourceId);
        AbstractParticipant resource = null;
        
        // TODO refactor this
        if (resourceType == ResourceType.PARTICIPANT) {
            resource = this.identity.getParticipant(resourceUUID);
        }
        //AbstractResource<?> resource = this.identity.findResource(resourceType, resourceUUID);
        List<WorklistItem> items = this.service.getWorklistItems(resource);
        return items;
        // return null;
    }

    @Path("/item/{worklist-item-id}/claim/{resource-type}-{resource-id}")
    @POST
    @Override
    public void claimWorklistItemBy(@PathParam("worklist-item-id") String worklistItemId,
                                    @PathParam("resource-type") ResourceType resourceType,
                                    @PathParam("resource-id") String resourceId) {

        UUID resourceUUID = UUID.fromString(resourceId);
        // AbstractResource<?> resource = this.identity.findResource(resourceType, resourceUUID);
        UUID worklistItemUUID = UUID.fromString(worklistItemId);
        // WorklistItem worklistItem = this.service.getWorklistItem(resource, worklistItemUUID);
        // this.service.claimWorklistItemBy(worklistItem, resource);
    }

    @Path("/item/{worklist-item-id}/begin/{resource-type}-{resource-id}")
    @POST
    @Override
    public void beginWorklistItemBy(@PathParam("worklist-item-id") String worklistItemId,
                                    @PathParam("resource-type") ResourceType resourceType,
                                    @PathParam("resource-id") String resourceId) {

        UUID resourceUUID = UUID.fromString(resourceId);
        // AbstractResource<?> resource = this.identity.findResource(resourceType, resourceUUID);
        UUID worklistItemUUID = UUID.fromString(worklistItemId);
        // WorklistItem worklistItem = this.service.getWorklistItem(resource, worklistItemUUID);
        // this.service.beginWorklistItemBy(worklistItem, resource);
    }

    @Path("/item/{worklist-item-id}/complete/{resource-type}-{resource-id}")
    @POST
    @Override
    public void completeWorklistItemBy(@PathParam("worklist-item-id") String worklistItemId,
                                       @PathParam("resource-type") ResourceType resourceType,
                                       @PathParam("resource-id") String resourceId) {

        UUID resourceUUID = UUID.fromString(resourceId);
        UUID worklistItemUUID = UUID.fromString(worklistItemId);
        // AbstractResource<?> resource = this.identity.findResource(resourceType, resourceUUID);
        // WorklistItem worklistItem = this.service.getWorklistItem(resource, worklistItemUUID);
        // this.service.completeWorklistItemBy(worklistItem, resource);
    }

    @Path("/item/{worklist-item-id}/abort/{resource-type}-{resource-id}")
    @POST
    @Override
    public void abortWorklistItemBy(@PathParam("worklist-item-id") String worklistItemId,
                                    @PathParam("resource-type") ResourceType resourceType,
                                    @PathParam("resource-id") String resourceId) {

        UUID resourceUUID = UUID.fromString(resourceId);
        UUID worklistItemUUID = UUID.fromString(worklistItemId);
        // AbstractResource<?> resource = this.identity.findResource(resourceType, resourceUUID);
        // WorklistItem worklistItem = this.service.getWorklistItem(resource, worklistItemUUID);
        // this.service.abortWorklistItemBy(worklistItem, resource);
    }
}
