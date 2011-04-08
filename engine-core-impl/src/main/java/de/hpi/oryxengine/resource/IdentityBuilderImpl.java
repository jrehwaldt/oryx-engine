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
    public Participant createParticipant(String participantName) {

        Participant participant = new Participant(participantName);

        identityService.getParticipantImpls().put(participant.getID(), participant);

        return participant;
    }

    @Override
    public IdentityBuilder deleteParticipant(UUID participantId)
    throws DalmatinaException {

        Participant participantImpl = this.identityService.getParticipant(participantId);

        for (Position positionImpl: participantImpl.getMyPositions()) {
            positionImpl.setPositionHolder(null);
        }

        identityService.getParticipantImpls().remove(participantImpl.getID());
        return this;
    }

    @Override
    public IdentityBuilder participantOccupiesPosition(UUID participantId,
                                                       UUID positionId) {

        Position positionImpl = this.identityService.getPosition(positionId);
        Participant participantImpl = this.identityService.getParticipant(participantId);

        Participant oldParticiant = positionImpl.getPositionHolder();
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

        Position positionImpl = this.identityService.getPosition(positionId);
        Participant participantImpl = this.identityService.getParticipant(participantId);

        positionImpl.belongsToOrganization(null);
        participantImpl.getMyPositions().remove(positionImpl);
        return this;
    }
    
    @Override
    public IdentityBuilder participantHasCapability(UUID participantId,
                                                    Capability capability) {
        
        return null; // TODO
    }

    @Override
    public IdentityBuilder participantBelongsToRole(UUID participantId,
                                                    UUID roleId) {

        Role roleImpl = this.identityService.getRole(roleId);
        Participant participantImpl = this.identityService.getParticipant(participantId);

        roleImpl.getParticipants().add(participantImpl);
        participantImpl.getMyRoles().add(roleImpl);

        return this;
    }

    @Override
    public IdentityBuilder participantDoesNotBelongToRole(UUID participantId,
                                                          UUID roleId) {

        Role roleImpl = this.identityService.getRole(roleId);
        Participant participantImpl = this.identityService.getParticipant(participantId);

        roleImpl.getParticipants().remove(participantImpl);
        participantImpl.getMyRoles().remove(roleImpl);
        return this;
    }

    // -------- Capability Builder Methods ------------

    @Override
    public Capability createCapability(String capabilityId) {

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

        OrganizationUnit organizationUnitImpl = this.identityService.getOrganizationUnit(organizationUnitId);

        for (OrganizationUnit childOrganizationUnitImpl: organizationUnitImpl.getChildOrganisationUnits()) {
            childOrganizationUnitImpl.setSuperOrganizationUnit(null);
        }

        for (Position positionImpl : organizationUnitImpl.getPositions()) {
            positionImpl.belongsToOrganization(null);
        }

        identityService.getOrganizationUnitImpls().remove(organizationUnitImpl.getID());
        return this;
    }

    @Override
    public IdentityBuilder subOrganizationUnitOf(UUID subOrganizationUnitId,
                                                 UUID superOrganizationUnitId)
    throws DalmatinaException {

        OrganizationUnit organizationUnitImpl = this.identityService.getOrganizationUnit(subOrganizationUnitId);
        OrganizationUnit superOrganizationUnitImpl =
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

        Position positionImpl = this.identityService.getPosition(positionId);
        OrganizationUnit organizationUnitImpl = this.identityService.getOrganizationUnit(organizationUnitId);

        OrganizationUnit oldOrganizationUnit = (OrganizationUnit) positionImpl.belongsToOrganization();
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

        Position positionImpl = this.identityService.getPosition(positionId);
        OrganizationUnit organizationUnitImpl = this.identityService.getOrganizationUnit(organizationUnitId);

        positionImpl.belongsToOrganization(null);
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

        Position positionImpl = this.identityService.getPosition(positionId);
        Position superiorPositionImpl =
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

        Position positionImpl = this.identityService.getPosition(positionId);

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

        Role roleImpl = this.identityService.getRole(roleId);

        for (Participant participantImpl : roleImpl.getParticipants()) {
            participantImpl.getMyRoles().remove(roleImpl);
        }

        identityService.getRoleImpls().remove(roleImpl.getID());
        return this;
    }

    @Override
    public IdentityBuilder subRoleOf(UUID subRole, UUID superRole) {

        return null; // TODO @Scherapo - bin mir nicht sicher ob wir überhaupt Oberrollen brauchen; sollte nochmal diskutiert werden  
    }
}
