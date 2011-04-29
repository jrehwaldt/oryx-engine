package de.hpi.oryxengine.resource;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.exception.DalmatinaException;

/**
 * Implementation of {@link IdentityBuilder} Interface.
 */
public class IdentityBuilderImpl implements IdentityBuilder {

    /** The identity service. */
    private IdentityServiceImpl identityService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
    public AbstractParticipant createParticipant(String participantName) {

        Participant participant = new Participant(participantName);

        identityService.getParticipantImpls().put(participant.getID(), participant);

        return participant;
    }

    @Override
    public IdentityBuilder deleteParticipant(UUID participantId)
    throws DalmatinaException {

        Participant participantImpl = Participant.asParticipantImpl(participantId);

        for (Position positionImpl : participantImpl.getMyPositions()) {
            positionImpl.setPositionHolder(null);
        }

        identityService.getParticipantImpls().remove(participantImpl.getID());
        return this;
    }

    @Override
    public IdentityBuilder participantOccupiesPosition(UUID participantId, UUID positionId) {

        Position positionImpl = Position.asPositionImpl(positionId);
        Participant participantImpl = Participant.asParticipantImpl(participantId);

        Participant oldParticiant = (Participant) positionImpl.getPositionHolder();
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
    public IdentityBuilder participantDoesNotOccupyPosition(UUID participantId, UUID positionId) {

        Position positionImpl = Position.asPositionImpl(positionId);
        Participant participantImpl = Participant.asParticipantImpl(participantId);

        positionImpl.belongstoOrganization(null);
        participantImpl.getMyPositions().remove(positionImpl);
        return this;
    }

    @Override
    public IdentityBuilder participantHasCapability(UUID participantId, AbstractCapability capability) {

        return null; // TODO
    }

    @Override
    public IdentityBuilder participantBelongsToRole(UUID participantId, UUID roleId) {

        Role roleImpl = Role.asRoleImpl(roleId);
        Participant participantImpl = Participant.asParticipantImpl(participantId);

        roleImpl.getParticipants().add(participantImpl);
        participantImpl.getMyRoles().add(roleImpl);

        return this;
    }

    @Override
    public IdentityBuilder participantDoesNotBelongToRole(UUID participantId, UUID roleId) {

        Role roleImpl = Role.asRoleImpl(roleId);
        Participant participantImpl = Participant.asParticipantImpl(participantId);

        roleImpl.getParticipants().remove(participantImpl);
        participantImpl.getMyRoles().remove(roleImpl);
        return this;
    }

    // -------- Capability Builder Methods ------------

    @Override
    public AbstractCapability createCapability(String capabilityId) {

        // hier könnte man das FlyWeight-Pattern verwenden ...
        // Capability capability = new CapabilityImpl(capabilityId);

        // return capability;
        return null; // TODO
    }

    // -------- OrganizationUnit Builder Methods ------

    @Override
    public OrganizationUnit createOrganizationUnit(String organizationUnitName) {

        OrganizationUnit organizationUnitImpl = new OrganizationUnit(organizationUnitName);

        identityService.getOrganizationUnitImpls().put(organizationUnitImpl.getID(), organizationUnitImpl);

        return organizationUnitImpl;
    }

    @Override
    public IdentityBuilder deleteOrganizationUnit(UUID organizationUnitId) {

        OrganizationUnit organizationUnitImpl = OrganizationUnit.asOrganizationUnitImpl(organizationUnitId);

        for (OrganizationUnit childOrganizationUnitImpl : organizationUnitImpl.getChildOrganisationUnits()) {
            childOrganizationUnitImpl.setSuperOrganizationUnit(null);
        }

        for (Position positionImpl : organizationUnitImpl.getPositions()) {
            positionImpl.belongstoOrganization(null);
        }

        identityService.getOrganizationUnitImpls().remove(organizationUnitImpl.getID());
        return this;
    }

    @Override
    public IdentityBuilder subOrganizationUnitOf(UUID subOrganizationUnitId, UUID superOrganizationUnitId)
    throws DalmatinaException {

        OrganizationUnit organizationUnitImpl = OrganizationUnit.asOrganizationUnitImpl(subOrganizationUnitId);
        OrganizationUnit superOrganizationUnitImpl = OrganizationUnit.asOrganizationUnitImpl(superOrganizationUnitId);

        if (organizationUnitImpl.equals(superOrganizationUnitImpl)) {
            throw new DalmatinaException("The OrganizationUnit cannot be the superior of yourself.");
        }

        organizationUnitImpl.setSuperOrganizationUnit(superOrganizationUnitImpl);

        superOrganizationUnitImpl.getChildOrganisationUnits().add(organizationUnitImpl);

        return this;
    }

    @Override
    public IdentityBuilder organizationUnitOffersPosition(UUID organizationUnitId, UUID positionId) {

        Position positionImpl = Position.asPositionImpl(positionId);
        OrganizationUnit organizationUnitImpl = OrganizationUnit.asOrganizationUnitImpl(organizationUnitId);

        OrganizationUnit oldOrganizationUnit = (OrganizationUnit) positionImpl.belongstoOrganization();
        if (oldOrganizationUnit != null) {
            if (!oldOrganizationUnit.equals(organizationUnitImpl)) {
                oldOrganizationUnit.getPositions().remove(positionImpl);
            }
        }

        positionImpl.belongstoOrganization(organizationUnitImpl);
        organizationUnitImpl.getPositions().add(positionImpl);

        return this;
    }

    @Override
    public IdentityBuilder organizationUnitDoesNotOfferPosition(UUID organizationUnitId, UUID positionId) {

        Position positionImpl = Position.asPositionImpl(positionId);
        OrganizationUnit organizationUnitImpl = OrganizationUnit.asOrganizationUnitImpl(organizationUnitId);

        positionImpl.belongstoOrganization(null);
        organizationUnitImpl.getPositions().remove(positionImpl);

        return this;
    }

    // -------- Position Builder Methods --------------

    @Override
    public Position createPosition(String positionName) {

        Position positionImpl = new Position(positionName);

        identityService.getPositionImpls().put(positionImpl.getID(), positionImpl);

        return positionImpl;
    }

    @Override
    public IdentityBuilder positionReportsToSuperior(UUID positionId, UUID superiorPositionId)
    throws DalmatinaException {

        Position positionImpl = Position.asPositionImpl(positionId);
        Position superiorPositionImpl = Position.asPositionImpl(superiorPositionId);

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

        Position positionImpl = Position.asPositionImpl(positionId);

        identityService.getPositionImpls().remove(positionImpl.getID());

        for (Position subordinatePosition : positionImpl.getSubordinatePositions()) {
            subordinatePosition.setSuperiorPosition(null);
        }

        return this;
    }

    // -------- Role Builder Methods ------------------

    @Override
    public Role createRole(String roleName) {

        Role roleImpl = new Role(roleName);

        identityService.getRoleImpls().put(roleImpl.getID(), roleImpl);

        return roleImpl;
    }

    @Override
    public IdentityBuilder deleteRole(UUID roleId)
    throws DalmatinaException {

        Role roleImpl = Role.asRoleImpl(roleId);

        for (Participant participantImpl : roleImpl.getParticipants()) {
            participantImpl.getMyRoles().remove(roleImpl);
        }

        identityService.getRoleImpls().remove(roleImpl.getID());
        return this;
    }

    @Override
    public IdentityBuilder subRoleOf(UUID subRole, UUID superRole) {

        return null; // TODO @Scherapo - bin mir nicht sicher ob wir überhaupt Oberrollen brauchen; sollte nochmal
                     // diskutiert werden
    }
}
