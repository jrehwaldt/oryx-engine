package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;

/**
 * Implementation of the {@link OrganizationUnit} Interface.
 */
public class OrganizationUnitImpl extends ResourceImpl<OrganizationUnit> implements OrganizationUnit {

    private Set<PositionImpl> positions;

    private OrganizationUnit superOrganizationalUnit;

    private Set<OrganizationUnitImpl> childOrganizationUnits;

    /**
     * Instantiates a new {@link OrganizationUnitImpl}.
     * 
     * @param organizationalUnitName
     *            the name of the organizational unit
     */
    public OrganizationUnitImpl(String organizationalUnitName) {

        super(organizationalUnitName, ResourceType.ORGANIZATION_UNIT);
        superOrganizationalUnit = null;
    }

    @Override
    public Set<Position> getPositions() {

        Set<Position> positionList = new HashSet<Position>(getPositionImpls());
        return Collections.unmodifiableSet(positionList);
    }

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

    public OrganizationUnit setSuperOrganizationUnit(OrganizationUnit organizationalUnit) {

        superOrganizationalUnit = organizationalUnit;
        return this;
    }

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
                + organizationUnit.getID() + ".");
        }
        return organizationUnitImpl;
    }
}
