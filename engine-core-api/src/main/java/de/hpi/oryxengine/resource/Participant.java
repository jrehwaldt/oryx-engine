package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hpi.oryxengine.resource.worklist.AbstractWorklist;
import de.hpi.oryxengine.resource.worklist.ParticipantWorklist;

/**
 * A Participants is a resource which a task can be assigned to.
 * 
 * For example, a clerk, but even a manager, etc.
 * 
 * @author Gerardo Navarro Suarez
 */
public class Participant extends AbstractResource<Participant> {

    /** My {@link AbstractPosition}s. */
    private Set<Position> myPositions;

    /** My {@link AbstractCapability}s. */
    private Set<Capability> myCapabilities;

    /** My {@link AbstractRole}s. */
    private Set<Role> myRoles;

    /**
     * Instantiates a new {@link Participant}.
     * 
     * @param participantName
     *            the name of the {@link Participant}
     */
    public Participant(String participantName) {

        super(participantName, ResourceType.PARTICIPANT);
    }

    /**
     * Returns a read-only Set of all Positions occupied by this Participant.
     *
     * @return a read-only Set of all Positions occupied by this Participant.
     */
    public Set<Position> getMyPositionsImmutable() {

        Set<Position> setToReturn = new HashSet<Position>(getMyPositions());
        return Collections.unmodifiableSet(setToReturn);
    }
    
    /**
     * Returns a Set of all PositionImpl of this Participant.
     * 
     * @return a Set of all PositionImpl of this Participant
     */
    protected Set<Position> getMyPositions() {

        if (myPositions == null) {
            myPositions = new HashSet<Position>();
        }
        return myPositions;
    }
    
    /**
     * Returns a read-only Set of all Capabilities of this Participant.
     * 
     * @return a read-only Set of all Capabilities of this Participant
     */
    protected Set<Capability> getMyCapabilities() {

        if (myCapabilities == null) {
            myCapabilities = new HashSet<Capability>();
        }
        return myCapabilities;
    }
    
    /**
     * Returns a read-only Set of all Roles that contains this Participant.
     * 
     * @return a read-only Set of all Roles that contains this Participant
     */
    public Set<Role> getMyRolesImmutable() {

        Set<Role> setToReturn = new HashSet<Role>(getMyRoles());
        return Collections.unmodifiableSet(setToReturn);
    }
    
    /**
     * Returns a Set of all RoleImpl of this Participant.
     * 
     * @return a Set of all RoleImpl of this Participant
     */
    protected Set<Role> getMyRoles() {

        if (myRoles == null) {
            myRoles = new HashSet<Role>();
        }
        return myRoles;
    }

    @Override
    public AbstractWorklist getWorklist() {

        if (worklist == null) {

            worklist = new ParticipantWorklist(this);
        }
        return worklist;
    }
}
