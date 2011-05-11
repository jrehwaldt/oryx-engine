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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.JodaEngineException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.resource.AbstractCapability;
import de.hpi.oryxengine.resource.AbstractOrganizationUnit;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractPosition;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.rest.PatchCollectionChangeset;
import de.hpi.oryxengine.util.annotations.PATCH;

/**
 * The Class IdentityWebService.
 */
@Path("/identity")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public final class IdentityWebService implements IdentityService, IdentityBuilder {
    
    private static final String NOT_ACCESSIBLE_VIA_WEBSERVICE = "This method is not accessible via web service.";
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final IdentityService identity;
    
    /**
     * Default constructor.
     */
    public IdentityWebService() {
        
        this.logger.info("Loading IdentityWebService...");
        this.identity = ServiceFactory.getIdentityService();
    }
    
    // ==============================================================
    // =================== IdentityServiceImpl ======================
    // ==============================================================
    
    @Override
    public IdentityBuilder getIdentityBuilder() {
        return identity.getIdentityBuilder();
    }
    
    // TODO the following methods with UUID in its signature will not yet work (see todo in other web service, resteasy)
    
    @Path("/participants")
    @GET
    @Override
    public Set<AbstractParticipant> getParticipants() {
        return this.identity.getParticipants();
    }
    
    @Path("/participants/{participantId}")
    @GET
    @Override
    public AbstractParticipant getParticipant(@PathParam("participantId") UUID participantId)
    throws ResourceNotAvailableException {
        return this.identity.getParticipant(participantId);
    }
    
    @Path("/roles")
    @GET
    @Override
    public Set<AbstractRole> getRoles() {
        return this.identity.getRoles();
    }
    
    @Path("/roles/{roleId}")
    @GET
    @Override
    public AbstractRole getRole(@PathParam("roleId") UUID roleId)
    throws ResourceNotAvailableException {
        return this.identity.getRole(roleId);
    }
    
    @Path("/positions")
    @GET
    @Override
    public Set<AbstractPosition> getPositions() {
        return this.identity.getPositions();
    }
    
    @Path("/positions/{positionId}")
    @GET
    @Override
    public AbstractPosition getPosition(@PathParam("positionId") UUID positionId)
    throws ResourceNotAvailableException {
        return this.identity.getPosition(positionId);
    }
    
    @Path("/organization-units")
    @GET
    @Override
    public Set<AbstractOrganizationUnit> getOrganizationUnits() {
        return this.identity.getOrganizationUnits();
    }
    
    @Path("/organization-units/{organizationUnitId}")
    @GET
    @Override
    public AbstractOrganizationUnit getOrganizationUnit(@PathParam("organizationUnitId") UUID organizationUnitId)
    throws ResourceNotAvailableException {
        return this.identity.getOrganizationUnit(organizationUnitId);
    }
    
    /**
     * Gets all participants for the specified role.
     * 
     * @param roleId
     *            the role id
     * @return the participants for role
     * @throws ResourceNotAvailableException
     *             thrown if the specified role does not exist
     */
    @Path("/roles/{roleId}/participants")
    @GET
    public Set<AbstractParticipant> getParticipantsForRole(@PathParam("roleId") String roleId)
    throws ResourceNotAvailableException {
        
        UUID roleUUID = UUID.fromString(roleId);
        return identity.getRole(roleUUID).getParticipantsImmutable();
    }
    
    // ==============================================================
    // =================== IdentityBuilderImpl ======================
    // ==============================================================
    
    /**
     * Creates a participant with a given name.
     * 
     * @param participantName
     *            the participant name
     * @return the response whether the API call was successful
     */
    @Path("/participants")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Override
    public AbstractParticipant createParticipant(String participantName) {
        
        // TODO ask Gerardo, why we need the Impl here/why the Impl has methods that are not specified in the interface.
        
        IdentityBuilder builder = getIdentityBuilder();
        AbstractParticipant participant = builder.createParticipant(participantName);
        
        return participant;
    }

    /**
     * Deletes a participant with the given id.
     * 
     * @param id
     *            the id
     * @return the response whether the API call was successful
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @Path("/participants/{participantId}")
    @DELETE
    public IdentityBuilder deleteParticipant(@PathParam("participantId") String id)
    throws ResourceNotAvailableException {
        
        return deleteParticipant(UUID.fromString(id));
    }

    @Override
    public IdentityBuilder deleteParticipant(UUID participantID)
    throws ResourceNotAvailableException {
        
        getIdentityBuilder().deleteParticipant(participantID);
        return NullIndentityBuilder.getInstance();
    }

    /**
     * Creates a role with a given name.
     * 
     * @param roleName
     *            the name of the role
     * @return the response whether the API call was successful
     */
    @Path("/roles")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Override
    public AbstractRole createRole(String roleName) {
        
        return getIdentityBuilder().createRole(roleName);
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
    @Path("/roles/{roleId}")
    @DELETE
    public IdentityBuilder deleteRole(@PathParam("roleId") String id)
    throws Exception {
        
        return deleteRole(UUID.fromString(id));
    }

    @Override
    public IdentityBuilder deleteRole(UUID roleID)
    throws Exception {
        getIdentityBuilder().deleteRole(roleID);
        return NullIndentityBuilder.getInstance();
    }

    /**
     * Adds the participant as specified in the post request body to the role.
     * 
     * @param roleId
     *            the role id
     * @param changeset
     *            the changeset that contains the UUIDs (as Strings) of the participants to add/remove
     * @return the response whether the API call was successful
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @Path("/roles/{roleId}/participants")
    @PATCH
    public Response changeParticipantRoleAssignment(@PathParam("roleId") String roleId,
                                                    PatchCollectionChangeset<String> changeset)
    throws ResourceNotAvailableException {
        
        // TODO do NOT return Response here -> throw an exception, map via ExceptionMapper to response code!
        UUID roleUUID = UUID.fromString(roleId);

        List<String> additions = changeset.getAdditions();
        List<String> removals = changeset.getRemovals();

        if (!Collections.disjoint(additions, removals)) {
            // additions and removals have some elements in common
            // do error handling here
            return Response.status(Status.BAD_REQUEST).build();
        }

        IdentityBuilder builder = getIdentityBuilder();
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
    
    /**
     * Creates the participant and already assigns him a role.
     *
     * @param roleId the role id
     * @param participantName the participant name
     * @return the response
     * @throws ResourceNotAvailableException  
     */
    @Path("/roles/{roleId}/participants")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public AbstractParticipant createParticipantForRole(@PathParam("roleId") String roleId,
                                                        String participantName)
    throws ResourceNotAvailableException {
        
        UUID roleUUID = UUID.fromString(roleId);
        IdentityBuilder builder = getIdentityBuilder();
        AbstractParticipant participant = builder.createParticipant(participantName);
        builder.participantBelongsToRole(participant.getID(), roleUUID);
        
        return participant;
    }

    @Override
    public AbstractPosition createPosition(String positionName) {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public IdentityBuilder deletePosition(UUID positionID)
    throws JodaEngineException {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public IdentityBuilder subRoleOf(UUID subRoleID,
                                     UUID superRoleID) {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public IdentityBuilder participantBelongsToRole(UUID participantID,
                                                    UUID roleID)
    throws ResourceNotAvailableException {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public IdentityBuilder participantDoesNotBelongToRole(UUID participantID,
                                                          UUID roleID)
    throws ResourceNotAvailableException {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public IdentityBuilder participantHasCapability(UUID participantID,
                                                    AbstractCapability capability) {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public AbstractOrganizationUnit createOrganizationUnit(String organizationUnitName) {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public IdentityBuilder deleteOrganizationUnit(UUID organizationUnit)
    throws JodaEngineException {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }
    
    @Override
    public IdentityBuilder positionReportsToSuperior(UUID positionID,
                                                     UUID superiorpositionID)
    throws JodaEngineException {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }
    
    @Override
    public AbstractCapability createCapability(String capabilityId) {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }
    
    @Override
    public IdentityBuilder participantOccupiesPosition(UUID participantID,
                                                       UUID positionID)
    throws ResourceNotAvailableException {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }
    
    @Override
    public IdentityBuilder participantDoesNotOccupyPosition(UUID participantID,
                                                            UUID positionID)
    throws ResourceNotAvailableException {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public IdentityBuilder organizationUnitOffersPosition(UUID organizationUnit,
                                                          UUID positionID)
    throws ResourceNotAvailableException {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public IdentityBuilder organizationUnitDoesNotOfferPosition(UUID organizationUnit,
                                                                UUID positionID)
    throws ResourceNotAvailableException {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public IdentityBuilder subOrganizationUnitOf(UUID subOrganizationUnit,
                                                 UUID superOrganizationUnit)
    throws JodaEngineException {
        
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }
    
    /**
     * We provide an empty identity builder. NullObjectPattern, you know?
     * 
     * @author Jan Rehwaldt
     */
    private static class NullIndentityBuilder implements IdentityBuilder {
        
        private static final IdentityBuilder BUILDER = new NullIndentityBuilder();
        
        /**
         * Provides a singleton null instance.
         * 
         * @return the null builder
         */
        private static IdentityBuilder getInstance() {
            return BUILDER;
        }
        
        @Override
        public AbstractParticipant createParticipant(String participantName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder deleteParticipant(UUID participantID)
        throws ResourceNotAvailableException {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder participantOccupiesPosition(UUID participantID, UUID positionID)
        throws ResourceNotAvailableException {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder participantDoesNotOccupyPosition(UUID participantID, UUID positionID)
        throws ResourceNotAvailableException {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder participantBelongsToRole(UUID participantID, UUID roleID)
        throws ResourceNotAvailableException {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder participantDoesNotBelongToRole(UUID participantID, UUID roleID)
        throws ResourceNotAvailableException {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder participantHasCapability(UUID participantID, AbstractCapability capability) {
            throw new UnsupportedOperationException();
        }

        @Override
        public AbstractOrganizationUnit createOrganizationUnit(String organizationUnitName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder deleteOrganizationUnit(UUID organizationUnit)
        throws JodaEngineException {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder organizationUnitOffersPosition(UUID organizationUnit, UUID positionID)
        throws ResourceNotAvailableException {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder organizationUnitDoesNotOfferPosition(UUID organizationUnit, UUID positionID)
        throws ResourceNotAvailableException {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder subOrganizationUnitOf(UUID subOrganizationUnit, UUID superOrganizationUnit)
        throws JodaEngineException {
            throw new UnsupportedOperationException();
        }

        @Override
        public AbstractPosition createPosition(String positionName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder deletePosition(UUID positionID)
        throws JodaEngineException {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder positionReportsToSuperior(UUID positionID, UUID superiorpositionID)
        throws JodaEngineException {
            throw new UnsupportedOperationException();
        }

        @Override
        public AbstractRole createRole(String roleName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder deleteRole(UUID roleID)
        throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        public IdentityBuilder subRoleOf(UUID subRoleID, UUID superRoleID) {
            throw new UnsupportedOperationException();
        }

        @Override
        public AbstractCapability createCapability(String capabilityId) {
            throw new UnsupportedOperationException();
        }
    }
}
