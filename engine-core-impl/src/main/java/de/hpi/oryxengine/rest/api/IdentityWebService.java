package de.hpi.oryxengine.rest.api;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.IdentityBuilderImpl;
import de.hpi.oryxengine.rest.PatchCollectionChangeset;
import de.hpi.oryxengine.util.annotations.PATCH;

/**
 * The Class IdentityWebService.
 */
@Path("/identity")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public final class IdentityWebService {

    private final IdentityService identity;

    // private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Default constructor.
     */
    public IdentityWebService() {

        this.identity = ServiceFactory.getIdentityService();
        // DemoDataForWebservice.generate();
    }

    /**
     * Get all participants.
     * 
     * @return json
     */
    @Path("/participants")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<AbstractParticipant> getParticipants() {

        Set<AbstractParticipant> participants = this.identity.getParticipants();

        return participants;

    }

    /**
     * Creates a participant with a given name.
     * 
     * @param participantName
     *            the participant name
     * @return the response whether the API call was successful
     */
    @Path("/participants")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    public Response createParticipant(String participantName) {

        // TODO ask Gerardo, why we need the Impl here/why the Impl has methods that are not specified in the interface.
        IdentityServiceImpl identityServiceImpl = (IdentityServiceImpl) identity;

        IdentityBuilder builder = new IdentityBuilderImpl(identityServiceImpl);
        builder.createParticipant(participantName);

        return Response.ok("Role " + participantName + " was created.").build();

    }

    /**
     * Deletes a participant with the given id.
     * 
     * @param id
     *            the id
     * @return the response whether the API call was successful
     * @throws Exception
     *             the exception
     */
    @Path("/participants")
    @DELETE
    public Response deleteParticipant(@QueryParam("participant-id") String id)
    throws Exception {

        // TODO write an Exception-Provider
        IdentityServiceImpl identityServiceImpl = (IdentityServiceImpl) identity;

        IdentityBuilder builder = new IdentityBuilderImpl(identityServiceImpl);
        UUID participantID = UUID.fromString(id);
        builder.deleteParticipant(participantID);

        return Response.ok().build();

    }

    /**
     * Gets all roles.
     * 
     * @return json
     */
    @Path("/roles")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<AbstractRole> getRoles() {

        Set<AbstractRole> roles = this.identity.getRoles();
        return roles;

    }

    /**
     * Creates a role with a given name.
     * 
     * @param roleName
     *            the name of the role
     * @return the response whether the API call was successful
     */
    @Path("/roles")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    public Response createRole(String roleName) {

        IdentityServiceImpl identityServiceImpl = (IdentityServiceImpl) identity;

        IdentityBuilder builder = new IdentityBuilderImpl(identityServiceImpl);
        builder.createRole(roleName);

        return Response.ok("Role " + roleName + " was created.").build();

    }

    /**
     * Deletes a role with the given id.
     * 
     * @param id
     *            the id of the role to delete
     * @return the response whether the API call was successful
     * @throws Exception
     *             the exception
     */
    @Path("/roles")
    @DELETE
    public Response deleteRole(@QueryParam("role-id") String id)
    throws Exception {

        IdentityServiceImpl identityServiceImpl = (IdentityServiceImpl) identity;

        IdentityBuilder builder = new IdentityBuilderImpl(identityServiceImpl);
        UUID roleID = UUID.fromString(id);
        builder.deleteRole(roleID);

        return Response.ok().build();
    }
    
    

    /**
     * Adds the participant as specified in the post request body to the role.
     *
     * @param roleID the role id
     * @param changeset the changeset that contains the UUIDs (as Strings) of the participants to add/remove 
     * @return the response whether the API call was successful
     * @throws ResourceNotAvailableException the resource not available exception
     */
    @Path("/roles/{roleID}/participants")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addParticipantsToRole(@PathParam("roleID") String roleID, PatchCollectionChangeset<String> changeset)
    throws ResourceNotAvailableException {

        IdentityServiceImpl identityServiceImpl = (IdentityServiceImpl) identity;
        UUID roleUUID = UUID.fromString(roleID);

        List<String> additions = changeset.getAdditions();
        List<String> removals = changeset.getRemovals();
        
        if (!Collections.disjoint(additions, removals)) {
            // additions and removals have some elements in common
            // do error handling here
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        IdentityBuilder builder = new IdentityBuilderImpl(identityServiceImpl);
        for (String participantID : additions) {
            UUID participantUUID = UUID.fromString(participantID);
            builder.participantBelongsToRole(participantUUID, roleUUID);
        }
        for (String participantID : removals) {
            UUID participantUUID = UUID.fromString(participantID);
            builder.participantDoesNotBelongToRole(participantUUID, roleUUID);
        }

        return Response.ok().build();
    }

}
