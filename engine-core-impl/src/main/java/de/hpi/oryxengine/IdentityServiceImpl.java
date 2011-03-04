package de.hpi.oryxengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hpi.oryxengine.builder.IdentityBuilder;
import de.hpi.oryxengine.builder.IdentityBuilderImpl;
import de.hpi.oryxengine.identity.Capability;
import de.hpi.oryxengine.identity.OrganizationUnit;
import de.hpi.oryxengine.identity.Participant;
import de.hpi.oryxengine.identity.Position;
import de.hpi.oryxengine.identity.Resource;
import de.hpi.oryxengine.identity.Role;
import de.hpi.oryxengine.resource.CapabilityImpl;
import de.hpi.oryxengine.resource.OrganizationUnitImpl;
import de.hpi.oryxengine.resource.ParticipantImpl;
import de.hpi.oryxengine.resource.PositionImpl;
import de.hpi.oryxengine.resource.ResourceImpl;
import de.hpi.oryxengine.resource.RoleImpl;

/**
 * The IdentityServiceImpl is concrete implamentation of th IdentityService that is provided by the engine. This
 * implementation is designed to store all information regarding the organization structure in the engine. Others
 * Identity Service implementation might think about database connections.
 * 
 * @author Gerardo Navarro Suarez
 */
public class IdentityServiceImpl implements IdentityService {

    private ArrayList<OrganizationUnitImpl> organizationUnits;
    private ArrayList<PositionImpl> positions;
    private ArrayList<ParticipantImpl> participants;
    private ArrayList<RoleImpl> roles;
    private ArrayList<CapabilityImpl> capabilities;

    public IdentityBuilder getIdentityBuilder() {

        return new IdentityBuilderImpl(this);
    }

    public List<OrganizationUnit> getOrganizationUnits() {

        return new ArrayList<OrganizationUnit>(getOrganizationUnitImpls());
    }

    public ArrayList<OrganizationUnitImpl> getOrganizationUnitImpls() {

        if (organizationUnits == null) {
            organizationUnits = new ArrayList<OrganizationUnitImpl>();
        }
        return organizationUnits;
    }

    public List<Position> getPositions() {

        return new ArrayList<Position>(getPositionImpls());
    }

    public ArrayList<PositionImpl> getPositionImpls() {

        if (positions == null) {
            positions = new ArrayList<PositionImpl>();
        }
        return positions;
    }

    public List<Participant> getParticipants() {

        if (participants == null) {
            participants = new ArrayList<ParticipantImpl>();
        }
        return new ArrayList<Participant>(participants);
    }

    public List<Role> getRoles() {

        if (roles == null) {
            roles = new ArrayList<RoleImpl>();
        }
        return new ArrayList<Role>(roles);
    }

    public List<Capability> getCapabilities() {

        if (capabilities == null) {
            capabilities = new ArrayList<CapabilityImpl>();
        }
        return new ArrayList<Capability>(capabilities);
    }

    public OrganizationUnitImpl findOrganizationUnitImpl(String id) {

        return find(getOrganizationUnitImpls(), id);
    }

    /**
     * Looks if the Resource with the given Id is already in the list.
     * 
     * @param <R>
     * @param resourceList - a list that contains object that inherits
     * @param id
     * @return <R> object, if it is in the resourcelist, otherwise null
     */
    @SuppressWarnings("unchecked")
    private static <R extends ResourceImpl<?>> R find(List<R> resourceList, String id) {

        for (ResourceImpl<?> resource : resourceList) {
            if (resource.getId().equals(id)) {
                return (R) resource;
            }
        }
        return null;
    }

    public PositionImpl findPositionImpl(String id) {

        return find(getPositionImpls(), id);
    }

}
