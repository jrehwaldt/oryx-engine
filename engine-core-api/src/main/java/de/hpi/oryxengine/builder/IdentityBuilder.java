package de.hpi.oryxengine.builder;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.identity.Capability;
import de.hpi.oryxengine.identity.OrganizationUnit;
import de.hpi.oryxengine.identity.Participant;
import de.hpi.oryxengine.identity.Position;
import de.hpi.oryxengine.identity.Role;

/**
 * The ResourceBuilder provides an easy and intuitive way to define and
 * customize the enterprise's organization structures.
 * 
 * @author Gery
 */
public interface IdentityBuilder {
    /**
     * 
     * @param capabilityId
     * @return
     */
    Capability createCapability(String capabilityId);

    /**
     * Creates a new Participant.
     * 
     * @param participantId
     *            - is the id of the instantiated object
     * @return the created participant object
     */
    Participant createParticipant(String participantId);

    /**
     * Creates a new OrganizationUnit.
     * 
     * @param organizationUnitId
     *            - is the id of the instantiated object
     * @return
     */
    OrganizationUnit createOrganizationUnit(String organizationUnitId);

    IdentityBuilder deleteOrganizationUnit(String organizationUnitId);

    /**
     * 
     * @param positionId
     *            - is the id of the instantiated object
     * @return
     */
    Position createPosition(String positionId);

    /**
     * 
     * @param roleId
     * @return
     */
    Role createRole(String roleId);

    /**
     * Builds a relationship between an OrganizationUnit and a Position which
     * represents that a Position belongs to an OrganizationUnit.
     * 
     * Be aware that an OrganizationUnit could have several Positions. So
     * calling this method twice with the same Position parameter does not
     * create two relationships cause the second Position is already there.
     * 
     * Be also aware that, only one Position is related to an OrganizationUnit.
     * Using this method assures this constraint.
     * 
     * @param organizationUnit
     *            -
     * @param position
     *            - that belongs to the organizationUnit
     * @return the IdentityBuilder itself
     */
    IdentityBuilder organizationUnitOffersPosition(OrganizationUnit organizationUnit, Position position);
    IdentityBuilder organizationUnitDoesNotOfferPosition(OrganizationUnit organizationUnit, Position position);
    /**
     * 
     * 
     * @param subOrganizationUnit
     * @param superOrganizationUnit
     * @return the IdentityBuilder itself
     */
    IdentityBuilder subOrganizationUnitOf(OrganizationUnit subOrganizationUnit, OrganizationUnit superOrganizationUnit);

    IdentityBuilder participantOccupiesPosition(Participant participant, Position position);

    IdentityBuilder participantBelongsToRole(Participant participant, Role role);

    IdentityBuilder participantHasCapability(Participant participant, Capability capability);

    IdentityBuilder subRoleOf(Role subRole, Role superRole);

    IdentityBuilder positionReportsToSuperior(Position position, Position superiorPosition);

    IdentityService getIdentityStructure();

}
