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

import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.IdentityBuilderImpl;
import de.hpi.oryxengine.resource.OrganizationUnit;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Position;
import de.hpi.oryxengine.resource.Role;

/**
 * The IdentityServiceImpl is concrete implementation of the {@link IdentityService} that is provided by the engine.
 * This implementation is designed to store all information regarding the organization structure in the engine. Others
 * Identity Service implementation might think about database connections.
 */
public class IdentityServiceImpl implements IdentityService, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private Map<UUID, OrganizationUnit> organizationUnits;
    
    private Map<UUID, Position> positions;
    
    private Map<UUID, Participant> participants;
    
    private Map<UUID, Role> roles;
    
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
    public Set<OrganizationUnit> getOrganizationUnits() {

        Set<OrganizationUnit> setToReturn = new HashSet<OrganizationUnit>(getOrganizationUnitImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Returns a mutable map of orga units.
     * 
     * @return a mutable orga units map.
     */
    public Map<UUID, OrganizationUnit> getOrganizationUnitImpls() {

        if (organizationUnits == null) {
            organizationUnits = new HashMap<UUID, OrganizationUnit>();
        }
        return organizationUnits;
    }

    @Override
    public Set<Position> getPositions() {

        Set<Position> setToReturn = new HashSet<Position>(getPositionImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }
    
    /**
     * Returns a mutable map of positions.
     * 
     * @return a mutable positions map.
     */
    public Map<UUID, Position> getPositionImpls() {

        if (positions == null) {
            positions = new HashMap<UUID, Position>();
        }
        return positions;
    }

    @Override
    public Set<Participant> getParticipants() {

        Set<Participant> setToReturn = new HashSet<Participant>(getParticipantImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Returns a mutable map of participants.
     * 
     * @return a mutable participants map.
     */
    public Map<UUID, Participant> getParticipantImpls() {

        if (participants == null) {
            participants = new HashMap<UUID, Participant>();
        }
        return participants;
    }

    @Override
    public Set<Role> getRoles() {

        Set<Role> setToReturn = new HashSet<Role>(getRoleImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Returns a mutable map of roles.
     * 
     * @return a mutable roles map.
     */
    public Map<UUID, Role> getRoleImpls() {

        if (roles == null) {
            roles = new HashMap<UUID, Role>();
        }
        return roles;
    }

    @Override
    public @Nullable OrganizationUnit getOrganizationUnit(@Nonnull UUID id) {

        return getOrganizationUnitImpls().get(id);
//        return find(getOrganizationUnitImpls(), id);
    }

    @Override
    public @Nullable Position getPosition(@Nonnull UUID id) {

        return getPositionImpls().get(id);
//        return find(getPositionImpls(), id);
    }

    @Override
    public @Nullable Participant getParticipant(@Nonnull UUID id) {

        return getParticipantImpls().get(id);
//        return find(getParticipants(), id);
    }

    @Override
    public @Nullable Role getRole(@Nonnull UUID id) {

        return getRoleImpls().get(id);
        //        return find(getRoleImpls(), id);
    }
}