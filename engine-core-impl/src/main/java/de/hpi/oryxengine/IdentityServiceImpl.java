package de.hpi.oryxengine;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hpi.oryxengine.builder.IdentityBuilder;
import de.hpi.oryxengine.builder.IdentityBuilderImpl;
import de.hpi.oryxengine.identity.OrganizationUnit;
import de.hpi.oryxengine.identity.Participant;
import de.hpi.oryxengine.identity.Position;
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

    private Set<OrganizationUnitImpl> organizationUnits;
    private Set<PositionImpl> positions;
    private Set<ParticipantImpl> participants;
    private Set<RoleImpl> roles;
    private Set<CapabilityImpl> capabilities;

    public IdentityBuilder getIdentityBuilder() {

        return new IdentityBuilderImpl(this);
    }

    public Set<OrganizationUnit> getOrganizationUnits() {

        Set<OrganizationUnit> setToReturn = new HashSet<OrganizationUnit>(getOrganizationUnitImpls());
        return Collections.unmodifiableSet(setToReturn);
    }

    public Set<OrganizationUnitImpl> getOrganizationUnitImpls() {

        if (organizationUnits == null) {
            organizationUnits = new HashSet<OrganizationUnitImpl>();
        }
        return organizationUnits;
    }

    public Set<Position> getPositions() {

        Set<Position> setToReturn = new HashSet<Position>(getPositionImpls());
        return Collections.unmodifiableSet(setToReturn);
    }

    public Set<PositionImpl> getPositionImpls() {

        if (positions == null) {
            positions = new HashSet<PositionImpl>();
        }
        return positions;
    }

    public Set<Participant> getParticipants() {

        Set<Participant> setToReturn = new HashSet<Participant>(getParticipantImpl());
        return Collections.unmodifiableSet(setToReturn);
    }

    public Set<ParticipantImpl> getParticipantImpl() {
        if (participants == null) {
            participants = new HashSet<ParticipantImpl>();
        }
        return participants;
    }
    
    public Set<Role> getRoles() {

        Set<Role> setToReturn = new HashSet<Role>(getRolesImpl());
        return Collections.unmodifiableSet(setToReturn);
    }
    
    public Set<RoleImpl> getRolesImpl() {
        if (roles == null) {
            roles = new HashSet<RoleImpl>();
        }
        return roles;
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
    private static <R extends ResourceImpl<?>> R find(Set<R> resourceList, String id) {

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
