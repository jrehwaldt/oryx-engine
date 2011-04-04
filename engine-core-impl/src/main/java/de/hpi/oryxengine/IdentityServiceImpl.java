package de.hpi.oryxengine;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.IdentityBuilderImpl;
import de.hpi.oryxengine.resource.OrganizationUnit;
import de.hpi.oryxengine.resource.OrganizationUnitImpl;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.ParticipantImpl;
import de.hpi.oryxengine.resource.Position;
import de.hpi.oryxengine.resource.PositionImpl;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.resource.ResourceImpl;
import de.hpi.oryxengine.resource.ResourceType;
import de.hpi.oryxengine.resource.Role;
import de.hpi.oryxengine.resource.RoleImpl;

/**
 * The IdentityServiceImpl is concrete implementation of the {@link IdentityService} that is provided by the engine.
 * This implementation is designed to store all information regarding the organization structure in the engine. Others
 * Identity Service implementation might think about database connections.
 */
public class IdentityServiceImpl implements IdentityService {

    private Map<UUID, OrganizationUnitImpl> organizationUnits;
    
    private Map<UUID, PositionImpl> positions;
    
    private Map<UUID, ParticipantImpl> participants;
    
    private Map<UUID, RoleImpl> roles;
    
    @Override
    public IdentityBuilder getIdentityBuilder() {

        return new IdentityBuilderImpl(this);
    }

    @Override
    public Set<OrganizationUnit> getOrganizationUnits() {

        Set<OrganizationUnit> setToReturn = new HashSet<OrganizationUnit>(getOrganizationUnitImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }

    public Map<UUID, OrganizationUnitImpl> getOrganizationUnitImpls() {

        if (organizationUnits == null) {
            organizationUnits = new HashMap<UUID, OrganizationUnitImpl>();
        }
        return organizationUnits;
    }

    @Override
    public Set<Position> getPositions() {

        Set<Position> setToReturn = new HashSet<Position>(getPositionImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }

    public Map<UUID, PositionImpl> getPositionImpls() {

        if (positions == null) {
            positions = new HashMap<UUID, PositionImpl>();
        }
        return positions;
    }

    @Override
    public Set<Participant> getParticipants() {

        Set<Participant> setToReturn = new HashSet<Participant>(getParticipantImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }

    public Map<UUID, ParticipantImpl> getParticipantImpls() {

        if (participants == null) {
            participants = new HashMap<UUID, ParticipantImpl>();
        }
        return participants;
    }

    @Override
    public Set<Role> getRoles() {

        Set<Role> setToReturn = new HashSet<Role>(getRoleImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }

    public Map<UUID, RoleImpl> getRoleImpls() {

        if (roles == null) {
            roles = new HashMap<UUID, RoleImpl>();
        }
        return roles;
    }

    @Override
    public @Nullable Resource<?> findResource(@Nonnull ResourceType resourceType,
                                              @Nonnull UUID id) {

        return null;
//        return find(getOrganizationUnitImpls(), id);
    }

    @Override
    public @Nullable OrganizationUnitImpl getOrganizationUnit(@Nonnull UUID id) {

        return getOrganizationUnitImpls().get(id);
        //        return find(getOrganizationUnitImpls(), id);
    }

    @Override
    public @Nullable PositionImpl getPosition(@Nonnull UUID id) {

        return getPositionImpls().get(id);
//        return find(getPositionImpls(), id);
    }

    @Override
    public @Nullable ParticipantImpl getParticipant(@Nonnull UUID id) {

        return getParticipantImpls().get(id);
//        return find(getParticipantImpls(), id);
    }

    @Override
    public @Nullable RoleImpl getRole(@Nonnull UUID id) {

        return getRoleImpls().get(id);
        //        return find(getRoleImpls(), id);
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
    private static @Nullable <R extends ResourceImpl<?>> R find(@Nonnull Set<R> resourceList,
                                                                @Nonnull UUID id) {

        for (ResourceImpl<?> resource : resourceList) {
            if (resource.getID().equals(id)) {
                return (R) resource;
            }
        }
        return null;
    }
}
