package de.hpi.oryxengine.builder;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.exception.OryxEngineException;
import de.hpi.oryxengine.identity.Capability;
import de.hpi.oryxengine.identity.OrganizationUnit;
import de.hpi.oryxengine.identity.Participant;
import de.hpi.oryxengine.identity.Position;
import de.hpi.oryxengine.identity.Role;
import de.hpi.oryxengine.resource.CapabilityImpl;
import de.hpi.oryxengine.resource.OrganizationUnitImpl;
import de.hpi.oryxengine.resource.PositionImpl;

/**
 * 
 * @author Gerardo Navarro Suarez
 */
public class IdentityBuilderImpl implements IdentityBuilder {

    private IdentityServiceImpl identityService;

    public IdentityBuilderImpl(IdentityServiceImpl identityServiceImpl) {

        identityService = identityServiceImpl;
    }

    public Capability createCapability(String capabilityId) {

        // hier könnte man das FlyWeight-Pattern verwenden ...
        Capability capability = new CapabilityImpl(capabilityId);
        identityService.getCapabilities().add(capability);

        return capability;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Participant createParticipant(String participantId) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrganizationUnit createOrganizationUnit(String organizationUnitId) {

        OrganizationUnitImpl organizationUnit = new OrganizationUnitImpl(organizationUnitId);
        identityService.getOrganizationUnitImpls().add(organizationUnit);

        return organizationUnit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Position createPosition(String positionId) {

        PositionImpl position = new PositionImpl(positionId);
        identityService.getPositionImpls().add(position);

        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role createRole(String roleId) {

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityService getIdentityStructure() {

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityBuilder organizationUnitOffersPosition(OrganizationUnit organizationUnit, Position position) {

        if (position == null) {
            throw new OryxEngineException("The Position parameter is null.");
        }
        if (organizationUnit == null) {
            throw new OryxEngineException("The Position parameter is null.");
        }

        PositionImpl positionImpl = identityService.findPositionImpl(position.getId());
        if (positionImpl == null) {
            throw new OryxEngineException("There exists no Position with the id " + position.getId() + ".");
        }
        OrganizationUnitImpl organizationUnitImpl = identityService.findOrganizationUnitImpl(organizationUnit.getId());
        if (organizationUnitImpl == null) {
            throw new OryxEngineException("There exists no OrganizationUnit with the id " + organizationUnit.getId() + ".");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityBuilder subOrganizationUnitOf(OrganizationUnit subOrganizationUnit,
                                                 OrganizationUnit superOrganizationUnit) {

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityBuilder participantOccupiesPosition(Participant participant, Position position) {

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityBuilder participantBelongsToRole(Participant participant, Role role) {

        // TODO Auto-generated method stub
        return null;
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
    public IdentityBuilder subRoleOf(Role subRole, Role superRole) {

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityBuilder positionReportsToSuperior(Position position, Position superiorPosition) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IdentityBuilder deleteOrganizationUnit(String organizationUnitId) {

        OrganizationUnitImpl organizationUnitImpl = identityService.findOrganizationUnitImpl(organizationUnitId);
        if (organizationUnitImpl == null) {
            throw new OryxEngineException("There exists no OrganizationUnit with the id " + organizationUnitId + ".");
        }
            
        identityService.getOrganizationUnitImpls().remove(organizationUnitImpl);
        return this;
    }

    @Override
    public IdentityBuilder organizationUnitDoesNotOfferPosition(OrganizationUnit organizationUnit, Position position) {

        if (position == null) {
            throw new OryxEngineException("The Position parameter is null.");
        }
        if (organizationUnit == null) {
            throw new OryxEngineException("The Position parameter is null.");
        }

        PositionImpl positionImpl = identityService.findPositionImpl(position.getId());
        if (positionImpl == null) {
            throw new OryxEngineException("There exists no Position with the id " + position.getId() + ".");
        }
        OrganizationUnitImpl organizationUnitImpl = identityService.findOrganizationUnitImpl(organizationUnit.getId());
        if (organizationUnitImpl == null) {
            throw new OryxEngineException("There exists no OrganizationUnit with the id " + organizationUnit.getId() + ".");
        }
        
        positionImpl.belongstoOrganization(null);
        organizationUnitImpl.getPositionImpls().remove(positionImpl);
        
        return this;
    }

}
