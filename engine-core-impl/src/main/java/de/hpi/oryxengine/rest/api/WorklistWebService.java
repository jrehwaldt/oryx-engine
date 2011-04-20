package de.hpi.oryxengine.rest.api;

import java.util.List;
import java.util.UUID;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;

/**
 * API servlet providing an interface for the worklist manager.
 * 
 * More info: http://jersey.java.net/nonav/documentation/latest/user-guide.html#getting-started
 * 
 * @author Jan Rehwaldt
 * @since 2011-03-24
 */
@Path("/worklist")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public final class WorklistWebService {
//implements WorklistServiceFacade {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
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
     * Creates a demo participant with a work item.
     * 
     * @return ID of the created participant
     */
    @Path("/demo")
    @GET
    @Produces("text/plain")
    public String demoParticipant() {
        
        IdentityBuilder builder = this.identity.getIdentityBuilder();
        AbstractParticipant thomas = builder.createParticipant("Thomas Strunz");
        
        Task task = new TaskImpl("Kaffee holen", "Bohnenkaffee", null, thomas);
        Token token = new TokenImpl(null);
        AbstractWorklistItem item = new WorklistItemImpl(task, token);
        thomas.getWorklist().addWorklistItem(item);
        
        return thomas.getID().toString();
    }

    // @Path("/items/")
    // @GET
    // // @Override
    // public @Nonnull List<WorklistItem> getWorklistItems(ResourceImpl<?> resource) {
    //     return this.service.getWorklistItems(resource);
    // }
    //
    // @Path("/items/position/")
    // @GET
    // // @Override
    // public @Nonnull List<WorklistItem> getWorklistItems(Resource<?> resource) {
    //     return this.service.getWorklistItems(resource);
    // }
    //
    // @Path("/items/organization-unit/")
    // @GET
    // // @Override
    // public @Nonnull List<WorklistItem> getWorklistItems(OrganizationUnitImpl resource) {
    //     return this.service.getWorklistItems(resource);
    // }
    
    /**
     * Gets the worklist items for a given resource (defined by a uuid which is a String and needs to be converted).
     *
     * @param id the id as a String
     * @return the worklist items for the specified resource
     * @throws ResourceNotAvailableException the resource not available exception
     */
    @Path("/items")
    @GET
    public List<AbstractWorklistItem> getWorklistItems(@QueryParam("id") String id)
    throws ResourceNotAvailableException {

        logger.debug("GET: {}", id);
        
        UUID uuid = UUID.fromString(id);
        List<AbstractWorklistItem> items = this.service.getWorklistItems(uuid);
        logger.debug("Jannik hacks");
        return items;
    }

 /*   @Path("/items")
    @POST
    // Qual der Wahl! So soll's sein.
    public List<AbstractWorklistItem> getWorklistItemsAsPost(AbstractResource<?> resource)
    throws ResourceNotAvailableException {
        logger.debug("POST: {}", resource);
        return getWorklistItems(resource);
    }*/
    
    /**
     * Claims a worklist item via POST request.
     *
     * @param worklistItemId the id for the worklist item, given in the request
     * @param resource the json version of a resource
     * @throws ResourceNotAvailableException the resource not available exception
     */
    @Path("/{worklistItem-id}/claim")
    @Consumes(MediaType.APPLICATION_JSON) 
    @POST
    // Qual der Wahl! So soll's sein.
    public void claimWorklistItemBy(@PathParam("worklistItem-id") String worklistItemId, AbstractResource<?> resource)
    throws ResourceNotAvailableException {
        logger.debug("POST: {}", resource);
        logger.debug("Request: {}", worklistItemId);
//      AbstractResource<?> resource = this.identity.findResource(resourceType, resourceUUID);
        UUID id = UUID.fromString(worklistItemId);
        
        AbstractWorklistItem item = service.getWorklistItem(resource, id);
        
        service.claimWorklistItemBy(item, resource);
    }
    
    /**
     * Claim a worklist item by aresource parameteres subject to change.
     *
     * @param workItem the work item
     * @param resource the resource
     */
    @Path("/item/claim")
    @GET
    // Wieder unüblich. Ideal siehe "claimWorklistItemByPost"
    public void claimWorklistItemBy(@QueryParam("workItem") AbstractWorklistItem workItem,
                                    @QueryParam("resource") AbstractResource<?> resource) {
        
//        UUID resourceUUID = UUID.fromString(resourceId);
//        AbstractResource<?> resource = this.identity.findResource(resourceType, resourceUUID);
//        UUID worklistItemUUID = UUID.fromString(workItem);
//        WorklistItem worklistItem = this.service.getWorklistItem(resource, worklistItemUUID);
        
        logger.debug("POST-claim WI: {}", workItem);
        logger.debug("POST-claim Res: {}", resource);
        
//        this.service.claimWorklistItemBy(workItem, resource);
        
    }
    
//    @Path("/item/claim")
//    @POST
//    public void claimWorklistItemByPost(AbstractWorklistItem workItem,
//                                        AbstractResource<?> resource) {
//        
////        UUID resourceUUID = UUID.fromString(resourceId);
////        AbstractResource<?> resource = this.identity.findResource(resourceType, resourceUUID);
////        UUID worklistItemUUID = UUID.fromString(workItem);
////        WorklistItem worklistItem = this.service.getWorklistItem(resource, worklistItemUUID);
//        
//        logger.debug("POST-claim WI: {}", workItem);
//        logger.debug("POST-claim Res: {}", resource);
//        
////        this.service.claimWorklistItemBy(workItem, resource);
//        
//    }
    
//    @Path("/items/{resource-type}/{resource-id}")
//    @GET
////    @Override
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<AbstractWorklistItem> getWorklistItems(@PathParam("resource-type") ResourceType resourceType,
//                                                       @PathParam("resource-id") String resourceId)
//    throws ResourceNotAvailableException {
//        
//        UUID resourceUUID = UUID.fromString(resourceId);
//        AbstractResource<?> resource = getResource(resourceType, resourceUUID);
//        return getWorklistItems(resource);
//    }
    
//    @Path("/item/{worklist-item-id}/claim/{resource-type}-{resource-id}")
//    @POST
//    @Override
//    public void claimWorklistItemBy(@PathParam("worklist-item-id") String worklistItemId,
//                                    @PathParam("resource-type") ResourceType resourceType,
//                                    @PathParam("resource-id") String resourceId) {
//
//        UUID resourceUUID = UUID.fromString(resourceId);
//        // AbstractResource<?> resource = this.identity.findResource(resourceType, resourceUUID);
//        UUID worklistItemUUID = UUID.fromString(worklistItemId);
//        // WorklistItem worklistItem = this.service.getWorklistItem(resource, worklistItemUUID);
//        // this.service.claimWorklistItemBy(worklistItem, resource);
//    }
//
//    @Path("/item/{worklist-item-id}/begin/{resource-type}-{resource-id}")
//    @POST
//    @Override
//    public void beginWorklistItemBy(@PathParam("worklist-item-id") String worklistItemId,
//                                    @PathParam("resource-type") ResourceType resourceType,
//                                    @PathParam("resource-id") String resourceId) {
//
//        UUID resourceUUID = UUID.fromString(resourceId);
//        // AbstractResource<?> resource = this.identity.findResource(resourceType, resourceUUID);
//        UUID worklistItemUUID = UUID.fromString(worklistItemId);
//        // WorklistItem worklistItem = this.service.getWorklistItem(resource, worklistItemUUID);
//        // this.service.beginWorklistItemBy(worklistItem, resource);
//    }
//
//    @Path("/item/{worklist-item-id}/complete/{resource-type}-{resource-id}")
//    @POST
//    @Override
//    public void completeWorklistItemBy(@PathParam("worklist-item-id") String worklistItemId,
//                                       @PathParam("resource-type") ResourceType resourceType,
//                                       @PathParam("resource-id") String resourceId) {
//
//        UUID resourceUUID = UUID.fromString(resourceId);
//        UUID worklistItemUUID = UUID.fromString(worklistItemId);
//        // AbstractResource<?> resource = this.identity.findResource(resourceType, resourceUUID);
//        // WorklistItem worklistItem = this.service.getWorklistItem(resource, worklistItemUUID);
//        // this.service.completeWorklistItemBy(worklistItem, resource);
//    }
//
//    @Path("/item/{worklist-item-id}/abort/{resource-type}-{resource-id}")
//    @POST
//    @Override
//    public void abortWorklistItemBy(@PathParam("worklist-item-id") String worklistItemId,
//                                    @PathParam("resource-type") ResourceType resourceType,
//                                    @PathParam("resource-id") String resourceId) {
//
//        UUID resourceUUID = UUID.fromString(resourceId);
//        UUID worklistItemUUID = UUID.fromString(worklistItemId);
//        // AbstractResource<?> resource = this.identity.findResource(resourceType, resourceUUID);
//        // WorklistItem worklistItem = this.service.getWorklistItem(resource, worklistItemUUID);
//        // this.service.abortWorklistItemBy(worklistItem, resource);
//    }
   
    /**
     * Gets a resource by id.
     * 
     * @param resourceType the resource's type
     * @param resourceId the resource's id
     * @return the resource or null, if none found
     */
/*    private @Nullable AbstractResource<?> getResource(@Nonnull ResourceType resourceType,
                                                      @Nonnull UUID resourceId) {
        
        AbstractResource<?> resource = null;
        switch (resourceType) {
            case PARTICIPANT:
                resource = this.identity.getParticipant(resourceId);
                break;
            case CAPABILITY:
//                TODO what is this stuff? resource = this.identity.getCapability(resourceUUID);
                break;
            case POSITION:
                resource = this.identity.getPosition(resourceId);
                break;
            case ORGANIZATION_UNIT:
                resource = this.identity.getOrganizationUnit(resourceId);
                break;
            case ROLE:
                resource = this.identity.getRole(resourceId);
                break;
            default:
                // will not occur
                break;
        }
        
        return resource;
    }
    */
    /**
     * Refreshs a resource with its detached counterpart.
     * 
     * @param resource the resource
     * @return the resource or null, if none found
     * @throws ResourceNotAvailableException thrown if the requested resource could not be found
     */
    /*private @Nonnull AbstractResource<?> refreshResource(@Nonnull AbstractResource<?> resource)
    throws ResourceNotAvailableException {
        resource = getResource(resource.getType(), resource.getID());
        
        if (resource == null) {
            throw new ResourceNotAvailableException();
        }
        
        return resource;
    }*/
}
