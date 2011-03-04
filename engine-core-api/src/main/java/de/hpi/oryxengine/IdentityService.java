package de.hpi.oryxengine;

import java.util.List;

import de.hpi.oryxengine.builder.IdentityBuilder;
import de.hpi.oryxengine.identity.Capability;
import de.hpi.oryxengine.identity.OrganizationUnit;
import de.hpi.oryxengine.identity.Participant;
import de.hpi.oryxengine.identity.Position;
import de.hpi.oryxengine.identity.Role;

/**
 * The ResourceService provides an interface for:
 * <ul>
 *  <li>the creation of the organization structure</li>
 *  <li>querying the the organization structure</li>
 * </ul>
 * 
 * @author Gerardo Navarro Suarez
 */
public interface IdentityService {

    IdentityBuilder getIdentityBuilder();

    // Letztendlich brauchen wir ein QueryObject
    // void createResourceQuery();

    List<OrganizationUnit> getOrganizationUnits();
    List<Position> getPositions();
    List<Participant> getParticipants();
    List<Role> getRoles();
    List<Capability> getCapabilities();
}
