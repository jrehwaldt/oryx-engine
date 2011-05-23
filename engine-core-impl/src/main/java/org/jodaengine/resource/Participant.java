package org.jodaengine.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.resource.worklist.AbstractWorklist;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.ParticipantWorklist;
import org.jodaengine.resource.worklist.WorklistItemState;


/**
 * A Participants is a resource which a task can be assigned to.
 * 
 * For example, a clerk, but even a manager, etc.
 * 
 * @author Gerardo Navarro Suarez
 */
public class Participant extends AbstractParticipant {

    private Set<Position> myPositions;

    private Set<AbstractCapability> myCapabilities;

    private Set<Role> myRoles;

    /**
     * Hidden jaxb constructor.
     */
    protected Participant() {

    }

    /**
     * Instantiates a new {@link Participant}.
     * 
     * @param participantName
     *            the name of the {@link Participant}
     */
    public Participant(String participantName) {

        super(participantName);
    }

    @Override
    public Set<AbstractPosition> getMyPositionsImmutable() {

        Set<AbstractPosition> setToReturn = new HashSet<AbstractPosition>(getMyPositions());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Returns a Set of all PositionImpl of this Participant.
     * 
     * @return a Set of all PositionImpl of this Participant
     */
    @JsonProperty
    @JsonManagedReference
    protected Set<Position> getMyPositions() {

        if (myPositions == null) {
            myPositions = new HashSet<Position>();
        }
        return myPositions;
    }

    @Override
    public Set<AbstractCapability> getMyCapabilitiesImmutable() {

        Set<AbstractCapability> setToReturn = new HashSet<AbstractCapability>(getMyCapabilities());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Returns a Set of all {@link Capability}s of this Participant.
     * 
     * @return a Set of all {@link Capability}s of this Participant
     */
    @JsonProperty
    @JsonManagedReference
    protected Set<AbstractCapability> getMyCapabilities() {

        // TODO hier muss noch was gemacht werden

        if (myCapabilities == null) {
            myCapabilities = new HashSet<AbstractCapability>();
        }
        return myCapabilities;
    }

    @Override
    public Set<AbstractRole> getMyRolesImmutable() {

        Set<AbstractRole> setToReturn = new HashSet<AbstractRole>(getMyRoles());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Returns a Set of all RoleImpl of this Participant.
     * 
     * @return a Set of all RoleImpl of this Participant
     */
    @JsonProperty
    @JsonManagedReference
    protected synchronized Set<Role> getMyRoles() {

        if (myRoles == null) {
            myRoles = new HashSet<Role>();
        }
        return myRoles;
    }

    @Override
    public synchronized AbstractWorklist getWorklist() {

        if (worklist == null) {
            worklist = new ParticipantWorklist(this);
        }

        return worklist;
    }

    /**
     * Translates a Participant into a corresponding ParticipantImpl object.
     * 
     * Furthermore some constrains are checked.
     * 
     * @param participantID
     *            - a Participant object
     * @return participantImpl - the casted Participant object
     * @throws ResourceNotAvailableException 
     */
    public static Participant asParticipantImpl(UUID participantID) throws ResourceNotAvailableException {

        if (participantID == null) {
            throw new JodaEngineRuntimeException("The Participant parameter is null.");
        }

        Participant participantImpl = (Participant) ServiceFactory.getIdentityService().getParticipant(participantID);

        if (participantImpl == null) {
            throw new JodaEngineRuntimeException("There exists no Participant with the id " + participantID + ".");
        }
        return participantImpl;
    }

    @Override
    public List<AbstractWorklistItem> getWorklistItemsCurrentlyInWork() {

        List<AbstractWorklistItem> inWork = new ArrayList<AbstractWorklistItem>();
        if (this.worklist != null) {
            for (AbstractWorklistItem w : this.worklist) {
                if (w.getStatus() == WorklistItemState.EXECUTING) {
                    inWork.add(w);
                }
            }
        }
        return inWork;
    }
}
