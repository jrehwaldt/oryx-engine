package de.hpi.oryxengine.rest.api;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.exception.InvalidItemException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;
import de.hpi.oryxengine.rest.WorklistActionWrapper;

import net.htmlparser.jericho.Config;
import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.FormFields;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

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
    @Produces(MediaType.TEXT_PLAIN)
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
     * @param worklistitemId the worklist item id
     * @param participantId the participant id
     * @return the form held by the worklist item
     * @throws ResourceNotAvailableException if the resource is not available
     */
    @Path("/items/{worklistitemId}/form")
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public Response getForm(@PathParam("worklistitemId") String worklistitemId, 
                            @QueryParam("participantId") String participantId)
    throws ResourceNotAvailableException {
        UUID participantUUID = UUID.fromString(participantId);
        UUID itemUUID = UUID.fromString(worklistitemId);
        logger.debug("GET: {}", worklistitemId);
        
        AbstractResource<?> resource = identity.getParticipant(participantUUID);
        try {
            AbstractWorklistItem item = service.getWorklistItem(resource, itemUUID);            
            ProcessInstanceContext context = item.getCorrespondingToken().getInstance().getContext();
            
            String html = populateForm(item.getForm(), context);
            return Response.ok(html).build();
        } catch (InvalidItemException e) {
            
            logger.error("Failed fetching the item", e);
            return Response.status(RESPONSE_FAIL).build();
        }
    }
    
    /**
     * This method populates the given form with data from the context.
     * It is required, that the input fields in the form and the context variables have exactly the same name.
     *
     * @param form the form
     * @param context the context
     * @return the string
     */
    private String populateForm(Form form, ProcessInstanceContext context) {
        // TODO move this configuration to a place, where it is only executed once
        Config.CurrentCompatibilityMode.setFormFieldNameCaseInsensitive(false);
        String unpopulatedFormHtml = form.getFormContentAsHTML();
        Source source = new Source(unpopulatedFormHtml);
        FormFields formFields = source.getFormFields();
        formFields.clearValues();
        
        
        for (FormField field : formFields) {
            String fieldName = field.getName();
            Object variable = context.getVariable(fieldName);
            if (variable != null) {
                formFields.addValue(fieldName, variable.toString());
            }
        }
        OutputDocument output = new OutputDocument(source);
        output.replace(formFields);
        return output.toString();
    }

    /**
     * Post the form to the engine and process the given arguments, so to say set process instance variables according
     * to the changes in the form.
     * !!! Be aware, every form data is saved as a String! !!! 
     * 
     * @param worklistItemId
     *            the worklistitem id
     * @param participantId
     *            the participant id
     * @param form
     *            the form that gets send to us
     * @return the response
     */
    @Path("/items/{worklistitemId}/form")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    public Response postForm(@PathParam("worklistitemId") String worklistItemId, 
                             @QueryParam("participantId") String participantId, 
                             MultivaluedMap<String, String> form) {
        
        UUID participantUUID = UUID.fromString(participantId);
        UUID itemUUID = UUID.fromString(worklistItemId);
        AbstractResource<?> resource = identity.getParticipant(participantUUID);
                
        // try to get the worklistitem and its token and thereby the context to put in the new data
        try {
            AbstractWorklistItem item = service.getWorklistItem(resource, itemUUID);
            item = service.getWorklistItem(resource, itemUUID);
            ProcessInstanceContext context = item.getCorrespondingToken().getInstance().getContext();
            
            
            
            Set<Map.Entry<String, List<String>>> entrySet = form.entrySet();
            for (Map.Entry<String, List<String>> entry : entrySet) {
                // We just get the first value since values shall be unique
                context.setVariable(entry.getKey(), entry.getValue().get(0));
            }
            
            logger.debug(context.getVariableMap().toString());
            return Response.ok().build();
            
        } catch (InvalidItemException e) {
            
            logger.error("Failed fetching the item", e);
            return Response.status(RESPONSE_FAIL).build();
        }
        
    }
    
    /**
     * It claims, begins or ends the item.
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
    public Response actionOnWorklistitem(@PathParam("worklistItem-id") String worklistItemId,
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
     * Ends (finishes/completes) the worklist item. It is implemented by setting the state accordingly.
     *
     * @param id the id
     * @param participantId the participant id
     * @throws InvalidItemException the invalid item exception
     */
    private void endWorklistItem(UUID id, UUID participantId)  throws InvalidItemException {
        
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
     * @throws InvalidItemException 
     */
    private void beginWorklistItem(UUID worklistItemId, UUID participantUUID)
    throws ResourceNotAvailableException, InvalidItemException {
        
        logger.debug("POST participantID: {}", participantUUID);
        logger.debug("worklistItemID: {}", worklistItemId);
        AbstractResource<?> resource = identity.getParticipant(participantUUID);
        AbstractWorklistItem item = service.getWorklistItem(resource, worklistItemId);

        logger.debug(item.toString());
        service.beginWorklistItemBy(item, resource);
    }
}
