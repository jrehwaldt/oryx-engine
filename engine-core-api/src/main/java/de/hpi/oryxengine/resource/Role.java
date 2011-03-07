package de.hpi.oryxengine.resource;

import java.util.Set;


/**
 * A Role is a duty or a set of duties that are performed by one or more participants. Imagine that it is a group. For
 * example, a Role could be team XY (representing all participants belonging to that group), the group of all account
 * manager.
 * 
 * @author Gery
 */
public interface Role extends Resource<Role> {
    Role getSuperRole();
    Role setSuperRole(Role superRole);
    
    /**
     * 
     * @return read-only list
     */
    Set<Participant> getParticipants();
}
