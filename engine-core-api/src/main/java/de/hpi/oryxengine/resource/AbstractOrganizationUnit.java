/*
 * 
 */
package de.hpi.oryxengine.resource;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

/**
 * An Organization Unit is a functional grouping of positions. Common examples might include departments like Marketing,
 * Sales, Human Resource, etc. .
 * 
 * An OrganizationUnit
 * 
 * @author Gerardo Navarro Suarez
 */
public abstract class AbstractOrganizationUnit extends AbstractResource<AbstractOrganizationUnit> {

    /**
     * Instantiates a new {@link OrganizationUnit}.
     * 
     * @param resourceID
     *            - the resource id
     * @param organizationalUnitName
     *            - the name of the organizational unit
     */
    public AbstractOrganizationUnit(@Nonnull UUID resourceID, @Nonnull String organizationalUnitName) {

        super(resourceID, organizationalUnitName, ResourceType.ORGANIZATION_UNIT);
    }

    /**
     * Instantiates a new {@link OrganizationUnit}.
     * 
     * @param organizationalUnitName
     *            - the name of the organizational unit
     */
    public AbstractOrganizationUnit(@Nonnull String organizationalUnitName) {

        super(organizationalUnitName, ResourceType.ORGANIZATION_UNIT);
    }

    /**
     * Get the superior OrganizationUnit.
     * 
     * @return OrganizationUnit - is the superior OrganzationUnit
     */
    public abstract AbstractOrganizationUnit getSuperOrganizationUnit();

    // Das werde ich vielleicht noch ändern.
    // OrganizationUnit setSuperOrganizationUnit(OrganizationUnit organizationUnit);

    /**
     * Returns a set of all Positions belonging to that OrganizationUnit.
     * 
     * @return a set of all Positions belonging to that OrganizationUnit
     */
    public abstract Set<AbstractPosition> getPositionsImmutable();
}
