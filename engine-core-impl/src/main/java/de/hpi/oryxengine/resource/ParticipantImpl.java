package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
     * @param participantId the participant id
     */
    public ParticipantImpl(String participantId) {

        super(participantId, ResourceType.PARTICIPANT);
    }

    /**
     * Gets the my positions.
     *
     * @return the my positions
     * {@inheritDoc}
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

}
