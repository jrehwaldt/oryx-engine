package de.hpi.oryxengine.resource;

import java.util.UUID;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.exception.DalmatinaException;

/**
 * Implementation of {@link IdentityBuilder} Interface.
 */
public class IdentityBuilderImpl implements IdentityBuilder {

    /** The identity service. */
    private IdentityServiceImpl identityService;

    /**
     * Default Constructor.
     * 
     * @param identityServiceImpl
     *            - the IdentityServiceImpl where to build the organization structure on
     */
    public IdentityBuilderImpl(@Nonnull IdentityServiceImpl identityServiceImpl) {

        identityService = identityServiceImpl;
    }

    // -------- Participant Builder Methods -----------

    @Override
    public ParticipantImpl createParticipant(String participantName) {

        ParticipantImpl participant = new ParticipantImpl(participantName);

        identityService.getParticipantImpls().put(participant.getID(), participant);

        return participant;
    }

    @Override
    public IdentityBuilder deleteParticipant(UUID participantId)
    throws DalmatinaException {

        ParticipantImpl participantImpl = this.identityService.getParticipant(participantId);

        for (PositionImpl positionImpl: participantImpl.getMyPositions()) {
            positionImpl.setPositionHolder(null);
        }

        identityService.getParticipantImpls().remove(participantImpl.getID());
        return this;
    }

    @Override
    public IdentityBuilder participantOccupiesPosition(UUID participantId,
                                                       UUID positionId) {

        PositionImpl positionImpl = this.identityService.getPosition(positionId);
        ParticipantImpl participantImpl = this.identityService.getParticipant(participantId);

        ParticipantImpl oldParticiant = positionImpl.getPositionHolder();
        if (oldParticiant != null) {
            if (!oldParticiant.equals(participantImpl)) {
                oldParticiant.getMyPositions().remove(positionImpl);
            }
        }

        positionImpl.setPositionHolder(participantImpl);
        participantImpl.getMyPositions().add(positionImpl);

        return this;
    }

    @Override
    public IdentityBuilder participantDoesNotOccupyPosition(UUID participantId,
                                                            UUID positionId) {

        PositionImpl positionImpl = this.identityService.getPosition(positionId);
        ParticipantImpl participantImpl = this.identityService.getParticipant(participantId);

        positionImpl.belongsToOrganization(null);
        participantImpl.getMyPositions().remove(positionImpl);
        return this;
    }
    
    @Override
    public IdentityBuilder participantHasCapability(UUID participantId,
                                                    CapabilityImpl capability) {
        
        return null; // TODO
    }

    @Override
    public IdentityBuilder participantBelongsToRole(UUID participantId,
                                                    UUID roleId) {

        RoleImpl roleImpl = this.identityService.getRole(roleId);
        ParticipantImpl participantImpl = this.identityService.getParticipant(participantId);

        roleImpl.getParticipants().add(participantImpl);
        participantImpl.getMyRoles().add(roleImpl);

        return this;
    }

    @Override
    public IdentityBuilder participantDoesNotBelongToRole(UUID participantId,
                                                          UUID roleId) {

        RoleImpl roleImpl = this.identityService.getRole(roleId);
        ParticipantImpl participantImpl = this.identityService.getParticipant(participantId);

        roleImpl.getParticipants().remove(participantImpl);
        participantImpl.getMyRoles().remove(roleImpl);
        return this;
    }

    // -------- Capability Builder Methods ------------

    @Override
    public CapabilityImpl createCapability(String capabilityId) {

        // hier könnte man das FlyWeight-Pattern verwenden ...
        // Capability capability = new CapabilityImpl(capabilityId);

        // return capability;
        return null; // TODO
    }

    // -------- OrganizationUnit Builder Methods ------

    @Override
    public OrganizationUnitImpl createOrganizationUnit(String organizationUnitName) {

        OrganizationUnitImpl organizationUnitImpl = new OrganizationUnitImpl(organizationUnitName);

        identityService.getOrganizationUnitImpls().put(organizationUnitImpl.getID(), organizationUnitImpl);

        return organizationUnitImpl;
    }

    @Override
    public IdentityBuilder deleteOrganizationUnit(UUID organizationUnitId) {

        OrganizationUnitImpl organizationUnitImpl = this.identityService.getOrganizationUnit(organizationUnitId);

        for (OrganizationUnitImpl childOrganizationUnitImpl: organizationUnitImpl.getChildOrganisationUnits()) {
            childOrganizationUnitImpl.setSuperOrganizationUnit(null);
        }

        for (PositionImpl positionImpl : organizationUnitImpl.getPositions()) {
            positionImpl.belongsToOrganization(null);
        }

        identityService.getOrganizationUnitImpls().remove(organizationUnitImpl.getID());
        return this;
    }

