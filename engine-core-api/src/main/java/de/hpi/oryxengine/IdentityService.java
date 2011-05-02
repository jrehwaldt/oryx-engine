package de.hpi.oryxengine;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.hpi.oryxengine.exception.InvalidItemException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.resource.AbstractOrganizationUnit;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractPosition;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.IdentityBuilder;

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
     * @return the {@link AbstractOrganizationUnit}s
     */
    @Nonnull Set<AbstractOrganizationUnit> getOrganizationUnits();

    /**
     * Gets the positions.
     * 
     * @return the {@link AbstractPosition}s
     */
    @Nonnull Set<AbstractPosition> getPositions();

    /**
     * Gets the participants.
     * 
     * @return the {@link AbstractParticipant}s
     */
    @Nonnull Set<AbstractParticipant> getParticipants();

    /**
     * Gets the roles.
     * 
     * @return the {@link AbstractRole}s
     */
    @Nonnull Set<AbstractRole> getRoles();
    
    /**
     * Returns the requested role.
     *
     * @param roleId the role's id
     * @return the {@link AbstractRole}
     * @throws ResourceNotAvailableException the resource not available exception
     */
    @Nullable AbstractRole getRole(@Nonnull UUID roleId) throws ResourceNotAvailableException;
    
    /**
     * Returns the requested positions.
     *
     * @param positionId the position's id
     * @return the {@link AbstractPosition}
     * @throws ResourceNotAvailableException the resource not available exception
     */
    @Nullable AbstractPosition getPosition(@Nonnull UUID positionId) throws ResourceNotAvailableException;
    
    /**
     * Returns the requested organizational units.
     *
     * @param organizationUnitId the organization's id
     * @return the {@link AbstractOrganizationUnit}
     * @throws ResourceNotAvailableException the resource not available exception
     */
    @Nullable AbstractOrganizationUnit getOrganizationUnit(@Nonnull UUID organizationUnitId)
    throws ResourceNotAvailableException;
    
    /**
     * Returns the requested participants.
     *
     * @param participantId the participant's id
     * @return the {@link AbstractParticipant}
     * @throws ResourceNotAvailableException the resource not available exception
     */
    @Nullable AbstractParticipant getParticipant(@Nonnull UUID participantId) throws ResourceNotAvailableException;
}
