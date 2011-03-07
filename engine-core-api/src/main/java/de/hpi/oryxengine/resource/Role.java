package de.hpi.oryxengine.resource;

import java.util.Set;


/**
 * A Role is a duty or a set of duties that are performed by one or more participants. Imagine that it is a group. For
 * example, a Role could be team XY (representing all participants belonging to that group), the group of all account
 * manager.
 * 
 * A Role could have a superior Role.
 * 
 * @author Gerardo Navarro Suarez
 */
public interface Role extends Resource<Role> {

    /**
     * Returns the superior Role of the current Role.
     * 
     * @return the superior Role of the current Role
     */
    Role getSuperRole();
    // Das änder ich vielleicht noch
    // Role setSuperRole(Role superRole);
    
    /**
     * Returns a read-only Set of all participants belonging to that Role.
     * 
     * @return a read-only Set of all participants belonging to that Role
     */
    Set<Participant> getParticipants();
}
