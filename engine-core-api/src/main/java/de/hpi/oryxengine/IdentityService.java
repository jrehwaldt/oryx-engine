package de.hpi.oryxengine;

import java.util.Set;

import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.OrganizationUnit;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Position;
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
 * <li>Participants - occupy a certain Position in the company, belongs to a certain role and have certain Capabilities</li>
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
    IdentityBuilder getIdentityBuilder();

    // Letztendlich brauchen wir ein QueryObject
    // void createResourceQuery();

    /**
     * Gets the organization units.
     * 
     * @return the organization units
     */
    Set<OrganizationUnit> getOrganizationUnits();

    /**
     * Gets the positions.
     * 
     * @return the positions
     */
    Set<Position> getPositions();

    /**
     * Gets the participants.
     * 
     * @return the participants
     */
    Set<Participant> getParticipants();

    /**
     * Gets the roles.
     * 
     * @return the roles
     */
    Set<Role> getRoles();
    // Set<Capability> getCapabilities();
}
