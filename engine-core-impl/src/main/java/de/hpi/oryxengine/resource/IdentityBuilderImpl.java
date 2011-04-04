package de.hpi.oryxengine.resource;

import java.util.UUID;

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
    public IdentityBuilderImpl(IdentityServiceImpl identityServiceImpl) {

        identityService = identityServiceImpl;
    }

    // -------- Participant Builder Methods -----------

    @Override
    public Participant createParticipant(String participantName) {

        ParticipantImpl participant = new ParticipantImpl(participantName);

        identityService.getParticipantImpls().put(participant.getID(), participant);

        return participant;
    }

    @Override
    public IdentityBuilder deleteParticipant(UUID participantId)
    throws DalmatinaException {

        ParticipantImpl participantImpl = ParticipantImpl.asParticipantImpl(participantId);

        for (PositionImpl positionImpl : participantImpl.getMyPositionImpls()) {
            positionImpl.setPositionHolder(null);
        }

        identityService.getParticipantImpls().remove(participantImpl.getID());
        return this;
    }

    @Override
    public IdentityBuilder participantOccupiesPosition(UUID participantId, UUID positionId) {

        PositionImpl positionImpl = PositionImpl.asPositionImpl(positionId);
        ParticipantImpl participantImpl = ParticipantImpl.asParticipantImpl(participantId);

        ParticipantImpl oldParticiant = (ParticipantImpl) positionImpl.getPositionHolder();
        if (oldParticiant != null) {
            if (!oldParticiant.equals(participantImpl)) {
                oldParticiant.getMyPositionImpls().remove(positionImpl);
            }
        }

        positionImpl.setPositionHolder(participantImpl);
        participantImpl.getMyPositionImpls().add(positionImpl);

        return this;
    }

    @Override
    public IdentityBuilder participantDoesNotOccupyPosition(UUID participantId, UUID positionId) {

        PositionImpl positionImpl = PositionImpl.asPositionImpl(positionId);
        ParticipantImpl participantImpl = ParticipantImpl.asParticipantImpl(participantId);

        positionImpl.belongstoOrganization(null);
        participantImpl.getMyPositionImpls().remove(positionImpl);
        return this;
    }

    @Override
    public IdentityBuilder participantHasCapability(UUID participantId, Capability capability) {

        return null;
    }

    @Override
    public IdentityBuilder participantBelongsToRole(UUID participantId, UUID roleId) {

        RoleImpl roleImpl = RoleImpl.asRoleImpl(roleId);
        ParticipantImpl participantImpl = ParticipantImpl.asParticipantImpl(participantId);

        roleImpl.getParticipantImpls().add(participantImpl);
        participantImpl.getMyRolesImpl().add(roleImpl);

        return this;
    }

    @Override
    public IdentityBuilder participantDoesNotBelongToRole(UUID participantId, UUID roleId) {

        RoleImpl roleImpl = RoleImpl.asRoleImpl(roleId);
        ParticipantImpl participantImpl = ParticipantImpl.asParticipantImpl(participantId);

        roleImpl.getParticipantImpls().remove(participantImpl);
        participantImpl.getMyRolesImpl().remove(roleImpl);
        return this;
    }

    // -------- Capability Builder Methods ------------

    @Override
    public Capability createCapability(String capabilityId) {

        // hier könnte man das FlyWeight-Pattern verwenden ...
        // Capability capability = new CapabilityImpl(capabilityId);

        // return capability;
        return null;
    }

    // -------- OrganizationUnit Builder Methods ------

    @Override
    public OrganizationUnit createOrganizationUnit(String organizationUnitName) {

        OrganizationUnitImpl organizationUnitImpl = new OrganizationUnitImpl(organizationUnitName);

        identityService.getOrganizationUnitImpls().put(organizationUnitImpl.getID(), organizationUnitImpl);

        return organizationUnitImpl;
    }

    @Override
    public IdentityBuilder deleteOrganizationUnit(UUID organizationUnitId) {

        OrganizationUnitImpl organizationUnitImpl = OrganizationUnitImpl.asOrganizationUnitImpl(organizationUnitId);

        for (OrganizationUnitImpl childOrganizationUnitImpl : organizationUnitImpl.getChildOrganisationUnitImpls()) {
            childOrganizationUnitImpl.setSuperOrganizationUnit(null);
        }

        for (PositionImpl positionImpl : organizationUnitImpl.getPositionImpls()) {
            positionImpl.belongstoOrganization(null);
        }

        identityService.getOrganizationUnitImpls().remove(organizationUnitImpl.getID());
        return this;
    }

    @Override
    public IdentityBuilder subOrganizationUnitOf(UUID subOrganizationUnitId,
                                                 UUID superOrganizationUnitId)
    throws DalmatinaException {

        OrganizationUnitImpl organizationUnitImpl = OrganizationUnitImpl.asOrganizationUnitImpl(subOrganizationUnitId);
        OrganizationUnitImpl superOrganizationUnitImpl = OrganizationUnitImpl
        .asOrganizationUnitImpl(superOrganizationUnitId);

        if (organizationUnitImpl.equals(superOrganizationUnitImpl)) {
            throw new DalmatinaException("The OrganizationUnit cannot be the superior of yourself.");
        }

        organizationUnitImpl.setSuperOrganizationUnit(superOrganizationUnitImpl);

        superOrganizationUnitImpl.getChildOrganisationUnitImpls().add(organizationUnitImpl);

        return this;
    }

    @Override
    public IdentityBuilder organizationUnitOffersPosition(UUID organizationUnitId, UUID positionId) {

        PositionImpl positionImpl = PositionImpl.asPositionImpl(positionId);
        OrganizationUnitImpl organizationUnitImpl = OrganizationUnitImpl.asOrganizationUnitImpl(organizationUnitId);

        OrganizationUnitImpl oldOrganizationUnit = (OrganizationUnitImpl) positionImpl.belongstoOrganization();
        if (oldOrganizationUnit != null) {
            if (!oldOrganizationUnit.equals(organizationUnitImpl)) {
                oldOrganizationUnit.getPositionImpls().remove(positionImpl);
            }
        }

        positionImpl.belongstoOrganization(organizationUnitImpl);
        organizationUnitImpl.getPositionImpls().add(positionImpl);

        return this;
    }

    @Override
    public IdentityBuilder organizationUnitDoesNotOfferPosition(UUID organizationUnitId, UUID positionId) {

        PositionImpl positionImpl = PositionImpl.asPositionImpl(positionId);
        OrganizationUnitImpl organizationUnitImpl = OrganizationUnitImpl.asOrganizationUnitImpl(organizationUnitId);

        positionImpl.belongstoOrganization(null);
        organizationUnitImpl.getPositionImpls().remove(positionImpl);

        return this;
    }

    // -------- Position Builder Methods --------------

    @Override
    public Position createPosition(String positionName) {

        PositionImpl positionImpl = new PositionImpl(positionName);

        identityService.getPositionImpls().put(positionImpl.getID(), positionImpl);

        return positionImpl;
    }

    @Override
    public IdentityBuilder positionReportsToSuperior(UUID positionId, UUID superiorPositionId)
    throws DalmatinaException {

        PositionImpl positionImpl = PositionImpl.asPositionImpl(positionId);
        PositionImpl superiorPositionImpl = PositionImpl.asPositionImpl(superiorPositionId);

        if (positionImpl.equals(superiorPositionImpl)) {
            throw new DalmatinaException("The Position '" + positionImpl.getID()
                + "' cannot be the superior of yourself.");
        }

        positionImpl.setSuperiorPosition(superiorPositionImpl);

        superiorPositionImpl.getSubordinatePositionImpls().add(positionImpl);

        return this;
    }

    @Override
    public IdentityBuilder deletePosition(UUID positionId)
    throws DalmatinaException {

        PositionImpl positionImpl = PositionImpl.asPositionImpl(positionId);

        identityService.getPositionImpls().remove(positionImpl.getID());

        for (PositionImpl subordinatePosition : positionImpl.getSubordinatePositionImpls()) {
            subordinatePosition.setSuperiorPosition(null);
        }

        return this;
    }

    // -------- Role Builder Methods ------------------

    @Override
    public Role createRole(String roleName) {

        RoleImpl roleImpl = new RoleImpl(roleName);

        identityService.getRoleImpls().put(roleImpl.getID(), roleImpl);

        return roleImpl;
    }

    @Override
    public IdentityBuilder deleteRole(UUID roleId)
    throws DalmatinaException {

        RoleImpl roleImpl = RoleImpl.asRoleImpl(roleId);

        for (ParticipantImpl participantImpl : roleImpl.getParticipantImpls()) {
            participantImpl.getMyRolesImpl().remove(roleImpl);
        }

        identityService.getRoleImpls().remove(roleImpl.getID());
        return this;
    }

    @Override
    public IdentityBuilder subRoleOf(UUID subRole, UUID superRole) {

        return null;
    }
}
