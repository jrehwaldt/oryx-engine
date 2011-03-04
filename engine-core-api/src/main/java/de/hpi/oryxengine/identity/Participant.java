package de.hpi.oryxengine.identity;

import java.util.List;

/**
 * A Participants is a resource which a task can be assigned to.
 * 
 * For example, a clerk, but even a manager, etc.
 * 
 * @author Gerardo Navarro Suarez
 */
public interface Participant extends Resource<Participant> {

    /**
     * @return read-only list
     */
    List<Position> getMyPositions();

    // Participant addMyPosition(Position position);

    /**
     * @return read-only list
     */
    List<Capability> getMyCapabilities();
    // Participant addMyCapability(Capability capability);
}
