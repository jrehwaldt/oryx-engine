package de.hpi.oryxengine;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.OrganizationUnitImpl;
import de.hpi.oryxengine.resource.ParticipantImpl;
import de.hpi.oryxengine.resource.PositionImpl;
import de.hpi.oryxengine.resource.ResourceType;
import de.hpi.oryxengine.resource.RoleImpl;

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
     * @return the {@link OrganizationUnitImpl}s
     */
    @Nonnull Set<OrganizationUnitImpl> getOrganizationUnits();

    /**
     * Gets the positions.
     * 
     * @return the {@link PositionImpl}s
     */
    @Nonnull Set<PositionImpl> getPositions();

    /**
     * Gets the participants.
     * 
     * @return the {@link ParticipantImpl}s
     */
    @Nonnull Set<ParticipantImpl> getParticipants();

    /**
     * Gets the roles.
     * 
     * @return the {@link RoleImpl}s
     */
    @Nonnull Set<RoleImpl> getRoles();
    
    /**
     * Returns the requested resource.
     * 
     * @param resourceType the resource's type
     * @param resourceId the resource's id
     * @return the {@link AbstractResource}
     */
    @Nullable AbstractResource<?> findResource(@Nonnull ResourceType resourceType,
                                               @Nonnull UUID resourceId);
    
    /**
     * Returns the requested role.
     * 
     * @param roleId the role's id
     * @return the {@link RoleImpl}
     */
    @Nullable RoleImpl getRole(@Nonnull UUID roleId);
    
    /**
     * Returns the requested positions.
     * 
     * @param positionId the position's id
     * @return the {@link PositionImpl}
     */
    @Nullable PositionImpl getPosition(@Nonnull UUID positionId);
    
    /**
     * Returns the requested organizational units.
     * 
     * @param organizationUnitId the organization's id
     * @return the {@link OrganizationUnitImpl}
     */
    @Nullable OrganizationUnitImpl getOrganizationUnit(@Nonnull UUID organizationUnitId);
    
    /**
     * Returns the requested participants.
     * 
     * @param participantId the participant's id
     * @return the {@link ParticipantImpl}
     */
    @Nullable ParticipantImpl getParticipant(@Nonnull UUID participantId);
}
