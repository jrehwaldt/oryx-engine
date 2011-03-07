package de.hpi.oryxengine.resource;

import java.lang.annotation.Inherited;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hpi.oryxengine.resource.OrganizationUnit;
import de.hpi.oryxengine.resource.Position;

/**
 * 
 * {@inheritdoc}
 * 
 * @author Gerardo Navarro Suarez
 */
public class OrganizationUnitImpl extends ResourceImpl<OrganizationUnit> implements OrganizationUnit {

    private Set<PositionImpl> positions;
    private OrganizationUnit superOrganizationalUnit;
    private Set<OrganizationUnitImpl> childOrganizationUnits;

    public OrganizationUnitImpl(String organizationalUnitId) {

        super(organizationalUnitId);
        superOrganizationalUnit = null;
    }

    public Set<Position> getPositions() {

        Set<Position> positionList = new HashSet<Position>(getPositionImpls());
        return Collections.unmodifiableSet(positionList);
    }

    public Set<PositionImpl> getPositionImpls() {

        // TODO Nachschauen, ob es bessere Implmentierung gibt für ArrayList<Position> aus ArrayList<PositionImpl>
        if (positions == null) {
            positions = new HashSet<PositionImpl>();
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

    public Set<OrganizationUnitImpl> getChildOrganisationUnitImpls() {

        if (childOrganizationUnits == null) {
            childOrganizationUnits = new HashSet<OrganizationUnitImpl>();
        }
        return childOrganizationUnits;
    }

}
