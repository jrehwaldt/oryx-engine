package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An Organization Unit is a functional grouping of positions. Common examples might include departments like
 * Marketing, Sales, Human Resource, etc. .
 * 
 * An OrganizationUnit is such as the Fachberecih BPT.
 * 
 * @author Gerardo Navarro Suarez
 */
public class OrganizationUnitImpl extends AbstractResourceImpl<OrganizationUnitImpl> {

    private Set<PositionImpl> positions;

    private OrganizationUnitImpl superOrganizationalUnit;

    private Set<OrganizationUnitImpl> childOrganizationUnits;

    /**
     * Instantiates a new {@link OrganizationUnitImpl}.
     * 
     * @param organizationalUnitName
     *            the name of the organizational unit
     */
    public OrganizationUnitImpl(@Nonnull String organizationalUnitName) {

        super(organizationalUnitName, ResourceType.ORGANIZATION_UNIT);
        superOrganizationalUnit = null; // TODO never set to something else...
    }

    /**
     * Returns a read-only Set of all Positions belonging to that OrganizationUnit.
     * 
     * @return a read-only Set of all Positions belonging to that OrganizationUnit
     */
    public @Nonnull Set<PositionImpl> getPositionsImmutable() {
        
        Set<PositionImpl> positionList = new HashSet<PositionImpl>(getPositions());
        return Collections.unmodifiableSet(positionList);
    }
    
    /**
     * Returns a set of all Positions belonging to that OrganizationUnit.
     * 
     * @return a set of all Positions belonging to that OrganizationUnit
     */
    protected @Nonnull Set<PositionImpl> getPositions() {

        if (positions == null) {
            positions = new HashSet<PositionImpl>();
        }
        return positions;
    }
    
    /**
     * Get the superior OrganizationUnit.
     *
     * @return OrganizationUnit - is the superior OrganzationUnit
     */
    public @Nullable OrganizationUnitImpl getSuperOrganizationUnit() {

        return superOrganizationalUnit;
    }
    /**
     * Sets the superior OrganizationUnit.
     *
     * @param superOrganizationalUnit - the superior OrganzationUnit
     */
    protected void setSuperOrganizationUnit(@Nullable OrganizationUnitImpl superOrganizationalUnit) {
        
        this.superOrganizationalUnit = superOrganizationalUnit;
    }
    
    /**
     * Get the child {@link OrganizationUnitImpl}.
     *
     * @return Set<OrganizationUnitImpl> - the child units
     */
    protected @Nonnull Set<OrganizationUnitImpl> getChildOrganisationUnits() {

        if (childOrganizationUnits == null) {
            childOrganizationUnits = new HashSet<OrganizationUnitImpl>();
        }
        return childOrganizationUnits;
    }
}
