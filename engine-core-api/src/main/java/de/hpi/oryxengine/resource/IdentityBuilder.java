package de.hpi.oryxengine.resource;

/**
 * The ResourceBuilder provides an easy and intuitive way to define and customize the enterprise's organization
 * structures.
 * 
 * You can CRUD Participants, OrganizationUnits, ... You can define the relations between them
 * 
 * @author Gerardo Navarro Suarez
 */
public interface IdentityBuilder {

    // Muss noch geschaut werden ob das wirklich schon gebracuht wird
    /**
     * Creates a new Capability.
     * 
     * @param capabilityId
     *            the capability id
     * @return the capability
     */
    Capability createCapability(String capabilityId);

    /**
     * Creates a new Participant. If a Participant with that participantId already exist, then the old Participant is
     * returned.
     * 
     * @param participantId
     *            - is the id of the instantiated object
     * @return the created Participant object, except when the Participant already exists
     */
    Participant createParticipant(String participantId);

    /**
     * Deletes the given Participant, so that it is removed from the organization structure.
     * 
     * @param participant
     *            object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder deleteParticipant(Participant participant)
    throws Exception;

    /**
     * Creates a new OrganizationUnit. If a OrganizationUnit with that organizationUnitId already exist, then the old
     * OrganizationUnit is returned.
     * 
     * @param organizationUnitId
     *            - is the id of the instantiated object
     * @return the created OrganizationUnit object, except when the OrganizationUnit already exists
     */
    OrganizationUnit createOrganizationUnit(String organizationUnitId);

    /**
     * Deletes the given OrganizationUnit, so that it is removed from the organization structure.
     * 
     * @param organizationUnit
     *            - object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder deleteOrganizationUnit(OrganizationUnit organizationUnit)
    throws Exception;

    /**
     * Creates a new Position. If a Position with that positionId already exist, then the old Position is returned.
     * 
     * @param positionId
     *            - is the id of the instantiated object
     * @return the created Position object, except when the Position already exists
     */
    Position createPosition(String positionId);

    /**
     * Deletes the given Position, so that it is removed from the organization structure.
     * 
     * @param position
     *            - object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder deletePosition(Position position)
    throws Exception;

    /**
     * Creates a new Role. If a Role with that roleId already exist, then the old Role is returned.
     * 
     * @param roleId
     *            - is the id of the instantiated object
     * @return the created Role object, except when the Role already exists
     */
    Role createRole(String roleId);

    /**
     * Deletes the given Role, so that it is removed from the organization structure.
     * 
     * @param role
     *            - object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder deleteRole(Role role)
    throws Exception;

    /**
     * Builds a relationship between an OrganizationUnit and a Position which represents that a Position belongs to an
     * OrganizationUnit.
     * 
     * Calling this method requires that the OrganizationUnit and Position already exist in the organization structure.
     * 
     * Be aware that an OrganizationUnit could have several Positions. So calling this method twice with the same
     * Position parameter does not create two relationships cause the second Position is already there.
     * 
     * Be also aware that, only one Position is related to an OrganizationUnit. Using this method assures this
     * constraint.
     * 
     * @param organizationUnit
     *            - part of the relationship
     * @param position
     *            - that belongs to the organizationUnit
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder organizationUnitOffersPosition(OrganizationUnit organizationUnit, Position position)
    throws Exception;

    /**
     * Removes the relationship between the certain OrganizationUnit and the certain Position.
     * 
     * The given objects are not removed, only the relationship between them is removed.
     * 
     * @param organizationUnit
     *            - part of the relationship
     * @param position
     *            - part of the relationship
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder organizationUnitDoesNotOfferPosition(OrganizationUnit organizationUnit, Position position)
    throws Exception;

    /**
     * Defines the relationship between two OrganizationUnits.
     * 
     * Calling this method requires that the two OrganizationUnits already exist in the organization structure.
     * 
     * After the method is executed the second OrganizationUnit parameter is the superior OrganizationUnit of the first
     * OrganizationUnit parameter.
     * 
     * @param subOrganizationUnit
     *            - {@link OrganizationUnit} below the superOrganizationUnit
     * @param superOrganizationUnit
     *            - {@link OrganizationUnit} above the superOrganizationUnit
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder subOrganizationUnitOf(OrganizationUnit subOrganizationUnit, OrganizationUnit superOrganizationUnit)
    throws Exception;

    /**
     * Defines the relationship between a Participant and a Position.
     * 
     * Be aware that a Position can only be occupied by only one Participant. But a Participant can occupy several
     * Positions.
     * 
     * Calling this method requires that the Participant and the Position already exist in the organization structure.
     * 
     * @param participant
     *            - {@link Participant} that occupies the {@link Position}
     * @param position
     *            - {@link Position} that is occupied by the {@link Participant}
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder participantOccupiesPosition(Participant participant, Position position)
    throws Exception;

    /**
     * Removes the relationship between a certain Participant and a certain Position.
     * 
     * The given objects are not removed, only the relationship between them is removed.
     * 
     * @param participant
     *            - {@link Participant} that occupies the {@link Position}
     * @param position
     *            - {@link Position} that is occupied by the {@link Participant}
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder participantDoesNotOccupyPosition(Participant participant, Position position)
    throws Exception;

    /**
     * Builds a relationship between a Participant and a Role which represents that a Participants belongs to an Role.
     * 
     * Calling this method requires that the Participant and the Role already exist in the organization structure.
     * 
     * Be aware that a Participant Unit could belong to several Roles. So calling this method twice with the same
     * Participant parameter does not create two relationships cause the second Participant is already there.
     * 
     * Be also aware that a Roles could have several Participants, too.
     * 
     * @param participant
     *            - {@link Participant} that should belong to a {@link Role}
     * @param role
     *            - {@link Role} that contains the {@link Participant}
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder participantBelongsToRole(Participant participant, Role role)
    throws Exception;

    /**
     * Removes the relationship between a certain Participant and a certain Role.
     * 
     * The given objects are not removed, only the relationship between them is removed.
     * 
     * @param participant
     *            - {@link Participant} that belongs to the {@link Role}
     * @param role
     *            - {@link Role} that contains the {@link Participant}
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder participantDoesNotBelongToRole(Participant participant, Role role)
    throws Exception;

    /**
     * Participant has capability.
     * 
     * @param participant
     *            the participant
     * @param capability
     *            the capability
     * @return the identity builder
     */
    IdentityBuilder participantHasCapability(Participant participant, Capability capability);

    /**
     * Sub role of.
     * 
     * @param subRole
     *            the sub role
     * @param superRole
     *            the super role
     * @return the identity builder
     */
    IdentityBuilder subRoleOf(Role subRole, Role superRole);

    /**
     * Defines the relationship between two Positions that one position is the superior position of the other position.
     * 
     * Calling this method requires that the two Positions already exist in the organization structure.
     * 
     * After the method is executed the second Position parameter is the superior Position of the first OrganizationUnit
     * parameter.
     * 
     * @param position
     *            - {@link Position} that is below the superior position
     * @param superiorPosition
     *            - {@link Position} that is the superior position of the other one
     * @return the current IdentityBuilder in order to continue building the organization structures
     * @throws Exception
     *             the exception
     */
    IdentityBuilder positionReportsToSuperior(Position position, Position superiorPosition)
    throws Exception;

    // IdentityService getIdentityStructure();

}
