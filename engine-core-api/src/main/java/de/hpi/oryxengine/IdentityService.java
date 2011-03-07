package de.hpi.oryxengine;

import java.util.Set;

import de.hpi.oryxengine.builder.IdentityBuilder;
import de.hpi.oryxengine.identity.OrganizationUnit;
import de.hpi.oryxengine.identity.Participant;
import de.hpi.oryxengine.identity.Position;
import de.hpi.oryxengine.identity.Role;

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
 * <li>Participants - occupy a certain Position in the company, belongs to a certain role and have certain
 * Capabilities</li>
 * <li>Positions - belong to a certain OrganizationUnit and are occupied by certain Positions</li>
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

    Set<OrganizationUnit> getOrganizationUnits();

    Set<Position> getPositions();

    Set<Participant> getParticipants();

    Set<Role> getRoles();
    // Set<Capability> getCapabilities();
}
