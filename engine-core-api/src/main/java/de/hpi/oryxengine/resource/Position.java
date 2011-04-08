package de.hpi.oryxengine.resource;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A Position refers to a unique job within an organization. Examples might include Positions like the CEO, bank
 * manager, secretary, etc. .
 * 
 * The purpose for this model is to define lines-of-reporting within the organization model in order to get a
 * Who-Is-My-Boss?-hierarchy.
 * 
 * @author Gerardo Navarro Suarez
 */
public class Position extends AbstractResource<Position> {
    
    private Participant positionHolder;
    
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

        super(positionName, ResourceType.POSITION);
    }
    
    /**
     * Gets the Participant that occupies this Position.
     * 
     * @return a Participant - Participant that occupies this Position
     */
    public Participant getPositionHolder() {

        return positionHolder;
    }

    /**
     * Sets the position holder position.
     * 
     * @param participant the participant
     * @return the subordinate position (this)
     */
    protected @Nonnull Position setPositionHolder(@Nullable Participant participant) {

        positionHolder = participant;
        return this;
    }
    
    /**
     * Gets the superior position.
     * 
     * @return the superior position
     */
    public Position getSuperiorPosition() {

        return superiorPosition;
    }
    
    /**
     * Sets the superior position.
     * 
     * @param position the superior position
     * @return the subordinate position (this)
     */
    protected @Nonnull Position setSuperiorPosition(@Nullable Position position) {

        superiorPosition = position;

        return this;
    }

    /**
     * Return the OrganizationUnit where this Position belongs to.
     * 
     * @return the OrganizationUnit where this Position belongs to
     */
    public @Nullable OrganizationUnit belongsToOrganization() {
        
        return organizationalUnit;
    }

    /**
     * Sets the {@link OrganizationUnit} that offers this {@link AbstractPosition}.
     * 
     * @param organizationalUnit
     *            the organizational unit
     * @return the position (this)
     */
    public @Nonnull Position belongsToOrganization(@Nullable OrganizationUnit organizationalUnit) {

        this.organizationalUnit = organizationalUnit;
        return this;
    }

    /**
     * Gets the subordinate position.
     * 
     * @return the subordinate position
     */
    public @Nonnull Set<Position> getSubordinatePositions() {

        if (subordinatePositions == null) {
            subordinatePositions = new HashSet<Position>();
        }
        return subordinatePositions;
    }
}
