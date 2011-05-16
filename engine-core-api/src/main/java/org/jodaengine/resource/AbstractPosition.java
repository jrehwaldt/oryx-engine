package org.jodaengine.resource;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * A Position refers to a unique job within an organization. Examples might include Positions like the CEO, bank
 * manager, secretary, etc. .
 * 
 * The purpose for this model is to define lines-of-reporting within the organization model in order to get a
 * Who-Is-My-Boss?-hierarchy.
 * 
 * @author Gerardo Navarro Suarez
 */
public abstract class AbstractPosition extends AbstractResource<AbstractPosition> {

    /**
     * Hidden constructor.
     */
    protected AbstractPosition() {

    }

    /**
     * The Default Constructor. Creates a position object with the given id.
     * 
     * @param resourceID
     *            - the resource id
     * @param positionName
     *            - identifier for the position Object
     */
    public AbstractPosition(@Nonnull UUID resourceID, String positionName) {

        super(resourceID, positionName, ResourceType.POSITION);
    }

    /**
     * The Default Constructor. Creates a position object with the given id.
     * 
     * @param positionName
     *            - identifier for the position Object
     */
    public AbstractPosition(String positionName) {

        super(positionName, ResourceType.POSITION);
    }

    // Vielleicht später mit Prioritäten, oder Wichitgkeit der Person

    /**
     * Gets the {@link AbstractParticipant} that occupies this {@link AbstractPosition}.
     * 
     * @return a Participant - {@link AbstractParticipant} that occupies this {@link AbstractPosition}
     */
    @JsonBackReference
    public abstract AbstractParticipant getPositionHolder();

    /**
     * Gets the superior position.
     * 
     * @return the superior position
     */
    @JsonProperty
    public abstract AbstractPosition getSuperiorPosition();

    /**
     * Return the {@link AbstractOrganizationUnit} where this {@link AbstractPosition} belongs to.
     * 
     * @return the {@link AbstractOrganizationUnit} where this {@link AbstractPosition} belongs to
     */
    @JsonBackReference
    public abstract AbstractOrganizationUnit belongstoOrganization();
}
