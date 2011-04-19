package de.hpi.oryxengine.resource;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonBackReference;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;

/**
 * A Position refers to a unique job within an organization. Examples might include Positions like the CEO, bank
 * manager, secretary, etc. .
 * 
 * The purpose for this model is to define lines-of-reporting within the organization model in order to get a
 * Who-Is-My-Boss?-hierarchy.
 * 
 * @author Gerardo Navarro Suarez
 */
public class Position extends AbstractPosition {

    private AbstractParticipant positionHolder;

    private OrganizationUnit organizationalUnit;

    private Position superiorPosition;

    private Set<Position> subordinatePositions;

    /**
     * The Default Constructor. Creates a position object with the given id.
     * 
     * @param positionName
     *            - identifier for the position Object
     */
    public Position(String positionName) {

        super(positionName);
    }

    @Override
    public AbstractParticipant getPositionHolder() {

        return positionHolder;
    }

    /**
     * Sets the position holder position.
     * 
     * @param participant
     *            the participant
     * @return the subordinate position (this)
     */
    protected @Nonnull
    AbstractPosition setPositionHolder(@Nullable AbstractParticipant participant) {

        positionHolder = participant;
        return this;
    }

    @Override
    public AbstractPosition getSuperiorPosition() {

        return superiorPosition;
    }

    /**
     * Sets the superior position.
     * 
     * @param position
     *            the superior position
     * @return the subordinate position (this)
     */
    protected @Nonnull
    Position setSuperiorPosition(@Nullable Position position) {

        superiorPosition = position;

        return this;
    }

    @Override
    public @Nullable
    OrganizationUnit belongstoOrganization() {

        return organizationalUnit;
    }

    /**
     * Sets the {@link OrganizationUnit} that offers this {@link AbstractPosition}.
     * 
     * @param organizationalUnit
     *            the organizational unit
     * @return the position (this)
     */
    public @Nonnull
    Position belongstoOrganization(@Nullable OrganizationUnit organizationalUnit) {

        this.organizationalUnit = organizationalUnit;
        return this;
    }

    /**
     * Gets the subordinate position.
     * 
     * @return the subordinate position
     */
    public @Nonnull
    Set<Position> getSubordinatePositions() {

        if (subordinatePositions == null) {
            subordinatePositions = new HashSet<Position>();
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
     * @return positionImpl - the casted {@link Position}
     */
    public static Position asPositionImpl(UUID positionId) {

        if (positionId == null) {
            throw new DalmatinaRuntimeException("The Position parameter is null.");
        }

        Position positionImpl = (Position) ServiceFactory.getIdentityService().getPosition(positionId);

        if (positionImpl == null) {
            throw new DalmatinaRuntimeException("There exists no Position with the id " + positionId + ".");
        }
        return positionImpl;
    }
}
