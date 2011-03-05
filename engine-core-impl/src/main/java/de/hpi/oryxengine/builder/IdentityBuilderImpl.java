package de.hpi.oryxengine.builder;

import java.util.Set;

import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.exception.OryxEngineException;
import de.hpi.oryxengine.identity.Capability;
import de.hpi.oryxengine.identity.OrganizationUnit;
import de.hpi.oryxengine.identity.Participant;
import de.hpi.oryxengine.identity.Position;
import de.hpi.oryxengine.identity.Role;
import de.hpi.oryxengine.resource.CapabilityImpl;
import de.hpi.oryxengine.resource.OrganizationUnitImpl;
import de.hpi.oryxengine.resource.ParticipantImpl;
import de.hpi.oryxengine.resource.PositionImpl;
import de.hpi.oryxengine.resource.RoleImpl;

/**
 * 
 * @author Gerardo Navarro Suarez
 */
public class IdentityBuilderImpl implements IdentityBuilder {

    private IdentityServiceImpl identityService;

    public IdentityBuilderImpl(IdentityServiceImpl identityServiceImpl) {

        identityService = identityServiceImpl;
    }

    // -------- Participant Builder Methods -----------

    /**
     * {@inheritDoc}
     */
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
    public IdentityBuilder deleteParticipant(Participant participant) {

        ParticipantImpl participantImpl = (ParticipantImpl) participant;

        if (!identityService.getParticipantImpls().contains(participant)) {
            throw new OryxEngineException("There exists no Participant with the id " + participant.getId() + ".");
        }

        for (PositionImpl positionImpl : participantImpl.getMyPositionImpls()) {
            positionImpl.setPositionHolder(null);
        }

        identityService.getParticipantImpls().remove(participantImpl);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityBuilder participantOccupiesPosition(Participant participant, Position position) {

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
    public IdentityBuilder participantDoesNotOccupyPosition(Participant participant, Position position) {

        PositionImpl positionImpl = extractPositionImplFrom(position);
        ParticipantImpl participantImpl = extractParticipantImplFrom(participant);

        positionImpl.belongstoOrganization(null);
        participantImpl.getMyPositionImpls().remove(positionImpl);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityBuilder participantHasCapability(Participant participant, Capability capability) {

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityBuilder participantBelongsToRole(Participant participant, Role role) {

        RoleImpl roleImpl = extractRoleImplFrom(role);
        ParticipantImpl participantImpl = extractParticipantImplFrom(participant);

        roleImpl.getParticipantImpls().add(participantImpl);
        participantImpl.getMyRolesImpl().add(roleImpl);

        return this;
    }

    @Override
    public IdentityBuilder participantDoesNotBelongToRole(Participant participant, Role role) {

        RoleImpl roleImpl = extractRoleImplFrom(role);
        ParticipantImpl participantImpl = extractParticipantImplFrom(participant);

        roleImpl.getParticipantImpls().remove(participantImpl);
        participantImpl.getMyRolesImpl().remove(roleImpl);
        return this;
    }

    private ParticipantImpl extractParticipantImplFrom(Participant participant) {
    
        if (participant == null) {
            throw new OryxEngineException("The Participant parameter is null.");
        }
        ParticipantImpl participantImpl = (ParticipantImpl) participant;
        if (!identityService.getParticipantImpls().contains(participantImpl)) {
            throw new OryxEngineException("There exists no Participant with the id " + participant.getId() + ".");
        }
        return participantImpl;
    }
    
    // -------- Capability Builder Methods ------------

    public Capability createCapability(String capabilityId) {

        // hier könnte man das FlyWeight-Pattern verwenden ...
        Capability capability = new CapabilityImpl(capabilityId);

        return capability;
    }

    // -------- OrganizationUnit Builder Methods ------

    /**
     * {@inheritDoc}
     */
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
    public IdentityBuilder deleteOrganizationUnit(OrganizationUnit organizationUnit) {

        OrganizationUnitImpl organizationUnitImpl = (OrganizationUnitImpl) organizationUnit;

        if (!identityService.getOrganizationUnitImpls().contains(organizationUnit)) {
            throw new OryxEngineException("There exists no OrganizationUnit with the id " + organizationUnit.getId()
                + ".");
        }

        for (OrganizationUnitImpl childOrganizationUnitImpl : organizationUnitImpl.getChildOrganisationUnitImpls()) {
            childOrganizationUnitImpl.setSuperOrganizationUnit(null);
        }

        for (PositionImpl positionImpl : organizationUnitImpl.getPositionImpls()) {
            positionImpl.belongstoOrganization(null);
        }

        identityService.getOrganizationUnitImpls().remove(organizationUnitImpl);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityBuilder subOrganizationUnitOf(OrganizationUnit subOrganizationUnit,
                                                 OrganizationUnit superOrganizationUnit) {

        OrganizationUnitImpl organizationUnitImpl = (OrganizationUnitImpl) subOrganizationUnit;
        OrganizationUnitImpl superOrganizationUnitImpl = (OrganizationUnitImpl) superOrganizationUnit;
        if (organizationUnitImpl.equals(superOrganizationUnitImpl)) {
            throw new OryxEngineException("The OrganizationUnit cannot be the superior of yourself.");
        }

        organizationUnitImpl.setSuperOrganizationUnit(superOrganizationUnitImpl);

        superOrganizationUnitImpl.getChildOrganisationUnitImpls().add(organizationUnitImpl);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityBuilder organizationUnitOffersPosition(OrganizationUnit organizationUnit, Position position) {

        extractPositionImplFrom(position);
        if (organizationUnit == null) {
            throw new OryxEngineException("The Position parameter is null.");
        }

        PositionImpl positionImpl = identityService.findPositionImpl(position.getId());
        if (positionImpl == null) {
            throw new OryxEngineException("There exists no Position with the id " + position.getId() + ".");
        }
        OrganizationUnitImpl organizationUnitImpl = identityService.findOrganizationUnitImpl(organizationUnit.getId());
        if (organizationUnitImpl == null) {
            throw new OryxEngineException("There exists no OrganizationUnit with the id " + organizationUnit.getId()
                + ".");
        }

        OrganizationUnitImpl oldOrganizationUnit = (OrganizationUnitImpl) positionImpl.belongstoOrganization();
        if (oldOrganizationUnit != null) {
            if (!oldOrganizationUnit.equals(organizationUnitImpl)) {
                oldOrganizationUnit.getPositionImpls().remove(positionImpl);
            }
        }

        positionImpl.belongstoOrganization(organizationUnitImpl);
        organizationUnitImpl.addPosition(positionImpl);

        return this;
    }

    @Override
    public IdentityBuilder organizationUnitDoesNotOfferPosition(OrganizationUnit organizationUnit, Position position) {

        extractPositionImplFrom(position);
        if (organizationUnit == null) {
            throw new OryxEngineException("The OrganizationUnit parameter is null.");
        }

        PositionImpl positionImpl = identityService.findPositionImpl(position.getId());
        if (positionImpl == null) {
            throw new OryxEngineException("There exists no Position with the id " + position.getId() + ".");
        }
        OrganizationUnitImpl organizationUnitImpl = identityService.findOrganizationUnitImpl(organizationUnit.getId());
        if (organizationUnitImpl == null) {
            throw new OryxEngineException("There exists no OrganizationUnit with the id " + organizationUnit.getId()
                + ".");
        }

        positionImpl.belongstoOrganization(null);
        organizationUnitImpl.getPositionImpls().remove(positionImpl);

        return this;
    }

    // -------- Position Builder Methods --------------

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityBuilder positionReportsToSuperior(Position position, Position superiorPosition) {

        PositionImpl positionImpl = (PositionImpl) position;
        PositionImpl superiorPositionImpl = (PositionImpl) superiorPosition;
        if (positionImpl.equals(superiorPositionImpl)) {
            throw new OryxEngineException("The Position '" + positionImpl.getId()
                + "' cannot be the superior of yourself.");
        }

        positionImpl.setSuperiorPosition(superiorPosition);

        superiorPositionImpl.getSubordinatePositionImpls().add(positionImpl);

        return this;
    }

    @Override
    public IdentityBuilder deletePosition(Position position) {

        PositionImpl positionImpl = (PositionImpl) position;
        if (!identityService.getPositionImpls().contains(positionImpl)) {
            throw new OryxEngineException("There exists no OrganizationUnit with the id " + position.getId() + ".");
        }

        identityService.getPositionImpls().remove(position);

        for (PositionImpl subordinatePosition : positionImpl.getSubordinatePositionImpls()) {
            subordinatePosition.setSuperiorPosition(null);
        }

        return this;
    }

    private PositionImpl extractPositionImplFrom(Position position) {
    
        if (position == null) {
            throw new OryxEngineException("The Position parameter is null.");
        }
        PositionImpl positionImpl = (PositionImpl) position;
        if (!identityService.getPositionImpls().contains(positionImpl)) {
            throw new OryxEngineException("There exists no Position with the id " + position.getId() + ".");
        }
        return positionImpl;
    }

    // -------- Role Builder Methods ------------------
    
    /**
     * {@inheritDoc}
     */
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
    public IdentityBuilder deleteRole(Role role) {

        RoleImpl roleImpl = (RoleImpl) role;

        if (!identityService.getRoleImpls().contains(role)) {
            throw new OryxEngineException("There exists no Role with the id " + role.getId() + ".");
        }

        for (ParticipantImpl participantImpl : roleImpl.getParticipantImpls()) {
            participantImpl.getMyRolesImpl().remove(roleImpl);
        }

        identityService.getRoleImpls().remove(roleImpl);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityBuilder subRoleOf(Role subRole, Role superRole) {

        // TODO Auto-generated method stub
        return null;
    }

    private RoleImpl extractRoleImplFrom(Role role) {
    
        if (role == null) {
            throw new OryxEngineException("The Role parameter is null.");
        }
        RoleImpl roleImpl = (RoleImpl) role;
        if (!identityService.getRoleImpls().contains(roleImpl)) {
            throw new OryxEngineException("There exists no Role with the id " + role.getId() + ".");
        }
        return roleImpl;
    }
}
