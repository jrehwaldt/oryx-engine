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
    public Participant createParticipant(String participantId) {

        ParticipantImpl participant = new ParticipantImpl(participantId);

        for (ParticipantImpl existingParticipant : identityService.getParticipantImpls()) {
            if (existingParticipant.equals(participant)) {
                return existingParticipant;
            }
        }

        identityService.getParticipantImpls().add(participant);

        return participant;
    }

    @Override
    public IdentityBuilder deleteParticipant(Participant participant)
    throws DalmatinaException {

        ParticipantImpl participantImpl = extractParticipantImplFrom(participant);

        for (PositionImpl positionImpl : participantImpl.getMyPositionImpls()) {
            positionImpl.setPositionHolder(null);
        }

        identityService.getParticipantImpls().remove(participantImpl);
        return this;
    }

    @Override
    public IdentityBuilder participantOccupiesPosition(Participant participant, Position position)
    throws DalmatinaException {

        PositionImpl positionImpl = extractPositionImplFrom(position);
        ParticipantImpl participantImpl = extractParticipantImplFrom(participant);

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
    public IdentityBuilder participantDoesNotOccupyPosition(Participant participant, Position position)
    throws DalmatinaException {

        PositionImpl positionImpl = extractPositionImplFrom(position);
        ParticipantImpl participantImpl = extractParticipantImplFrom(participant);

        positionImpl.belongstoOrganization(null);
        participantImpl.getMyPositionImpls().remove(positionImpl);
        return this;
    }

    @Override
    public IdentityBuilder participantHasCapability(Participant participant, Capability capability) {

        return null;
    }

    @Override
    public IdentityBuilder participantBelongsToRole(Participant participant, Role role)
    throws DalmatinaException {

        RoleImpl roleImpl = extractRoleImplFrom(role);
        ParticipantImpl participantImpl = extractParticipantImplFrom(participant);

        roleImpl.getParticipantImpls().add(participantImpl);
        participantImpl.getMyRolesImpl().add(roleImpl);

        return this;
    }

    @Override
    public IdentityBuilder participantDoesNotBelongToRole(Participant participant, Role role)
    throws DalmatinaException {

        RoleImpl roleImpl = extractRoleImplFrom(role);
        ParticipantImpl participantImpl = extractParticipantImplFrom(participant);

        roleImpl.getParticipantImpls().remove(participantImpl);
        participantImpl.getMyRolesImpl().remove(roleImpl);
        return this;
    }

    /**
     * Translates a Participant into a corresponding ParticipantImpl object.
     * 
     * Furthermore some constrains are checked.
     * 
     * @param participant
     *            - a Participant object
     * @return participantImpl - the casted Participant object
     * @throws DalmatinaException
     *             the oryx engine exception
     */
    private ParticipantImpl extractParticipantImplFrom(Participant participant)
    throws DalmatinaException {

        if (participant == null) {
            throw new DalmatinaException("The Participant parameter is null.");
        }

        ParticipantImpl participantImpl = (ParticipantImpl) participant;
        if (!identityService.getParticipantImpls().contains(participantImpl)) {
            throw new DalmatinaException("There exists no Participant with the id " + participant.getId() + ".");
        }
        return participantImpl;
    }

    // -------- Capability Builder Methods ------------

    @Override
    public Capability createCapability(String capabilityId) {

        // hier könnte man das FlyWeight-Pattern verwenden ...
        Capability capability = new CapabilityImpl(capabilityId);

        return capability;
    }

    // -------- OrganizationUnit Builder Methods ------

    @Override
    public OrganizationUnit createOrganizationUnit(String organizationUnitId) {

        OrganizationUnitImpl organizationUnit = new OrganizationUnitImpl(organizationUnitId);

        for (OrganizationUnitImpl existingOrganizationUnit : identityService.getOrganizationUnitImpls()) {
            if (existingOrganizationUnit.equals(organizationUnit)) {
                return existingOrganizationUnit;
            }
        }

        identityService.getOrganizationUnitImpls().add(organizationUnit);

        return organizationUnit;
    }

    @Override
    public IdentityBuilder deleteOrganizationUnit(OrganizationUnit organizationUnit)
    throws DalmatinaException {

        OrganizationUnitImpl organizationUnitImpl = extractOrganizationUnitImplFrom(organizationUnit);

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

        OrganizationUnitImpl organizationUnitImpl = extractOrganizationUnitImplFrom(subOrganizationUnit);
        OrganizationUnitImpl superOrganizationUnitImpl = extractOrganizationUnitImplFrom(superOrganizationUnit);

        if (organizationUnitImpl.equals(superOrganizationUnitImpl)) {
            throw new DalmatinaException("The OrganizationUnit cannot be the superior of yourself.");
        }

        organizationUnitImpl.setSuperOrganizationUnit(superOrganizationUnitImpl);

        superOrganizationUnitImpl.getChildOrganisationUnitImpls().add(organizationUnitImpl);

        return this;
    }

    @Override
    public IdentityBuilder organizationUnitOffersPosition(OrganizationUnit organizationUnit, Position position)
    throws DalmatinaException {

        PositionImpl positionImpl = extractPositionImplFrom(position);
        OrganizationUnitImpl organizationUnitImpl = extractOrganizationUnitImplFrom(organizationUnit);

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
    public IdentityBuilder organizationUnitDoesNotOfferPosition(OrganizationUnit organizationUnit, Position position)
    throws DalmatinaException {

        PositionImpl positionImpl = extractPositionImplFrom(position);
        OrganizationUnitImpl organizationUnitImpl = extractOrganizationUnitImplFrom(organizationUnit);

        positionImpl.belongstoOrganization(null);
        organizationUnitImpl.getPositionImpls().remove(positionImpl);

        return this;
    }

    /**
     * Translates a OrganizationUnit into a corresponding OrganizationUnitImpl object.
     * 
     * Furthermore some constrains are checked.
     * 
     * @param organizationUnit
     *            - a OrganizationUnit object
     * @return organizationUnitImpl - the casted OrganizationUnit object
     * @throws DalmatinaException
     *             the oryx engine exception
     */
    private OrganizationUnitImpl extractOrganizationUnitImplFrom(OrganizationUnit organizationUnit)
    throws DalmatinaException {

        if (organizationUnit == null) {
            throw new DalmatinaException("The OrganizationUnit parameter is null.");
        }

        OrganizationUnitImpl organizationUnitImpl = (OrganizationUnitImpl) organizationUnit;
        if (!identityService.getOrganizationUnitImpls().contains(organizationUnitImpl)) {
            throw new DalmatinaException("There exists no OrganizationUnit with the id " + organizationUnit.getId()
                + ".");
        }
        return organizationUnitImpl;
    }

    // -------- Position Builder Methods --------------

    @Override
    public Position createPosition(String positionId) {

        PositionImpl position = new PositionImpl(positionId);

        for (PositionImpl existingPosition : identityService.getPositionImpls()) {
            if (existingPosition.equals(position)) {
                return existingPosition;
            }
        }

        identityService.getPositionImpls().add(position);

        return position;
    }

    @Override
    public IdentityBuilder positionReportsToSuperior(Position position, Position superiorPosition)
    throws DalmatinaException {

        PositionImpl positionImpl = extractPositionImplFrom(position);
        PositionImpl superiorPositionImpl = extractPositionImplFrom(superiorPosition);

        if (positionImpl.equals(superiorPositionImpl)) {
            throw new DalmatinaException("The Position '" + positionImpl.getId()
                + "' cannot be the superior of yourself.");
        }

        positionImpl.setSuperiorPosition(superiorPosition);

        superiorPositionImpl.getSubordinatePositionImpls().add(positionImpl);

        return this;
    }

    @Override
    public IdentityBuilder deletePosition(Position position)
    throws DalmatinaException {

        PositionImpl positionImpl = extractPositionImplFrom(position);

        identityService.getPositionImpls().remove(position);

        for (PositionImpl subordinatePosition : positionImpl.getSubordinatePositionImpls()) {
            subordinatePosition.setSuperiorPosition(null);
        }

        return this;
    }

    /**
     * Translates a Position into a corresponding PositionImpl object.
     * 
     * Furthermore some constrains are checked.
     * 
     * @param position
     *            - a Position object
     * @return positionImpl - the casted Position object
     * @throws DalmatinaException
     *             the oryx engine exception
     */
    private PositionImpl extractPositionImplFrom(Position position)
    throws DalmatinaException {

        if (position == null) {
            throw new DalmatinaException("The Position parameter is null.");
        }

        PositionImpl positionImpl = (PositionImpl) position;
        if (!identityService.getPositionImpls().contains(positionImpl)) {
            throw new DalmatinaException("There exists no Position with the id " + position.getId() + ".");
        }
        return positionImpl;
    }

    // -------- Role Builder Methods ------------------

    @Override
    public Role createRole(String roleId) {

        RoleImpl role = new RoleImpl(roleId);

        for (RoleImpl existingRoles : identityService.getRoleImpls()) {
            if (existingRoles.equals(role)) {
                return existingRoles;
            }
        }

        identityService.getRoleImpls().add(role);

        return role;
    }

    @Override
    public IdentityBuilder deleteRole(Role role)
    throws DalmatinaException {

        RoleImpl roleImpl = extractRoleImplFrom(role);

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

    /**
     * Translates a Role into a corresponding RoleImpl object.
     * 
     * Furthermore some constrains are checked.
     * 
     * @param role
     *            - a Role object
     * @return roleImpl - the casted Role object
     * @throws DalmatinaException
     *             the oryx engine exception
     */
    private RoleImpl extractRoleImplFrom(Role role)
    throws DalmatinaException {

        if (role == null) {
            throw new DalmatinaException("The Role parameter is null.");
        }
        RoleImpl roleImpl = (RoleImpl) role;
        if (!identityService.getRoleImpls().contains(roleImpl)) {
            throw new DalmatinaException("There exists no Role with the id " + role.getId() + ".");
        }
        return roleImpl;
    }
}
