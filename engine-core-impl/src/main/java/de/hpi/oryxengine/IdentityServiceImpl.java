package de.hpi.oryxengine;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.AbstractResourceImpl;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.IdentityBuilderImpl;
import de.hpi.oryxengine.resource.OrganizationUnitImpl;
import de.hpi.oryxengine.resource.ParticipantImpl;
import de.hpi.oryxengine.resource.PositionImpl;
import de.hpi.oryxengine.resource.ResourceType;
import de.hpi.oryxengine.resource.RoleImpl;

/**
 * The IdentityServiceImpl is concrete implementation of the {@link IdentityService} that is provided by the engine.
 * This implementation is designed to store all information regarding the organization structure in the engine. Others
 * Identity Service implementation might think about database connections.
 */
public class IdentityServiceImpl implements IdentityService, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private Map<UUID, OrganizationUnitImpl> organizationUnits;
    
    private Map<UUID, PositionImpl> positions;
    
    private Map<UUID, ParticipantImpl> participants;
    
    private Map<UUID, RoleImpl> roles;
    
    @Override
    public void start() {
        
        logger.info("Starting the correlation manager");
    }

    @Override
    public void stop() {
        
        logger.info("Stopping the correlation manager");
    }

    @Override
    public IdentityBuilder getIdentityBuilder() {

        return new IdentityBuilderImpl(this);
    }

    @Override
    public Set<OrganizationUnitImpl> getOrganizationUnits() {

        Set<OrganizationUnitImpl> setToReturn = new HashSet<OrganizationUnitImpl>(getOrganizationUnitImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }

    public Map<UUID, OrganizationUnitImpl> getOrganizationUnitImpls() {

        if (organizationUnits == null) {
            organizationUnits = new HashMap<UUID, OrganizationUnitImpl>();
        }
        return organizationUnits;
    }

    @Override
    public Set<PositionImpl> getPositions() {

        Set<PositionImpl> setToReturn = new HashSet<PositionImpl>(getPositionImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }

    public Map<UUID, PositionImpl> getPositionImpls() {

        if (positions == null) {
            positions = new HashMap<UUID, PositionImpl>();
        }
        return positions;
    }

    @Override
    public Set<ParticipantImpl> getParticipants() {

        Set<ParticipantImpl> setToReturn = new HashSet<ParticipantImpl>(getParticipantImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }

    public Map<UUID, ParticipantImpl> getParticipantImpls() {

        if (participants == null) {
            participants = new HashMap<UUID, ParticipantImpl>();
        }
        return participants;
    }

    @Override
    public Set<RoleImpl> getRoles() {

        Set<RoleImpl> setToReturn = new HashSet<RoleImpl>(getRoleImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }

    public Map<UUID, RoleImpl> getRoleImpls() {

        if (roles == null) {
            roles = new HashMap<UUID, RoleImpl>();
        }
        return roles;
    }

    @Override
    public @Nullable AbstractResource<?> findResource(@Nonnull ResourceType resourceType,
                                                      @Nonnull UUID id) {

        return null; // TODO Gerardo verkloppen... einfach Methoden so umschreiben, dass sie null zurückgeben
                     //      gibt eine Uff'n Kopp!
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
//        return find(getParticipants(), id);
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
     *            - a subclass of {@link AbstractResourceImpl}
     * @param resourceList
     *            - a list that contains object that inherits
     * @param id
     *            - id of the resource to look for
     * @return <R> object, if it is in the resourceList, otherwise null
     */
    @SuppressWarnings("unchecked")
    private static @Nullable <R extends AbstractResourceImpl<?>> R find(@Nonnull Set<R> resourceList,
                                                                        @Nonnull UUID id) {

        for (AbstractResourceImpl<?> resource: resourceList) {
            if (resource.getID().equals(id)) {
                return (R) resource;
            }
        }
        return null;
    }
}
