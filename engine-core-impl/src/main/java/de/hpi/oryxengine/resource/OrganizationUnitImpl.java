package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the {@link OrganizationUnit} Interface.
 */
public class OrganizationUnitImpl extends ResourceImpl<OrganizationUnit> implements OrganizationUnit {

    /** The positions. */
    private Set<PositionImpl> positions;

    /** The super organizational unit. */
    private OrganizationUnit superOrganizationalUnit;

    /** The child organization units. */
    private Set<OrganizationUnitImpl> childOrganizationUnits;

    /**
     * Instantiates a new {@link OrganizationUnitImpl}.
     * 
     * @param organizationalUnitId
     *            the organizational unit id
     */
    public OrganizationUnitImpl(String organizationalUnitId) {

        super(organizationalUnitId);
        superOrganizationalUnit = null;
    }

    @Override
    public Set<Position> getPositions() {

        Set<Position> positionList = new HashSet<Position>(getPositionImpls());
        return Collections.unmodifiableSet(positionList);
    }

    /**
     * Gets the positionImpls.
     * 
     * @return the positionImpls
     */
    protected Set<PositionImpl> getPositionImpls() {

        if (positions == null) {
            positions = new HashSet<PositionImpl>();
        }
        return positions;
    }

    @Override
    public OrganizationUnit getSuperOrganizationUnit() {

        return superOrganizationalUnit;
    }

    /**
     * Sets the super organization unit.
     * 
     * @param organizationalUnit
     *            the organizational unit
     * @return the organization unit
     */
    public OrganizationUnit setSuperOrganizationUnit(OrganizationUnit organizationalUnit) {

        superOrganizationalUnit = organizationalUnit;
        return this;
    }

    /**
     * Gets the child organisation unit impls.
     * 
     * @return the child organisation unit impls
     */
    protected Set<OrganizationUnitImpl> getChildOrganisationUnitImpls() {

        if (childOrganizationUnits == null) {
            childOrganizationUnits = new HashSet<OrganizationUnitImpl>();
        }
        return childOrganizationUnits;
    }

}
