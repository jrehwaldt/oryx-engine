package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hpi.oryxengine.identity.Capability;
import de.hpi.oryxengine.identity.OrganizationUnit;
import de.hpi.oryxengine.identity.Participant;
import de.hpi.oryxengine.identity.Position;
import de.hpi.oryxengine.identity.Role;

/**
 * 
 * @author Gerardo Navarro Suarez
 */
public class ParticipantImpl extends ResourceImpl<Participant> implements Participant {

    private Set<PositionImpl> myPositions;
    private Set<Capability> myCapabilities;
    private Set<RoleImpl> myRoles;

    public ParticipantImpl(String participantId) {

        super(participantId);
    }

    @Override
    public Set<Position> getMyPositions() {

        Set<Position> setToReturn = new HashSet<Position>(getMyPositionImpls());
        return Collections.unmodifiableSet(setToReturn);
    }

    public Set<PositionImpl> getMyPositionImpls() {

        if (myPositions == null) {
            myPositions = new HashSet<PositionImpl>();
        }
        return myPositions;
    }

    // @Override
    // public Participant addMyPosition(Position position) {
    //
    // myPositions.add(position);
    // return this;
    // }

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

    public Set<RoleImpl> getMyRolesImpl() {

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
