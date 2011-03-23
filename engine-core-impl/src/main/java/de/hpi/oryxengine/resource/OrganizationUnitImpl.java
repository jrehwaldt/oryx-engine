package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;

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

        super(organizationalUnitId, ResourceType.ORGANIZATION_UNIT);
        superOrganizationalUnit = null;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * Translates a OrganizationUnit into a corresponding OrganizationUnitImpl object.
     * 
     * Furthermore some constrains are checked.
     * 
     * @param organizationUnit
     *            - a OrganizationUnit object
     * @return organizationUnitImpl - the casted OrganizationUnit object
     */
    public static OrganizationUnitImpl asOrganizationUnitImpl(OrganizationUnit organizationUnit) {

        if (organizationUnit == null) {
            throw new DalmatinaRuntimeException("The OrganizationUnit parameter is null.");
        }

        OrganizationUnitImpl organizationUnitImpl = (OrganizationUnitImpl) organizationUnit;
        if (!ServiceFactory.getIdentityService().getOrganizationUnits().contains(organizationUnitImpl)) {
            throw new DalmatinaRuntimeException("There exists no OrganizationUnit with the id "
                + organizationUnit.getId() + ".");
        }
        return organizationUnitImpl;
    }

}
