package org.jodaengine.rest.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jodaengine.IdentityService;
import org.jodaengine.JodaEngineServices;
import org.jodaengine.WorklistService;
import org.jodaengine.exception.InvalidWorkItemException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.forms.processor.FormProcessor;
import org.jodaengine.forms.processor.juel.JuelFormProcessor;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * API servlet providing an interface for the {@link WorklistService}.
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
//    TODO implements WorklistService

    private static final int RESPONSE_FAIL = 404;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final WorklistService service;
    private final IdentityService identity;

    /**
     * Default constructor.
     */
    public WorklistWebService(JodaEngineServices engineServices) {

        this.service = engineServices.getWorklistService();
        this.identity = engineServices.getIdentityService();
    }

    /**
     * Gets the items of the participant that are in the specified state.
     * Valid values for the state are:
     * - OFFERED
     * - ALLOCATED
     * - EXECUTING
     * 
     * If no item state is specified, the whole worklist is returned.
     *
     * @param participantId the id of the participant
     * @param itemState the state of the item
     * @return the items of the specified state
     * @throws ResourceNotAvailableException the resource not available exception
     */
    @Path("/items")
    @GET
    public List<AbstractWorklistItem> getWorklistItems(@QueryParam("id") String participantId,
        @QueryParam("itemState") String itemState)
    throws ResourceNotAvailableException {

        logger.debug("GET: {}", participantId);
        
        UUID participantUUID = UUID.fromString(participantId);
        // this will be the result collection
        List<AbstractWorklistItem> items = null;
                
        // no state specified return all worklist items
        if (itemState == null) {
            items = this.service.getWorklistItems(participantUUID);
        } else {
            WorklistItemState state = WorklistItemState.valueOf(itemState);
            
            switch (state) {
                case OFFERED:
                    items = service.getOfferedWorklistItems(participantUUID);
                    break;
                case ALLOCATED:   
                    items = service.getAllocatedWorklistItems(participantUUID);
                    break;
    
                case EXECUTING:
                    items = service.getExecutingWorklistItems(participantUUID);
                    break;
    
                default:
                    logger.debug("Query for unknown state of worklist items");
                    break;
            }
        }

        return items;
    }
    
    /**
     * Gets the items of the participant that are in the specified state.
     * Valid values for the state are:
     * - OFFERED
     * - ALLOCATED
     * - EXECUTING
     * 
     * @param participantId
     *            the UUID of the participant whose worklistitems we want to get
     * @param itemState
     *            the state of the item
     * @return the items of the specified state
     */

    /**
     * Gets the form that is held by the {@link WorklistItemImpl Worklist Item}.
     * 
     * @param worklistitemId
     *            the worklist item id
     * @param participantId
     *            the participant id
     * @return the form held by the worklist item
     * @throws ResourceNotAvailableException
     *             if the resource is not available
     * @throws InvalidWorkItemException
     *             if the item was not available
     */
    @Path("/items/{worklistitemId}/form")
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public Response getForm(@PathParam("worklistitemId") String worklistitemId,
                            @QueryParam("participantId") String participantId)
    throws ResourceNotAvailableException, InvalidWorkItemException {

        UUID participantUUID = UUID.fromString(participantId);
        UUID itemUUID = UUID.fromString(worklistitemId);
        logger.debug("GET: {}", worklistitemId);

        AbstractResource<?> resource = identity.getParticipant(participantUUID);
        AbstractWorklistItem item = service.getWorklistItem(resource, itemUUID);
        ProcessInstanceContext context = item.getCorrespondingToken().getInstance().getContext();

        FormProcessor processor = new JuelFormProcessor();
        String formHtml = processor.prepareForm(item.getForm(), context);
        
//        String html = populateForm(item.getForm(), context);
        return Response.ok(formHtml).build();

    }

    /**
     * Post the form to the engine and process the given arguments, so to say set process instance variables according
     * to the changes in the form.
     * !!! Be aware, every form data is saved as a String! !!!
     *
     * @param worklistItemId the worklistitem id
     * @param participantId the participant id
     * @param form the form that gets send to us
     * @return the response
     * @throws ResourceNotAvailableException the resource not available exception
     * @throws InvalidWorkItemException the invalid work item exception
     */
    @Path("/items/{worklistitemId}/form")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    public Response postForm(@PathParam("worklistitemId") String worklistItemId,
                             @QueryParam("participantId") String participantId,
                             MultivaluedMap<String, String> form)
    throws ResourceNotAvailableException, InvalidWorkItemException {

        UUID participantUUID = UUID.fromString(participantId);
        UUID itemUUID = UUID.fromString(worklistItemId);
        AbstractResource<?> resource = this.identity.getParticipant(participantUUID);

        // try to get the worklist item and its token and thereby the context to put in the new data
        AbstractWorklistItem item = this.service.getWorklistItem(resource, itemUUID);
        ProcessInstanceContext context = item.getCorrespondingToken().getInstance().getContext();

        FormProcessor processor = new JuelFormProcessor();
        
        // convert MultiValuedMap to Map. Duplicate entries might be overriden here
        Map<String, String> singleValueMap = new HashMap<String, String>();
        Set<Map.Entry<String, List<String>>> entrySet = form.entrySet();
        for (Map.Entry<String, List<String>> entry : entrySet) {
            // We just get the first value since values shall be unique
            singleValueMap.put(entry.getKey(), entry.getValue().get(0));
        }
        logger.debug("### {}", singleValueMap);
        
        processor.readFilledForm(singleValueMap, item.getForm(), context);
        
//        Set<Map.Entry<String, List<String>>> entrySet = form.entrySet();
//        for (Map.Entry<String, List<String>> entry : entrySet) {
//            // We just get the first value since values shall be unique
//            context.setVariable(entry.getKey(), entry.getValue().get(0));
//        }

        logger.debug(context.getVariableMap().toString());
        return Response.ok().build();

    }

    /**
     * It claims, begins or ends the item.
     * 
     * @param worklistItemId
     *            the id for the worklist item, given in the request
     * @param participantId
     *            the participant id given in the request
     * @param status
     *            the status of the worklistitem (allocated, executing or completed)
     * @return returns an empty response with a http status
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     * @throws InvalidWorkItemException
     *             the invalid work item exception
     */
    @Path("/items/{worklistItemId}/state")
    @Consumes(MediaType.TEXT_PLAIN)
    @PUT
    public Response actionOnWorklistitem(@PathParam("worklistItemId") String worklistItemId,
                                         @QueryParam("participantId") String participantId,
                                         String status)
    throws ResourceNotAvailableException, InvalidWorkItemException {

        // generates an enum from the given string value
        WorklistItemState state = WorklistItemState.valueOf(status);
        UUID itemUUID = UUID.fromString(worklistItemId);
        UUID participantUUID = UUID.fromString(participantId);
        logger.debug("entered action choosing");
        switch (state) {
            case ALLOCATED:

                logger.debug("success, now claiming");
                claimWorklistItem(itemUUID, participantUUID);
                break;

            case EXECUTING:

                logger.debug("success, now beginning");
                beginWorklistItem(itemUUID, participantUUID);
                break;

            case COMPLETED:

                logger.debug("success, now ending");
                endWorklistItem(itemUUID, participantUUID);
                break;

            default:
                logger.debug("no valid action could be found");
                return Response.status(RESPONSE_FAIL).build();
                
        }
                
        return Response.ok().build();
    }

    /**
     * Ends (finishes/completes) the worklist item. It is implemented by setting the state accordingly.
     *
     * @param id the id
     * @param participantId the participant id
     * @throws InvalidWorkItemException the invalid item exception
     * @throws ResourceNotAvailableException the resource not available exception
     */
    private void endWorklistItem(UUID id, UUID participantId)
    throws InvalidWorkItemException, ResourceNotAvailableException {

        AbstractResource<?> resource = identity.getParticipant(participantId);
        AbstractWorklistItem item;
        item = service.getWorklistItem(resource, id);
        service.completeWorklistItemBy(item, resource);
    }

    /**
     * Processes the claim action for the given user and the worklist item.
     *
     * @param worklistItemId the worklist item id
     * @param participantUUID the participant uuid
     * @throws ResourceNotAvailableException the resource not available exception
     * @throws InvalidWorkItemException the invalid work item exception
     */
    private void claimWorklistItem(UUID worklistItemId, UUID participantUUID)
    throws ResourceNotAvailableException, InvalidWorkItemException {

        AbstractResource<?> resource = identity.getParticipant(participantUUID);
        AbstractWorklistItem item = service.getWorklistItem(resource, worklistItemId);

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
     * @throws InvalidWorkItemException the invalid work item exception
     */
    private void beginWorklistItem(UUID worklistItemId, UUID participantUUID)
    throws ResourceNotAvailableException, InvalidWorkItemException {

        logger.debug("POST participantID: {}", participantUUID);
        logger.debug("worklistItemID: {}", worklistItemId);
        AbstractResource<?> resource = identity.getParticipant(participantUUID);
        AbstractWorklistItem item = service.getWorklistItem(resource, worklistItemId);

        logger.debug(item.toString());
        service.beginWorklistItemBy(item, resource);
    }
}
