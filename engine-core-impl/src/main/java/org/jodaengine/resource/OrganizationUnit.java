package org.jodaengine.resource;

import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.exception.ResourceNotAvailableException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * An Organization Unit is a functional grouping of positions. Common examples might include departments like Marketing,
 * Sales, Human Resource, etc. .
 * 
 * An OrganizationUnit is such as the department BPT.
 * 
 * @author Gerardo Navarro Suarez
 */
public class OrganizationUnit extends AbstractOrganizationUnit {

    private Set<Position> positions;

    private OrganizationUnit superOrganizationalUnit;

    private Set<OrganizationUnit> childOrganizationUnits;
    
    /**
     * Hidden constructor.
     */
    protected OrganizationUnit() { }

    /**
     * Instantiates a new {@link OrganizationUnit}.
     * 
     * @param organizationalUnitName
     *            the name of the organizational unit
     */
    public OrganizationUnit(@Nonnull String organizationalUnitName) {

        super(organizationalUnitName);
        superOrganizationalUnit = null; // TODO never set to something else...
    }

    @Override
    public @Nonnull
    Set<AbstractPosition> getPositionsImmutable() {

        Set<AbstractPosition> positionList = new HashSet<AbstractPosition>(getPositions());
        return Collections.unmodifiableSet(positionList);
    }

    /**
     * Gets the positions offered by this {@link AbstractOrganizationUnit}.
     * 
     * @return the positions
     */
    protected @Nonnull
    Set<Position> getPositions() {

        if (positions == null) {
            positions = new HashSet<Position>();
        }
        return positions;
    }

    @Override
    public @Nullable
    OrganizationUnit getSuperOrganizationUnit() {

        return superOrganizationalUnit;
    }

    /**
     * Sets the superior OrganizationUnit.
     * 
     * @param superOrganizationalUnit
     *            - the superior OrganzationUnit
     */
    protected void setSuperOrganizationUnit(@Nullable OrganizationUnit superOrganizationalUnit) {

        this.superOrganizationalUnit = superOrganizationalUnit;
    }

    /**
     * Get the child {@link OrganizationUnit}.
     * 
     * @return Set<OrganizationUnitImpl> - the child units
     */
    protected @Nonnull
    Set<OrganizationUnit> getChildOrganisationUnits() {

        if (childOrganizationUnits == null) {
            childOrganizationUnits = new HashSet<OrganizationUnit>();
        }
        return childOrganizationUnits;
    }

    /**
     * Translates a OrganizationUnit into a corresponding OrganizationUnitImpl object.
     * 
     * Furthermore some constrains are checked.
     * 
     * @param organizationUnitId
     *            - a OrganizationUnit object
     * @return organizationUnitImpl - the casted OrganizationUnit object
     * @throws ResourceNotAvailableException 
     */
    public static OrganizationUnit asOrganizationUnitImpl(UUID organizationUnitId)
    throws ResourceNotAvailableException {

        if (organizationUnitId == null) {
            throw new JodaEngineRuntimeException("The OrganizationUnit parameter is null.");
        }

        OrganizationUnit organizationUnitImpl = (OrganizationUnit) ServiceFactory.getIdentityService()
        .getOrganizationUnit(organizationUnitId);
     
        if (organizationUnitImpl == null) {
            throw new JodaEngineRuntimeException("There exists no OrganizationUnit with the id " + organizationUnitId
                + ".");
        }
        return organizationUnitImpl;
    }
}
