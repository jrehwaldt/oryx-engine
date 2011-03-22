package de.hpi.oryxengine.resource;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of {@link Position} Interface.
 */
public class PositionImpl extends ResourceImpl<Position> implements Position {

    /** The {@link Participant} that occupies this {@link Position}. */
    private Participant positionHolder;
    
    /** The {@link OrganizationUnit} that offers the {@link Position}. */
    private OrganizationUnit organizationalUnit;
    
    /** The superior {@link Position}. */
    private Position superiorPosition;
    
    /** The subordinate positions. */
    private Set<PositionImpl> subordinatePositions;

    /**
     * The Default Constructor. Creates a position object with the given id.
     * 
     * @param positionId
     *            - identifier for the position Object
     */
    public PositionImpl(String positionId) {

        super(positionId, ResourceType.POSITION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Participant getPositionHolder() {

        return positionHolder;
    }

    /**
     * Sets the position holder.
     *
     * @param participant the participant
     * @return the position
     */
    public Position setPositionHolder(Participant participant) {

        positionHolder = participant;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Position getSuperiorPosition() {

        return superiorPosition;
    }

    /**
     * Sets the superior position.
     *
     * @param position the position
     * @return the position
     */
    public Position setSuperiorPosition(Position position) {

        superiorPosition = position;

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrganizationUnit belongstoOrganization() {

        // TODO hier nochmal quatschen ob eine Position nicht doch eine Orga haben muss!!!
        return organizationalUnit;
    }

    /**
     * Sets the {@link OrganizationUnit} that offers this {@link Position}.
     *
     * @param organizationalUnit the organizational unit
     * @return the position
     */
    public Position belongstoOrganization(OrganizationUnit organizationalUnit) {

        this.organizationalUnit = organizationalUnit;
        return this;
    }

    /**
     * Gets the subordinate position impls.
     *
     * @return the subordinate position impls
     */
    public Set<PositionImpl> getSubordinatePositionImpls() {

        if (subordinatePositions == null) {
            subordinatePositions = new HashSet<PositionImpl>();
        }
        return subordinatePositions;
    }
}
