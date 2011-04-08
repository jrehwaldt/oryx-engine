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
public class OrganizationUnit extends AbstractResource<OrganizationUnit> {

    private Set<Position> positions;

    private OrganizationUnit superOrganizationalUnit;

    private Set<OrganizationUnit> childOrganizationUnits;

    /**
     * Instantiates a new {@link OrganizationUnit}.
     * 
     * @param organizationalUnitName
     *            the name of the organizational unit
     */
    public OrganizationUnit(@Nonnull String organizationalUnitName) {

        super(organizationalUnitName, ResourceType.ORGANIZATION_UNIT);
        superOrganizationalUnit = null; // TODO never set to something else...
    }

    /**
     * Returns a read-only Set of all Positions belonging to that OrganizationUnit.
     * 
     * @return a read-only Set of all Positions belonging to that OrganizationUnit
     */
    public @Nonnull Set<Position> getPositionsImmutable() {
        
        Set<Position> positionList = new HashSet<Position>(getPositions());
        return Collections.unmodifiableSet(positionList);
    }
    
    /**
     * Returns a set of all Positions belonging to that OrganizationUnit.
     * 
     * @return a set of all Positions belonging to that OrganizationUnit
     */
    protected @Nonnull Set<Position> getPositions() {

        if (positions == null) {
            positions = new HashSet<Position>();
        }
        return positions;
    }
    
    /**
     * Get the superior OrganizationUnit.
     *
     * @return OrganizationUnit - is the superior OrganzationUnit
     */
    public @Nullable OrganizationUnit getSuperOrganizationUnit() {

        return superOrganizationalUnit;
    }
    /**
     * Sets the superior OrganizationUnit.
     *
     * @param superOrganizationalUnit - the superior OrganzationUnit
     */
    protected void setSuperOrganizationUnit(@Nullable OrganizationUnit superOrganizationalUnit) {
        
        this.superOrganizationalUnit = superOrganizationalUnit;
    }
    
    /**
     * Get the child {@link OrganizationUnit}.
     *
     * @return Set<OrganizationUnitImpl> - the child units
     */
    protected @Nonnull Set<OrganizationUnit> getChildOrganisationUnits() {

        if (childOrganizationUnits == null) {
            childOrganizationUnits = new HashSet<OrganizationUnit>();
        }
        return childOrganizationUnits;
    }
}
