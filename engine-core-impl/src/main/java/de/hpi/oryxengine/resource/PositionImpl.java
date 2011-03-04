package de.hpi.oryxengine.resource;

import de.hpi.oryxengine.identity.OrganizationUnit;
import de.hpi.oryxengine.identity.Participant;
import de.hpi.oryxengine.identity.Position;

/**
 * {@inheritDoc}
 * 
 * @author Gerardo Navarro Suarez
 */
public class PositionImpl extends ResourceImpl<Position> implements Position {

    private Participant positionHolder;
    private OrganizationUnit organizationalUnit;
    private Position superiorPosition;

    /**
     * The Default Constructor. Creates a position object with the given id.
     * 
     * @param positionId
     *            - identifier for the position Object
     */
    public PositionImpl(String positionId) {

        super(positionId);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Participant getPositionHolder() {

        return positionHolder;
    }

    @Override
    public Position setPositionHolder(Participant participant) {

        positionHolder = participant;
        return this;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Position getSuperiorPosition() {

        return superiorPosition;
    }

    @Override
    public Position setSuperiorPosition(Position position) {

        superiorPosition = position;
        return this;
    }

    @Override
    public OrganizationUnit belongstoOrganization() {

        // TODO hier nochmal quatschen ob eine Position nicht doch eine Orga haben muss!!!
        return organizationalUnit;
    }

     public Position belongstoOrganization(OrganizationUnit organizationalUnit) {
    
     this.organizationalUnit = organizationalUnit;
     return this;
     }
}