    @Override
    public IdentityBuilder subOrganizationUnitOf(UUID subOrganizationUnitId,
                                                 UUID superOrganizationUnitId)
    throws DalmatinaException {

        OrganizationUnitImpl organizationUnitImpl = this.identityService.getOrganizationUnit(subOrganizationUnitId);
        OrganizationUnitImpl superOrganizationUnitImpl =
            this.identityService.getOrganizationUnit(superOrganizationUnitId);

        if (organizationUnitImpl.equals(superOrganizationUnitImpl)) {
            throw new DalmatinaException("The OrganizationUnit cannot be the superior of yourself.");
        }

        organizationUnitImpl.setSuperOrganizationUnit(superOrganizationUnitImpl);

        superOrganizationUnitImpl.getChildOrganisationUnits().add(organizationUnitImpl);

        return this;
    }

    @Override
    public IdentityBuilder organizationUnitOffersPosition(UUID organizationUnitId, UUID positionId) {

        PositionImpl positionImpl = this.identityService.getPosition(positionId);
        OrganizationUnitImpl organizationUnitImpl = this.identityService.getOrganizationUnit(organizationUnitId);

        OrganizationUnitImpl oldOrganizationUnit = (OrganizationUnitImpl) positionImpl.belongsToOrganization();
        if (oldOrganizationUnit != null) {
            if (!oldOrganizationUnit.equals(organizationUnitImpl)) {
                oldOrganizationUnit.getPositions().remove(positionImpl);
            }
        }

        positionImpl.belongsToOrganization(organizationUnitImpl);
        organizationUnitImpl.getPositions().add(positionImpl);

        return this;
    }

    @Override
    public IdentityBuilder organizationUnitDoesNotOfferPosition(UUID organizationUnitId, UUID positionId) {

        PositionImpl positionImpl = this.identityService.getPosition(positionId);
        OrganizationUnitImpl organizationUnitImpl = this.identityService.getOrganizationUnit(organizationUnitId);

        positionImpl.belongsToOrganization(null);
        organizationUnitImpl.getPositions().remove(positionImpl);

        return this;
    }

    // -------- Position Builder Methods --------------

    @Override
    public PositionImpl createPosition(String positionName) {

        PositionImpl positionImpl = new PositionImpl(positionName);

        identityService.getPositionImpls().put(positionImpl.getID(), positionImpl);

        return positionImpl;
    }

    @Override
    public IdentityBuilder positionReportsToSuperior(UUID positionId, UUID superiorPositionId)
    throws DalmatinaException {

        PositionImpl positionImpl = this.identityService.getPosition(positionId);
        PositionImpl superiorPositionImpl =
            this.identityService.getPosition(superiorPositionId);

        if (positionImpl.equals(superiorPositionImpl)) {
            throw new DalmatinaException("The Position '" + positionImpl.getID()
                + "' cannot be the superior of yourself.");
        }

        positionImpl.setSuperiorPosition(superiorPositionImpl);

        superiorPositionImpl.getSubordinatePositions().add(positionImpl);

        return this;
    }

    @Override
    public IdentityBuilder deletePosition(UUID positionId)
    throws DalmatinaException {

        PositionImpl positionImpl = this.identityService.getPosition(positionId);

        identityService.getPositionImpls().remove(positionImpl.getID());

        for (PositionImpl subordinatePosition : positionImpl.getSubordinatePositions()) {
            subordinatePosition.setSuperiorPosition(null);
        }

        return this;
    }

    // -------- Role Builder Methods ------------------

    @Override
    public RoleImpl createRole(String roleName) {

        RoleImpl roleImpl = new RoleImpl(roleName);

        identityService.getRoleImpls().put(roleImpl.getID(), roleImpl);

        return roleImpl;
    }

    @Override
    public IdentityBuilder deleteRole(UUID roleId)
    throws DalmatinaException {

        RoleImpl roleImpl = this.identityService.getRole(roleId);

        for (ParticipantImpl participantImpl : roleImpl.getParticipants()) {
            participantImpl.getMyRoles().remove(roleImpl);
        }

        identityService.getRoleImpls().remove(roleImpl.getID());
        return this;
    }

    @Override
    public IdentityBuilder subRoleOf(UUID subRole, UUID superRole) {

        return null; // TODO @Scherapo... 
    }
}
