package de.hpi.oryxengine.resource;

import java.util.Set;

/**
 * A Participants is a resource which a task can be assigned to.
 * 
 * For example, a clerk, but even a manager, etc.
 * 
 * @author Gerardo Navarro Suarez
 */
public interface Participant extends Resource<Participant> {

    /**
     * Returns a read-only Set of all Positions occupied by this Participant.
     *
     * @return a read-only Set of all Positions occupied by this Participant.
     */
    Set<Position> getMyPositions();

    // Participant addMyPosition(Position position);

    /**
     * Returns a read-only Set of all Capabilities of this Participant.
     * 
     * @return a read-only Set of all Capabilities of this Participant
     */
    Set<Capability> getMyCapabilities();
    // Participant addMyCapability(Capability capability);

    /**
     * Returns a read-only Set of all Roles that contains this Participant.
     * 
     * @return a read-only Set of all Roles that contains this Participant
     */
    Set<Role> getMyRoles();
}
