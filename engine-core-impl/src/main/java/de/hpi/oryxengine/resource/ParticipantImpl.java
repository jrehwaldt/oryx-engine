package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.resource.worklist.ParticipantWorklist;
import de.hpi.oryxengine.resource.worklist.Worklist;

/**
 * Implementation of the {@link Participant} Interface.
 */
public class ParticipantImpl extends ResourceImpl<Participant> implements Participant {

    /** My {@link Position}s. */
    private Set<PositionImpl> myPositions;

    /** My {@link Capability}s. */
    private Set<Capability> myCapabilities;

    /** My {@link Role}s. */
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
     * Gets the my positions.
     * 
     * @return the my positions {@inheritDoc}
     */
    @Override
    public Set<Position> getMyPositions() {

        Set<Position> setToReturn = new HashSet<Position>(getMyPositionImpls());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Gets the my position impls.
     * 
     * @return the my position impls
     */
    protected Set<PositionImpl> getMyPositionImpls() {

        if (myPositions == null) {
            myPositions = new HashSet<PositionImpl>();
        }
        return myPositions;
    }

    @Override
    public Set<Capability> getMyCapabilities() {

        if (myCapabilities == null) {
            myCapabilities = new HashSet<Capability>();
        }
        return myCapabilities;
    }

    @Override
    public Set<Role> getMyRoles() {

        Set<Role> setToReturn = new HashSet<Role>(getMyRolesImpl());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Gets the my roles impl.
     * 
     * @return the my roles impl
     */
    protected Set<RoleImpl> getMyRolesImpl() {

        if (myRoles == null) {
            myRoles = new HashSet<RoleImpl>();
        }
        return myRoles;
    }

    // @Override
    // public Participant addMyCapability(Capability capability) {
    //
    // myCapabilities.add(capability);
    // return this;
    // }

    @Override
    public Worklist getWorklist() {

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
     */
    public static ParticipantImpl asParticipantImpl(UUID participantID) {

        if (participantID == null) {
            throw new DalmatinaRuntimeException("The Participant parameter is null.");
        }

        ParticipantImpl participantImpl = (ParticipantImpl) ServiceFactory.getIdentityService().getParticipant(participantID);
        if (participantImpl == null) {
            
            throw new DalmatinaRuntimeException("There exists no Participant with the id " + participantID + ".");
        }
        return participantImpl;
    }
}
