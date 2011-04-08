package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hpi.oryxengine.resource.worklist.ParticipantWorklist;
import de.hpi.oryxengine.resource.worklist.Worklist;

/**
 * A Participants is a resource which a task can be assigned to.
 * 
 * For example, a clerk, but even a manager, etc.
 * 
 * @author Gerardo Navarro Suarez
 */
public class ParticipantImpl extends AbstractResourceImpl<ParticipantImpl> {

    /** My {@link AbstractPosition}s. */
    private Set<PositionImpl> myPositions;

    /** My {@link AbstractCapability}s. */
    private Set<CapabilityImpl> myCapabilities;

    /** My {@link AbstractRole}s. */
    private Set<RoleImpl> myRoles;

    /**
     * Instantiates a new {@link ParticipantImpl}.
     * 
     * @param participantName
     *            the name of the {@link ParticipantImpl}
     */
    public ParticipantImpl(String participantName) {

        super(participantName, ResourceType.PARTICIPANT);
    }

    /**
     * Returns a read-only Set of all Positions occupied by this Participant.
     *
     * @return a read-only Set of all Positions occupied by this Participant.
     */
    public Set<PositionImpl> getMyPositionsImmutable() {

        Set<PositionImpl> setToReturn = new HashSet<PositionImpl>(getMyPositions());
        return Collections.unmodifiableSet(setToReturn);
    }
    
    /**
     * Returns a Set of all PositionImpl of this Participant.
     * 
     * @return a Set of all PositionImpl of this Participant
     */
    protected Set<PositionImpl> getMyPositions() {

        if (myPositions == null) {
            myPositions = new HashSet<PositionImpl>();
        }
        return myPositions;
    }
    
    /**
     * Returns a read-only Set of all Capabilities of this Participant.
     * 
     * @return a read-only Set of all Capabilities of this Participant
     */
    protected Set<CapabilityImpl> getMyCapabilities() {

        if (myCapabilities == null) {
            myCapabilities = new HashSet<CapabilityImpl>();
        }
        return myCapabilities;
    }
    
    /**
     * Returns a read-only Set of all Roles that contains this Participant.
     * 
     * @return a read-only Set of all Roles that contains this Participant
     */
    public Set<RoleImpl> getMyRolesImmutable() {

        Set<RoleImpl> setToReturn = new HashSet<RoleImpl>(getMyRoles());
        return Collections.unmodifiableSet(setToReturn);
    }
    
    /**
     * Returns a Set of all RoleImpl of this Participant.
     * 
     * @return a Set of all RoleImpl of this Participant
     */
    protected Set<RoleImpl> getMyRoles() {

        if (myRoles == null) {
            myRoles = new HashSet<RoleImpl>();
        }
        return myRoles;
    }

    @Override
    public Worklist getWorklist() {

        if (worklist == null) {

            worklist = new ParticipantWorklist(this);
        }
        return worklist;
    }
}
