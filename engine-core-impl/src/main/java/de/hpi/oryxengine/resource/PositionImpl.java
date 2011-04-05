package de.hpi.oryxengine.resource;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;

/**
 * Implementation of {@link Position} Interface.
 */
public class PositionImpl extends ResourceImpl<Position> implements Position {

    private Participant positionHolder;

    private OrganizationUnit organizationalUnit;

    private Position superiorPosition;

    private Set<PositionImpl> subordinatePositions;

    /**
     * The Default Constructor. Creates a position object with the given id.
     * 
     * @param positionName
     *            - identifier for the position Object
     */
    public PositionImpl(String positionName) {

        super(positionName, ResourceType.POSITION);
    }

    @Override
    public Participant getPositionHolder() {

        return positionHolder;
    }

    public Position setPositionHolder(Participant participant) {

        positionHolder = participant;
        return this;
    }

    @Override
    public Position getSuperiorPosition() {

        return superiorPosition;
    }

    public Position setSuperiorPosition(Position position) {

        superiorPosition = position;

        return this;
    }

    @Override
    public OrganizationUnit belongstoOrganization() {

        // TODO hier nochmal quatschen ob eine Position nicht doch zu mehreren Orgas gehört!!!
        return organizationalUnit;
    }

    /**
     * Sets the {@link OrganizationUnit} that offers this {@link Position}.
     * 
     * @param organizationalUnit
     *            the organizational unit
     * @return the position
     */
    public Position belongstoOrganization(OrganizationUnit organizationalUnit) {

        this.organizationalUnit = organizationalUnit;
        return this;
    }

    public Set<PositionImpl> getSubordinatePositionImpls() {

        if (subordinatePositions == null) {
            subordinatePositions = new HashSet<PositionImpl>();
        }
        return subordinatePositions;
    }

    /**
     * Translates a Position into a corresponding PositionImpl object.
     * 
     * Furthermore some constrains are checked.
     * 
     * @param positionId
     *            - a Position object
     * @return positionImpl - the casted Position object
     */
    public static PositionImpl asPositionImpl(UUID positionId) {

        if (positionId == null) {
            throw new DalmatinaRuntimeException("The Position parameter is null.");
        }

        PositionImpl positionImpl = (PositionImpl) ServiceFactory.getIdentityService().getPosition(positionId);

        if (positionImpl == null) {
            throw new DalmatinaRuntimeException("There exists no Position with the id " + positionId + ".");
        }
        return positionImpl;
    }
}
