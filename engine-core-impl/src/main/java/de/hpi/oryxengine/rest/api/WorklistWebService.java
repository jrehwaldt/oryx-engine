package de.hpi.oryxengine.rest.api;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.exception.InvalidItemException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;
import de.hpi.oryxengine.rest.WorklistActionWrapper;

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
    // implements WorklistServiceFacade {

    private static final int RESPONSE_FAIL = 404;

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

    /**
     * Gets the worklist items for a given resource (defined by a uuid which is a String and needs to be converted).
     * 
     * @param id
     *            the id as a String
     * @return the worklist items for the specified resource
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @Path("/items")
    @GET
    public List<AbstractWorklistItem> getWorklistItems(@QueryParam("id") String id)
    throws ResourceNotAvailableException {

        logger.debug("GET: {}", id);

        UUID uuid = UUID.fromString(id);
        List<AbstractWorklistItem> items = this.service.getWorklistItems(uuid);
        return items;
    }
    
    /**
     * Gets the form that is held by the {@link WorklistItemImpl Worklist Item}.
     *
     * @param id the worklist item id
     * @param pId the participant id
     * @return the form held by the worklist item
     * @throws ResourceNotAvailableException if the resource is not available
     */
    @Path("/items/{worklistItem-id}/form")
    @GET
    public Response getForm(@QueryParam("worklistItem-id") String id, @QueryParam("participantId") String pId)
    throws ResourceNotAvailableException {
        UUID participantId = UUID.fromString(pId);
        UUID itemId = UUID.fromString(id);
        logger.debug("GET: {}", id);
        
        AbstractResource<?> resource = identity.getParticipant(participantId);
        AbstractWorklistItem item;
        try {
            item = service.getWorklistItem(resource, itemId);
            return Response.ok(item.getForm().getFormContentAsHTML()).build();
        } catch (InvalidItemException e) {
            
            e.printStackTrace();
            return Response.status(RESPONSE_FAIL).build();
        }


    }
    
    /**
     * Checks what to do.
     * 
     * @param worklistItemId
     *            the id for the worklist item, given in the request
     * @param wrapper
     *            wrapper object for multiple parameters
     * @return returns an empty response with a http status
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @Path("/items/{worklistItem-id}/state")
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Response intitializeNextSteps(@PathParam("worklistItem-id") String worklistItemId,
                                         WorklistActionWrapper wrapper)
    throws ResourceNotAvailableException {

        UUID id = UUID.fromString(worklistItemId);
        logger.debug("entered method");
        try {
            switch (wrapper.getAction()) {
                case CLAIM:
    
                    logger.debug("success, now claiming");
                    claimWorklistItem(id, wrapper.getParticipantId());
                    // make these numbers constants
                    return Response.ok().build();
                    
                case BEGIN:
                    
                    logger.debug("success, now beginning");
                    // make these numbers constants
                    beginWorklistItem(id, wrapper.getParticipantId());
                    return Response.ok().build();
                    
                case END:
                    
                    logger.debug("success, now ");
                    endWorklistItem(id, wrapper.getParticipantId());
    
                    return Response.ok().build();
    
                default:
                    logger.debug("crap");
                    return Response.status(RESPONSE_FAIL).build();
    
            }
        } catch (InvalidItemException e1) {
            logger.debug("Invalid Item!");
            e1.printStackTrace();
            return Response.status(RESPONSE_FAIL).build();
        }

        
    }

    /**
     * Complete the worklist item.
     *
     * @param id the item id
     * @param participantId the participant id
     * @throws InvalidItemException the invalid item exception
     */
    private void endWorklistItem(UUID id, UUID participantId) throws InvalidItemException {
        
        AbstractResource<?> resource = identity.getParticipant(participantId);
        AbstractWorklistItem item;
        item = service.getWorklistItem(resource, id);
        service.completeWorklistItemBy(item, resource);
    }

    /**
     * Processes the claim action for the given user and the worklist item.
     * 
     * @param worklistItemId
     *            the worklist item id
     * @param participantUUID
     *            the participant uuid
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     * @throws InvalidItemException 
     */
    private void claimWorklistItem(UUID worklistItemId, UUID participantUUID)
    throws ResourceNotAvailableException, InvalidItemException {
        
        AbstractResource<?> resource = identity.getParticipant(participantUUID);
        AbstractWorklistItem item;
        
        item = service.getWorklistItem(resource, worklistItemId);
        logger.debug("POST participantID: {}", participantUUID);
        logger.debug("worklistItemID: {}", worklistItemId);
        service.claimWorklistItemBy(item, resource);

    }
    
    
    /**
     * Begin a worklist item. By beginning the item gets also claimed.
     *
     * @param worklistItemId the id of the worklist item
     * @param participantUUID the participant uuid
     * @throws ResourceNotAvailableException the resource not available exception
     * @throws InvalidItemException 
     */
    private void beginWorklistItem(UUID worklistItemId, UUID participantUUID)
    throws ResourceNotAvailableException, InvalidItemException {
        
        logger.debug("POST participantID: {}", participantUUID);
        logger.debug("worklistItemID: {}", worklistItemId);
        AbstractResource<?> resource = identity.getParticipant(participantUUID);
        AbstractWorklistItem item;
        item = service.getWorklistItem(resource, worklistItemId);
        logger.debug(item.toString());
        service.beginWorklistItemBy(item, resource);
    }

    /**
     * Claim a worklist item by aresource parameteres subject to change.
     * 
     * @param workItem
     *            the work item
     * @param resource
     *            the resource
     */
    @Path("/item/claim")
    @GET
    // Wieder unüblich. Ideal siehe "claimWorklistItemByPost"
    public void claimWorklistItemBy(@QueryParam("workItem") AbstractWorklistItem workItem,
                                    @QueryParam("resource") AbstractResource<?> resource) {

        // UUID resourceUUID = UUID.fromString(resourceId);
        // AbstractResource<?> resource = this.identity.findResource(resourceType, resourceUUID);
        // UUID worklistItemUUID = UUID.fromString(workItem);
        // WorklistItem worklistItem = this.service.getWorklistItem(resource, worklistItemUUID);

        logger.debug("POST-claim WI: {}", workItem);
        logger.debug("POST-claim Res: {}", resource);
        

        // this.service.claimWorklistItemBy(workItem, resource);

    }
}
