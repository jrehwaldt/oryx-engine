package de.hpi.oryxengine;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.IdentityBuilderImpl;
import de.hpi.oryxengine.resource.OrganizationUnit;
import de.hpi.oryxengine.resource.OrganizationUnitImpl;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.ParticipantImpl;
import de.hpi.oryxengine.resource.Position;
import de.hpi.oryxengine.resource.PositionImpl;
import de.hpi.oryxengine.resource.ResourceImpl;
import de.hpi.oryxengine.resource.Role;
import de.hpi.oryxengine.resource.RoleImpl;

/**
 * The IdentityServiceImpl is concrete implementation of the {@link IdentityService} that is provided by the engine.
 * This implementation is designed to store all information regarding the organization structure in the engine. Others
 * Identity Service implementation might think about database connections.
 */
public class IdentityServiceImpl implements IdentityService {

    /** The organization units. */
    private Set<OrganizationUnitImpl> organizationUnits;
    
    /** The positions. */
    private Set<PositionImpl> positions;
    
    /** The participants. */
    private Set<ParticipantImpl> participants;
    
    /** The roles. */
    private Set<RoleImpl> roles;
    
    /** The capabilities. */
//    private Set<CapabilityImpl> capabilities;

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityBuilder getIdentityBuilder() {

        return new IdentityBuilderImpl(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<OrganizationUnit> getOrganizationUnits() {

        Set<OrganizationUnit> setToReturn = new HashSet<OrganizationUnit>(getOrganizationUnitImpls());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Gets the {@link OrganizationUnit} implementations.
     *
     * @return a set of organizationUnitImpls
     */
    public Set<OrganizationUnitImpl> getOrganizationUnitImpls() {

        if (organizationUnits == null) {
            organizationUnits = new HashSet<OrganizationUnitImpl>();
        }
        return organizationUnits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Position> getPositions() {

        Set<Position> setToReturn = new HashSet<Position>(getPositionImpls());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Gets the {@link Position} implementations.
     *
     * @return a set of PositionImpls
     */
    public Set<PositionImpl> getPositionImpls() {

        if (positions == null) {
            positions = new HashSet<PositionImpl>();
        }
        return positions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Participant> getParticipants() {

        Set<Participant> setToReturn = new HashSet<Participant>(getParticipantImpls());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Gets the {@link Participant} implementations.
     *
     * @return a set of ParticipantImpls
     */
    public Set<ParticipantImpl> getParticipantImpls() {

        if (participants == null) {
            participants = new HashSet<ParticipantImpl>();
        }
        return participants;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Role> getRoles() {

        Set<Role> setToReturn = new HashSet<Role>(getRoleImpls());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Gets the {@link Role} implementations.
     *
     * @return a set of RoleImpls
     */
    public Set<RoleImpl> getRoleImpls() {

        if (roles == null) {
            roles = new HashSet<RoleImpl>();
        }
        return roles;
    }

    /**
     * Retrieves a specific {@link OrganizationUnitImpl}.
     * 
     * @param id
     *            - id of the {@link OrganizationUnitImpl} to look for
     * @return organizationUnitImpl
     */
    protected OrganizationUnitImpl findOrganizationUnitImpl(String id) {

        return find(getOrganizationUnitImpls(), id);
    }

    /**
     * Retrieves a specific {@link PositionImpl}.
     * 
     * @param id
     *            - id of the {@link PositionImpl} to look for
     * @return positionImpl
     */
    protected PositionImpl findPositionImpl(String id) {

        return find(getPositionImpls(), id);
    }

    /**
     * Retrieves a specific {@link ParticipantImpl}.
     * 
     * @param id
     *            - id of the {@link ParticipantImpl} to look for
     * @return participantImpl
     */
    protected ParticipantImpl findParticipantImpl(String id) {

        return find(getParticipantImpls(), id);
    }

    /**
     * Retrieves a specific {@link RoleImpl}.
     * 
     * @param id
     *            - id of the {@link RoleImpl} to look for
     * @return roleImpl
     */
    protected RoleImpl findRoleImpl(String id) {

        return find(getRoleImpls(), id);
    }

    /**
     * Looks if the Resource with the given Id is already in the list.
     * 
     * @param <R>
     *            - a subclass of {@link ResourceImpl}
     * @param resourceList
     *            - a list that contains object that inherits
     * @param id
     *            - id of the resource to look for
     * @return <R> object, if it is in the resourceList, otherwise null
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
}
