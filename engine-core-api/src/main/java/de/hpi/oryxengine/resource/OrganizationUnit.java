package de.hpi.oryxengine.resource;

import java.util.Set;

/**
 * An Organization Unit is a functional grouping of positions. Common examples might include departments like
 * Marketing, Sales, Human Resource, etc. .
 * 
 * An OrganizationUnit 
 * 
 * @author Gery
 */
public interface OrganizationUnit extends Resource<OrganizationUnit> {

    /**
     * Get the superior OrganizationUnit.
     *
     * @return OrganizationUnit - is the superior OrganzationUnit
     */
    OrganizationUnit getSuperOrganizationUnit();
    
    // Das werde ich vielleicht noch ändern.
    // OrganizationUnit setSuperOrganizationUnit(OrganizationUnit organizationUnit);
    
    /**
     * Returns a read-only Set of all Positions belonging to that OrganizationUnit.
     * 
     * @return a read-only Set of all Positions belonging to that OrganizationUnit
     */
    Set<Position> getPositions();
}
