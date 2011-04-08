package de.hpi.oryxengine;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.OrganizationUnit;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Position;
import de.hpi.oryxengine.resource.ResourceType;
import de.hpi.oryxengine.resource.Role;

/**
 * The ResourceService provides an interface for:
 * <ul>
 * <li>the creation of the organization structure</li>
 * <li>querying the the organization structure</li>
 * </ul>
 * 
 * The enterprise's organization structure consists 4 major elements. It contains:
 * <ul>
 * <li>OrganizationUnits - have Positions and have a superior OrganizationUnit</li>
 * <li>Participants - occupy a certain Position in the company, belongs to a certain role and has Capabilities</li>
 * <li>Positions - belong to a certain OrganizationUnit and are occupied by a certain Participant</li>
 * <li>Roles - try to summarize certain Participants in a specific context</li>
 * </ul>
 * 
 * @author Gerardo Navarro Suarez
 */
public interface IdentityService {

    /**
     * Creates an IdentityBuilder, that helps you to manage the organization structure.
     * 
     * @return an IdentityBuilder Object
     */
    @Nonnull IdentityBuilder getIdentityBuilder();

    // Letztendlich brauchen wir ein QueryObject
    // void createResourceQuery();

    /**
     * Gets the organization units.
     * 
     * @return the {@link OrganizationUnit}s
     */
    @Nonnull Set<OrganizationUnit> getOrganizationUnits();

    /**
     * Gets the positions.
     * 
     * @return the {@link Position}s
     */
    @Nonnull Set<Position> getPositions();

    /**
     * Gets the participants.
     * 
     * @return the {@link Participant}s
     */
    @Nonnull Set<Participant> getParticipants();

    /**
     * Gets the roles.
     * 
     * @return the {@link Role}s
     */
    @Nonnull Set<Role> getRoles();
    
    /**
     * Returns the requested role.
     * 
     * @param roleId the role's id
     * @return the {@link Role}
     */
    @Nullable Role getRole(@Nonnull UUID roleId);
    
    /**
     * Returns the requested positions.
     * 
     * @param positionId the position's id
     * @return the {@link Position}
     */
    @Nullable Position getPosition(@Nonnull UUID positionId);
    
    /**
     * Returns the requested organizational units.
     * 
     * @param organizationUnitId the organization's id
     * @return the {@link OrganizationUnit}
     */
    @Nullable OrganizationUnit getOrganizationUnit(@Nonnull UUID organizationUnitId);
    
    /**
     * Returns the requested participants.
     * 
     * @param participantId the participant's id
     * @return the {@link Participant}
     */
    @Nullable Participant getParticipant(@Nonnull UUID participantId);
}
