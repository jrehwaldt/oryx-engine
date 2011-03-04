package de.hpi.oryxengine.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hpi.oryxengine.identity.OrganizationUnit;
import de.hpi.oryxengine.identity.Position;

/**
 * 
 * @author Gerardo Navarro Suarez
 */
public class OrganizationUnitImpl extends ResourceImpl<OrganizationUnit> implements OrganizationUnit {

    private ArrayList<PositionImpl> positions;
    private OrganizationUnit superOrganizationalUnit;

    public OrganizationUnitImpl(String organizationalUnitId) {

        super(organizationalUnitId);
        superOrganizationalUnit = null;
    }

    public List<Position> getPositions() {

        List<Position> positionList = new ArrayList<Position>(getPositionImpls());
        return Collections.unmodifiableList(positionList);
    }

    public List<PositionImpl> getPositionImpls() {

        // TODO Nachschauen, ob es bessere Implmentierung gibt für ArrayList<Position> aus ArrayList<PositionImpl>
        if (positions == null) {
            positions = new ArrayList<PositionImpl>();
        }
        return positions;
    }


    public OrganizationUnit getSuperOrganizationUnit() {

        return superOrganizationalUnit;
    }

    public OrganizationUnit setSuperOrganizationUnit(OrganizationUnit organizationalUnit) {

        superOrganizationalUnit = organizationalUnit;
        return this;
    }

    public void addPosition(PositionImpl position) {
        
        if (getPositionImpls().contains(position)) {
            // Nothing to do because it is already there
            return;
        }
        
        getPositionImpls().add(position);
    }

}
