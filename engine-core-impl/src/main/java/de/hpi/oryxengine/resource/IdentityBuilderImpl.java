package de.hpi.oryxengine.resource;

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

//        for (ParticipantImpl existingParticipant : identityService.getParticipantImpls()) {
//            if (existingParticipant.equals(participant)) {
//                return existingParticipant;
//            }
//        }

        identityService.getParticipantImpls().add(participant);

        return participant;
    }

    @Override
    public IdentityBuilder deleteParticipant(Participant participant)
    throws DalmatinaException {

        ParticipantImpl participantImpl = ParticipantImpl.asParticipantImpl(participant);

        for (PositionImpl positionImpl : participantImpl.getMyPositionImpls()) {
            positionImpl.setPositionHolder(null);
        }

        identityService.getParticipantImpls().remove(participantImpl);
        return this;
    }

    @Override
    public IdentityBuilder participantOccupiesPosition(Participant participant, Position position) {

        PositionImpl positionImpl = PositionImpl.asPositionImpl(position);
        ParticipantImpl participantImpl = ParticipantImpl.asParticipantImpl(participant);

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
    public IdentityBuilder participantDoesNotOccupyPosition(Participant participant, Position position) {

        PositionImpl positionImpl = PositionImpl.asPositionImpl(position);
        ParticipantImpl participantImpl = ParticipantImpl.asParticipantImpl(participant);

        positionImpl.belongstoOrganization(null);
        participantImpl.getMyPositionImpls().remove(positionImpl);
        return this;
    }

    @Override
    public IdentityBuilder participantHasCapability(Participant participant, Capability capability) {

        return null;
    }

    @Override
    public IdentityBuilder participantBelongsToRole(Participant participant, Role role) {

        RoleImpl roleImpl = RoleImpl.asRoleImpl(role);
        ParticipantImpl participantImpl = ParticipantImpl.asParticipantImpl(participant);

        roleImpl.getParticipantImpls().add(participantImpl);
        participantImpl.getMyRolesImpl().add(roleImpl);

        return this;
    }

    @Override
    public IdentityBuilder participantDoesNotBelongToRole(Participant participant, Role role) {

        RoleImpl roleImpl = RoleImpl.asRoleImpl(role);
        ParticipantImpl participantImpl = ParticipantImpl.asParticipantImpl(participant);

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

        OrganizationUnitImpl organizationUnit = new OrganizationUnitImpl(organizationUnitName);

//        for (OrganizationUnitImpl existingOrganizationUnit : identityService.getOrganizationUnitImpls()) {
//            if (existingOrganizationUnit.equals(organizationUnit)) {
//                return existingOrganizationUnit;
//            }
//        }

        identityService.getOrganizationUnitImpls().add(organizationUnit);

        return organizationUnit;
    }

    @Override
    public IdentityBuilder deleteOrganizationUnit(OrganizationUnit organizationUnit) {

        OrganizationUnitImpl organizationUnitImpl = OrganizationUnitImpl.asOrganizationUnitImpl(organizationUnit);

        for (OrganizationUnitImpl childOrganizationUnitImpl : organizationUnitImpl.getChildOrganisationUnitImpls()) {
            childOrganizationUnitImpl.setSuperOrganizationUnit(null);
        }

        for (PositionImpl positionImpl : organizationUnitImpl.getPositionImpls()) {
            positionImpl.belongstoOrganization(null);
        }

        identityService.getOrganizationUnitImpls().remove(organizationUnitImpl);
        return this;
    }

    @Override
    public IdentityBuilder subOrganizationUnitOf(OrganizationUnit subOrganizationUnit,
                                                 OrganizationUnit superOrganizationUnit)
    throws DalmatinaException {

        OrganizationUnitImpl organizationUnitImpl = OrganizationUnitImpl.asOrganizationUnitImpl(subOrganizationUnit);
        OrganizationUnitImpl superOrganizationUnitImpl = OrganizationUnitImpl
        .asOrganizationUnitImpl(superOrganizationUnit);

        if (organizationUnitImpl.equals(superOrganizationUnitImpl)) {
            throw new DalmatinaException("The OrganizationUnit cannot be the superior of yourself.");
        }

        organizationUnitImpl.setSuperOrganizationUnit(superOrganizationUnitImpl);

        superOrganizationUnitImpl.getChildOrganisationUnitImpls().add(organizationUnitImpl);

        return this;
    }

    @Override
    public IdentityBuilder organizationUnitOffersPosition(OrganizationUnit organizationUnit, Position position) {

        PositionImpl positionImpl = PositionImpl.asPositionImpl(position);
        OrganizationUnitImpl organizationUnitImpl = OrganizationUnitImpl.asOrganizationUnitImpl(organizationUnit);

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
    public IdentityBuilder organizationUnitDoesNotOfferPosition(OrganizationUnit organizationUnit, Position position) {

        PositionImpl positionImpl = PositionImpl.asPositionImpl(position);
        OrganizationUnitImpl organizationUnitImpl = OrganizationUnitImpl.asOrganizationUnitImpl(organizationUnit);

        positionImpl.belongstoOrganization(null);
        organizationUnitImpl.getPositionImpls().remove(positionImpl);

        return this;
    }

    // -------- Position Builder Methods --------------

    @Override
    public Position createPosition(String positionName) {

        PositionImpl position = new PositionImpl(positionName);
//
//        for (PositionImpl existingPosition : identityService.getPositionImpls()) {
//            if (existingPosition.equals(position)) {
//                return existingPosition;
//            }
//        }

        identityService.getPositionImpls().add(position);

        return position;
    }

    @Override
    public IdentityBuilder positionReportsToSuperior(Position position, Position superiorPosition)
    throws DalmatinaException {

        PositionImpl positionImpl = PositionImpl.asPositionImpl(position);
        PositionImpl superiorPositionImpl = PositionImpl.asPositionImpl(superiorPosition);

        if (positionImpl.equals(superiorPositionImpl)) {
            throw new DalmatinaException("The Position '" + positionImpl.getID()
                + "' cannot be the superior of yourself.");
        }

        positionImpl.setSuperiorPosition(superiorPosition);

        superiorPositionImpl.getSubordinatePositionImpls().add(positionImpl);

        return this;
    }

    @Override
    public IdentityBuilder deletePosition(Position position)
    throws DalmatinaException {

        PositionImpl positionImpl = PositionImpl.asPositionImpl(position);

        identityService.getPositionImpls().remove(position);

        for (PositionImpl subordinatePosition : positionImpl.getSubordinatePositionImpls()) {
            subordinatePosition.setSuperiorPosition(null);
        }

        return this;
    }

    // -------- Role Builder Methods ------------------

    @Override
    public Role createRole(String roleName) {

        RoleImpl role = new RoleImpl(roleName);

//        for (RoleImpl existingRoles : identityService.getRoleImpls()) {
//            if (existingRoles.equals(role)) {
//                return existingRoles;
//            }
//        }

        identityService.getRoleImpls().add(role);

        return role;
    }

    @Override
    public IdentityBuilder deleteRole(Role role)
    throws DalmatinaException {

        RoleImpl roleImpl = RoleImpl.asRoleImpl(role);

        for (ParticipantImpl participantImpl : roleImpl.getParticipantImpls()) {
            participantImpl.getMyRolesImpl().remove(roleImpl);
        }

        identityService.getRoleImpls().remove(roleImpl);
        return this;
    }

    @Override
    public IdentityBuilder subRoleOf(Role subRole, Role superRole) {

        return null;
    }
}
