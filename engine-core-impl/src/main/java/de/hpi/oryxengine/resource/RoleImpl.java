package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Role;
/**
 * 
 * @author Gerardo Navarro Suarez
 */
public class RoleImpl extends ResourceImpl<Role> implements Role {

    Set<ParticipantImpl> participants;
    
    public RoleImpl(String roleId) {

        super(roleId);
    }

    @Override
    public Role getSuperRole() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Role setSuperRole(Role superRole) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Participant> getParticipants() {

        Set<Participant> setToReturn = new HashSet<Participant>(getParticipantImpls());
        return Collections.unmodifiableSet(setToReturn);
    }
    
    public Set<ParticipantImpl> getParticipantImpls() {
        if (participants == null) {
            participants = new HashSet<ParticipantImpl>();
        }
        return participants;
    }

}
